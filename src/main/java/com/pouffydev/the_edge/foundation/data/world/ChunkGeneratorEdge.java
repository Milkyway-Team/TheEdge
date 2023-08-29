package com.pouffydev.the_edge.foundation.data.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pouffydev.the_edge.TEBlocks;
import com.pouffydev.the_edge.TEFluids;
import com.pouffydev.the_edge.foundation.data.world.biome.BiomeKeys;
import com.pouffydev.the_edge.foundation.data.world.biome.TEBiomeProvider;
import com.pouffydev.the_edge.foundation.data.world.biome.chunkgenerators.ChunkGeneratorWrapper;
import com.pouffydev.the_edge.foundation.data.world.biome.chunkgenerators.TEGenerationSettings;
import com.pouffydev.the_edge.foundation.data.world.biome.chunkgenerators.warp.NoiseModifier;
import com.pouffydev.the_edge.foundation.data.world.biome.chunkgenerators.warp.TEBlendedNoise;
import com.pouffydev.the_edge.foundation.data.world.biome.chunkgenerators.warp.TENoiseInterpolator;
import com.pouffydev.the_edge.foundation.data.world.biome.chunkgenerators.warp.TETerrainWarp;
import com.pouffydev.the_edge.foundation.data.world.biome.feature.TheEdgeFeatures;
import com.pouffydev.the_edge.foundation.data.world.biome.util.TEFeature;
import com.pouffydev.the_edge.foundation.util.IntPair;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.Predicate;

public class ChunkGeneratorEdge extends ChunkGeneratorWrapper {
    public static final Codec<ChunkGeneratorEdge> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            ChunkGenerator.CODEC.fieldOf("wrapped_generator").forGetter(o -> o.delegate),
            RegistryOps.retrieveRegistry(Registry.STRUCTURE_SET_REGISTRY).forGetter(o -> o.structureSets),
            NoiseGeneratorSettings.CODEC.fieldOf("noise_generation_settings").forGetter(o -> o.noiseGeneratorSettings),
            Codec.BOOL.fieldOf("generate_dark_forest_canopy").forGetter(o -> o.genDarkForestCanopy),
            Codec.BOOL.fieldOf("monster_spawns_below_sealevel").forGetter(o -> o.monsterSpawnsBelowSeaLevel),
            Codec.INT.optionalFieldOf("dark_forest_canopy_height").forGetter(o -> o.darkForestCanopyHeight),
            Codec.BOOL.fieldOf("use_overworld_seed").forGetter(o -> false) // Don't make this persistent, we want to load the stored seed on existing worlds! This is purely used on world creation ONLY!!
    ).apply(instance, instance.stable(ChunkGeneratorEdge::new)));
    
    private final Holder<NoiseGeneratorSettings> noiseGeneratorSettings;
    private final boolean genDarkForestCanopy;
    private final boolean monsterSpawnsBelowSeaLevel;
    private final Optional<Integer> darkForestCanopyHeight;
    
    private final BlockState defaultBlock;
    private final BlockState defaultFluid;
    private final Optional<Climate.Sampler> surfaceNoiseGetter;
    private final Optional<TETerrainWarp> warper;
    
    public final ConcurrentHashMap<ChunkPos, TEFeature> featureCache = new ConcurrentHashMap<>();
    private static final BlockState[] EMPTY_COLUMN = new BlockState[0];
    
    public ChunkGeneratorEdge(ChunkGenerator delegate, Registry<StructureSet> structures, Holder<NoiseGeneratorSettings> noiseGenSettings, boolean genDarkForestCanopy, boolean monsterSpawnsBelowSeaLevel, Optional<Integer> darkForestCanopyHeight, boolean owSeed) {
        //super(delegate.getBiomeSource(), delegate.getBiomeSource(), delegate.getSettings(), delegate instanceof NoiseBasedChunkGenerator noiseGen ? noiseGen.seed : delegate.strongholdSeed);
        super(structures, owSeed ? delegate = delegate.withSeed(TheEdgeFeatures.seed) : delegate);
        this.noiseGeneratorSettings = noiseGenSettings;
        this.genDarkForestCanopy = genDarkForestCanopy;
        this.monsterSpawnsBelowSeaLevel = monsterSpawnsBelowSeaLevel;
        this.darkForestCanopyHeight = darkForestCanopyHeight;
        
        if (delegate instanceof NoiseBasedChunkGenerator noiseGen) {
            this.defaultBlock = noiseGen.defaultBlock;
            this.defaultFluid = noiseGenSettings.value().defaultFluid();
            this.surfaceNoiseGetter = Optional.of(noiseGen.sampler);
        } else {
            this.defaultBlock = TEBlocks.EDGESTONE.get().defaultBlockState();
            this.defaultFluid = TEFluids.STARFALL.get().defaultFluidState().createLegacyBlock();
            this.surfaceNoiseGetter = Optional.empty();
        }
        
        NoiseSettings settings = noiseGenSettings.value().noiseSettings();
        if (delegate.getBiomeSource() instanceof TEBiomeProvider source) {
            WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(delegate.ringPlacementSeed));
            TEBlendedNoise blendedNoise = new TEBlendedNoise(random, settings.noiseSamplingSettings(), settings.getCellWidth(), settings.getCellHeight());
            NoiseModifier modifier = NoiseModifier.PASS;
            this.warper = Optional.of(new TETerrainWarp(settings.getCellWidth(), settings.getCellHeight(), settings.getCellCountY(), source, settings, blendedNoise, modifier));
        } else {
            this.warper = Optional.empty();
        }
    }
    
    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }
    
    @Override
    public ChunkGenerator withSeed(long newSeed) {
        return new ChunkGeneratorEdge(this.delegate.withSeed(newSeed), this.structureSets, this.noiseGeneratorSettings, this.genDarkForestCanopy, this.monsterSpawnsBelowSeaLevel, this.darkForestCanopyHeight, false);
    }
    
    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types heightMap, LevelHeightAccessor level) {
        if (warper.isEmpty()) {
            return super.getBaseHeight(x, z, heightMap, level);
        } else {
            NoiseSettings settings = this.noiseGeneratorSettings.value().noiseSettings();
            int minY = Math.max(settings.minY(), level.getMinBuildHeight());
            int maxY = Math.min(settings.minY() + settings.height(), level.getMaxBuildHeight());
            int minCell = Mth.intFloorDiv(minY, settings.getCellHeight());
            int maxCell = Mth.intFloorDiv(maxY - minY, settings.getCellHeight());
            return maxCell <= 0 ? level.getMinBuildHeight() : this.iterateNoiseColumn(x, z, null, heightMap.isOpaque(), minCell, maxCell).orElse(level.getMinBuildHeight());
        }
    }
    
    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level) {
        if (warper.isEmpty()) {
            return super.getBaseColumn(x, z, level);
        } else {
            NoiseSettings settings = this.noiseGeneratorSettings.value().noiseSettings();
            int minY = Math.max(settings.minY(), level.getMinBuildHeight());
            int maxY = Math.min(settings.minY() + settings.height(), level.getMaxBuildHeight());
            int minCell = Mth.intFloorDiv(minY, settings.getCellHeight());
            int maxCell = Mth.intFloorDiv(maxY - minY, settings.getCellHeight());
            if (maxCell <= 0) {
                return new NoiseColumn(minY, EMPTY_COLUMN);
            } else {
                BlockState[] ablockstate = new BlockState[maxCell * settings.getCellHeight()];
                this.iterateNoiseColumn(x, z, ablockstate, null, minCell, maxCell);
                return new NoiseColumn(minY, ablockstate);
            }
        }
    }
    
    //This logic only seems to concern very specific features, but it does need the Warp
    protected OptionalInt iterateNoiseColumn(int x, int z, BlockState[] states, Predicate<BlockState> predicate, int min, int max) {
        NoiseSettings settings = this.noiseGeneratorSettings.value().noiseSettings();
        int cellWidth = settings.getCellWidth();
        int cellHeight = settings.getCellHeight();
        int xDiv = Math.floorDiv(x, cellWidth);
        int zDiv = Math.floorDiv(z, cellWidth);
        int xMod = Math.floorMod(x, cellWidth);
        int zMod = Math.floorMod(z, cellWidth);
        int xMin = xMod / cellWidth;
        int zMin = zMod / cellWidth;
        double[][] columns = new double[][] {
                this.makeAndFillNoiseColumn(xDiv, zDiv, min, max),
                this.makeAndFillNoiseColumn(xDiv, zDiv + 1, min, max),
                this.makeAndFillNoiseColumn(xDiv + 1, zDiv, min, max),
                this.makeAndFillNoiseColumn(xDiv + 1, zDiv + 1, min, max)
        };
        
        for (int cell = max - 1; cell >= 0; cell--) {
            double d00 = columns[0][cell];
            double d10 = columns[1][cell];
            double d20 = columns[2][cell];
            double d30 = columns[3][cell];
            double d01 = columns[0][cell + 1];
            double d11 = columns[1][cell + 1];
            double d21 = columns[2][cell + 1];
            double d31 = columns[3][cell + 1];
            
            for (int height = cellHeight - 1; height >= 0; height--) {
                double dcell = height / (double)cellHeight;
                double lcell = Mth.lerp3(dcell, xMin, zMin, d00, d01, d20, d21, d10, d11, d30, d31);
                int layer = cell * cellHeight + height;
                int maxlayer = layer + min * cellHeight;
                BlockState state = this.generateBaseState(lcell, layer);
                
                if (states != null) {
                    states[layer] = state;
                }
                
                if (predicate != null && predicate.test(state)) {
                    return OptionalInt.of(maxlayer + 1);
                }
            }
        }
        
        return OptionalInt.empty();
    }
    
    @Override
    public CompletableFuture<ChunkAccess> createBiomes(Registry<Biome> biomes, Executor executor, Blender blender, StructureFeatureManager manager, ChunkAccess chunkAccess) {
        //Mimic behaviour of ChunkGenerator, NoiseBasedChunkGenerator does weird things
        return CompletableFuture.supplyAsync(Util.wrapThreadWithTaskName("init_biomes", () -> {
            chunkAccess.fillBiomesFromNoise(this.getBiomeSource(), this.climateSampler());
            return chunkAccess;
        }), Util.backgroundExecutor());
    }
    
    //VanillaCopy of NoiseBasedChunkGenerator#fillFromNoise, only so doFill can be ours
    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, StructureFeatureManager structureManager, ChunkAccess chunkAccess) {
        if (warper.isEmpty()) {
            return super.fillFromNoise(executor, blender, structureManager, chunkAccess);
        } else {
            NoiseSettings settings = this.noiseGeneratorSettings.value().noiseSettings();
            int cellHeight = settings.getCellHeight();
            int minY = Math.max(settings.minY(), chunkAccess.getMinBuildHeight());
            int maxY = Math.min(settings.minY() + settings.height(), chunkAccess.getMaxBuildHeight());
            int mincell = Mth.intFloorDiv(minY, cellHeight);
            int maxcell = Mth.intFloorDiv(maxY - minY, cellHeight);
            
            if (maxcell <= 0) {
                return CompletableFuture.completedFuture(chunkAccess);
            } else {
                int maxIndex = chunkAccess.getSectionIndex(maxcell * cellHeight - 1 + minY);
                int minIndex = chunkAccess.getSectionIndex(minY);
                Set<LevelChunkSection> sections = Sets.newHashSet();
                
                for (int index = maxIndex; index >= minIndex; index--) {
                    LevelChunkSection section = chunkAccess.getSection(index);
                    section.acquire();
                    sections.add(section);
                }
                
                return CompletableFuture.supplyAsync(() -> this.doFill(chunkAccess, mincell, maxcell), Util.backgroundExecutor()).whenCompleteAsync((chunk, throwable) -> {
                    for (LevelChunkSection section : sections) {
                        section.release();
                    }
                }, executor);
            }
        }
    }
    
    private ChunkAccess doFill(ChunkAccess access, int min, int max) {
        NoiseSettings settings = noiseGeneratorSettings.value().noiseSettings();
        int cellWidth = settings.getCellWidth();
        int cellHeight = settings.getCellHeight();
        int cellCountX = 16 / cellWidth;
        int cellCountZ = 16 / cellWidth;
        Heightmap oceanfloor = access.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
        Heightmap surface = access.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
        ChunkPos chunkpos = access.getPos();
        int minX = chunkpos.getMinBlockX();
        int minZ = chunkpos.getMinBlockZ();
        TENoiseInterpolator interpolator = new TENoiseInterpolator(cellCountX, max, cellCountZ, chunkpos, min, this::fillNoiseColumn);
        List<TENoiseInterpolator> list = Lists.newArrayList(interpolator);
        list.forEach(TENoiseInterpolator::initialiseFirstX);
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        
        for (int cellX = 0; cellX < cellCountX; cellX++) {
            int advX = cellX;
            list.forEach((noiseint) -> noiseint.advanceX(advX));
            
            for (int cellZ = 0; cellZ < cellCountZ; cellZ++) {
                LevelChunkSection section = access.getSection(access.getSectionsCount() - 1);
                
                for (int cellY = max - 1; cellY >= 0; cellY--) {
                    int advY = cellY;
                    int advZ = cellZ;
                    list.forEach((noiseint) -> noiseint.selectYZ(advY, advZ));
                    
                    for(int height = cellHeight - 1; height >= 0; height--) {
                        int minheight = (min + cellY) * cellHeight + height;
                        int mincellY = minheight & 15;
                        int minindexY = access.getSectionIndex(minheight);
                        
                        if (access.getSectionIndex(section.bottomBlockY()) != minindexY) {
                            section = access.getSection(minindexY);
                        }
                        
                        double heightdiv = (double)height / (double)cellHeight;
                        list.forEach((noiseint) -> noiseint.updateY(heightdiv));
                        
                        for (int widthX = 0; widthX < cellWidth; widthX++) {
                            int minwidthX = minX + cellX * cellWidth + widthX;
                            int mincellX = minwidthX & 15;
                            double widthdivX = (double)widthX / (double)cellWidth;
                            list.forEach((noiseint) -> noiseint.updateX(widthdivX));
                            
                            for (int widthZ = 0; widthZ < cellWidth; widthZ++) {
                                int minwidthZ = minZ + cellZ * cellWidth + widthZ;
                                int mincellZ = minwidthZ & 15;
                                double widthdivZ = (double)widthZ / (double)cellWidth;
                                double noiseval = interpolator.updateZ(widthdivZ);
                                //BlockState state = this.updateNoiseAndGenerateBaseState(beardifier, this.emptyAquifier, NoiseModifier.PASS, minwidthX, minheight, minwidthZ, noiseval); //TODO
                                BlockState state = this.generateBaseState(noiseval, minheight);
                                
                                if (state != Blocks.AIR.defaultBlockState()) {
                                    if (state.getLightEmission() != 0) {
                                        ProtoChunk proto = (ProtoChunk) access;
                                        mutable.set(minwidthX, minheight, minwidthZ);
                                        proto.addLight(mutable);
                                    }
                                    
                                    section.setBlockState(mincellX, mincellY, mincellZ, state, false);
                                    oceanfloor.update(mincellX, minheight, mincellZ, state);
                                    surface.update(mincellX, minheight, mincellZ, state);
                                    
                                    //Probably not necessary?
//									if (emptyAquifier.shouldScheduleFluidUpdate() && !state.getFluidState().isEmpty()) {
//										mutable.set(minwidthX, minheight, minwidthZ);
//										access.markPosForPostprocessing(mutable);
//									}
                                }
                            }
                        }
                    }
                }
            }
            
            list.forEach(TENoiseInterpolator::swapSlices);
        }
        
        return access;
    }
    
    private double[] makeAndFillNoiseColumn(int x, int z, int min, int max) {
        double[] columns = new double[max + 1];
        this.fillNoiseColumn(columns, x, z, min, max);
        return columns;
    }
    
    private void fillNoiseColumn(double[] columns, int x, int z, int min, int max) {
        NoiseSettings settings = this.noiseGeneratorSettings.value().noiseSettings();
        this.warper.get().fillNoiseColumn(this, columns, x, z, settings, this.getSeaLevel(), min, max);
    }
    
    //Logic based on 1.16. Will only ever get the default Block, Fluid, or Air
    private BlockState generateBaseState(double noiseVal, double level) {
        BlockState state;
        
        if (noiseVal > 0.0D) {
            state = this.defaultBlock;
        } else if (level < this.getSeaLevel()) {
            state = this.defaultFluid;
        } else {
            state = Blocks.AIR.defaultBlockState();
        }
        
        return state;
    }
    
    @Override
    public void buildSurface(WorldGenRegion world, StructureFeatureManager manager, ChunkAccess chunk) {
        this.deformTerrainForFeature(world, chunk);
        
        super.buildSurface(world, manager, chunk);
        
        addGlaciers(world, chunk);
    }
    
    private void addGlaciers(WorldGenRegion primer, ChunkAccess chunk) {
        
        BlockState glacierBase = Blocks.GRAVEL.defaultBlockState();
        BlockState glacierMain = Blocks.PACKED_ICE.defaultBlockState();
        BlockState glacierTop = Blocks.ICE.defaultBlockState();
        
        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {
                Optional<ResourceKey<Biome>> biome = primer.getBiome(primer.getCenter().getWorldPosition().offset(x, 0, z)).unwrapKey();
                if (biome.isEmpty() || !BiomeKeys.glacialPeaks.location().equals(biome.get().location())) continue;
                
                // find the (current) top block
                int gBase = -1;
                for (int y = 127; y >= 0; y--) {
                    Block currentBlock = primer.getBlockState(withY(primer.getCenter().getWorldPosition().offset(x, 0, z), y)).getBlock();
                    if (currentBlock == TEBlocks.EDGESTONE.get()) {
                        gBase = y;
                        primer.setBlock(withY(primer.getCenter().getWorldPosition().offset(x, 0, z), y), glacierBase, 3);
                        break;
                    }
                }
                
                // raise the glacier from that top block
                int gHeight = 32;
                int gTop = Math.min(gBase + gHeight, 127);
                
                for (int y = gBase; y < gTop; y++) {
                    primer.setBlock(withY(primer.getCenter().getWorldPosition().offset(x, 0, z), y), glacierMain, 3);
                }
                primer.setBlock(withY(primer.getCenter().getWorldPosition().offset(x, 0, z), gTop), glacierTop, 3);
            }
        }
    }
    
    @Override
    public void addDebugScreenInfo(List<String> p_208054_, BlockPos p_208055_) {
        //do we do anything with this? we need to implement it for some reason
    }
    protected final void deformTerrainForFeature(WorldGenRegion primer, ChunkAccess chunk) {
        IntPair featureRelativePos = new IntPair();
        TEFeature nearFeature = TEFeature.getNearestFeature(primer.getCenter().x, primer.getCenter().z, primer, featureRelativePos);
        
        //Optional<StructureStart<?>> structureStart = TFGenerationSettings.locateTFStructureInRange(primer.getLevel(), nearFeature, chunk.getPos().getWorldPosition(), nearFeature.size + 1);
        
        if (!nearFeature.requiresTerraforming) {
            return;
        }
        
        final int relativeFeatureX = featureRelativePos.x;
        final int relativeFeatureZ = featureRelativePos.z;
        
        //if (TEFeature.isTheseFeatures(nearFeature)) {
        //    int hdiam = (nearFeature.size * 2 + 1) * 16;
        //
        //    for (int xInChunk = 0; xInChunk < 16; xInChunk++) {
        //        for (int zInChunk = 0; zInChunk < 16; zInChunk++) {
        //            int featureDX = xInChunk - relativeFeatureX;
        //            int featureDZ = zInChunk - relativeFeatureZ;
        //
        //            float dist = (int) Mth.sqrt(featureDX * featureDX + featureDZ * featureDZ);
        //            float hheight = (int) (Mth.cos(dist / hdiam * Mth.PI) * (hdiam / 3F));
        //        }
        //    }
        //}
        // done!
    }
    private void flattenTerrainForFeature(WorldGenRegion primer, TEFeature nearFeature, int x, int z, int dx, int dz) {
        
        float squishFactor = 0f;
        int mazeHeight = TEGenerationSettings.SEALEVEL + 2;
        final int FEATURE_BOUNDARY = (nearFeature.size * 2 + 1) * 8 - 8;
        
        if (dx <= -FEATURE_BOUNDARY) {
            squishFactor = (-dx - FEATURE_BOUNDARY) / 8.0f;
        } else if (dx >= FEATURE_BOUNDARY) {
            squishFactor = (dx - FEATURE_BOUNDARY) / 8.0f;
        }
        
        if (dz <= -FEATURE_BOUNDARY) {
            squishFactor = Math.max(squishFactor, (-dz - FEATURE_BOUNDARY) / 8.0f);
        } else if (dz >= FEATURE_BOUNDARY) {
            squishFactor = Math.max(squishFactor, (dz - FEATURE_BOUNDARY) / 8.0f);
        }
        
        if (squishFactor > 0f) {
            // blend the old terrain height to arena height
            for (int y = 0; y <= 127; y++) {
                Block currentTerrain = primer.getBlockState(withY(primer.getCenter().getWorldPosition().offset(x, 0, z), y)).getBlock();
                // we're still in ground
                if (currentTerrain != TEBlocks.EDGESTONE.get()) {
                    // we found the lowest chunk of earth
                    mazeHeight += ((y - mazeHeight) * squishFactor);
                    break;
                }
            }
        }
        
        // sets the ground level to the maze height, but dont move anything in rivers
        for (int y = 0; y < mazeHeight; y++) {
            BlockState b = primer.getBlockState(withY(primer.getCenter().getWorldPosition().offset(x, 0, z), y));
            if(!primer.getBiome(withY(primer.getCenter().getWorldPosition().offset(x, 0, z), y)).is(BiomeKeys.starryStream)) {
                if (b.isAir() || b.getMaterial().isLiquid()) {
                    primer.setBlock(withY(primer.getCenter().getWorldPosition().offset(x, 0, z), y), TEBlocks.STARFILLED_EDGESTONE.get().defaultBlockState(), 3);
                }
            }
        }
        
        for (int y = mazeHeight; y <= 127; y++) {
            BlockState b = primer.getBlockState(withY(primer.getCenter().getWorldPosition().offset(x, 0, z), y));
            if(!primer.getBiome(withY(primer.getCenter().getWorldPosition().offset(x, 0, z), y)).is(BiomeKeys.starryStream)) {
                if (!b.isAir() && !b.getMaterial().isLiquid()) {
                    primer.setBlock(withY(primer.getCenter().getWorldPosition().offset(x, 0, z), y), Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }
    }
    
    protected final BlockPos withY(BlockPos old, int y) {
        return new BlockPos(old.getX(), y, old.getZ());
    }
    
    static void forceHeightMapLevel(ChunkAccess chunk, Heightmap.Types type, BlockPos pos, int dY) {
        chunk.getOrCreateHeightmapUnprimed(type).setHeight(pos.getX() & 15, pos.getZ() & 15, dY + 1);
    }
    
    public TEFeature getFeatureCached(final ChunkPos chunk, final WorldGenLevel world) {
        return this.featureCache.computeIfAbsent(chunk, chunkPos -> TEFeature.generateFeature(chunkPos.x, chunkPos.z, world));
    }
}
