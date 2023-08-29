package com.pouffydev.the_edge.foundation.data.world.biome.util;

import com.google.common.collect.ImmutableMap;
import com.pouffydev.the_edge.foundation.data.world.biome.chunkgenerators.TEGenerationSettings;
import com.pouffydev.the_edge.foundation.util.IntPair;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Arbiting class that decides what feature goes where in the world, in terms of the major features in the world
 */
public class TEFeature {
    public static final TEFeature NOTHING = new TEFeature( 0, "no_feature", false){ { this.enableDecorations().disableStructure(); } };
    
    
    
    
    private static final Map<ResourceLocation, TEFeature> BIOME_FEATURES = new ImmutableMap.Builder<ResourceLocation, TEFeature>()
            //.put(BiomeKeys.DARK_FOREST.location(), KNIGHT_STRONGHOLD)
            .build();
    
    public final int size;
    public final String name;
    public final boolean centerBounds;
    public boolean surfaceDecorationsAllowed = false;
    public boolean undergroundDecoAllowed = true;
    public boolean requiresTerraforming = false; // TODO Terraforming Type? Envelopment vs Flattening maybe?
    protected boolean adjustToTerrainHeight = false;
    
    private static int maxPossibleSize;
    
    private final List<List<MobSpawnSettings.SpawnerData>> spawnableMonsterLists = new ArrayList<>();
    private final List<MobSpawnSettings.SpawnerData> ambientCreatureList = new ArrayList<>();
    private final List<MobSpawnSettings.SpawnerData> waterCreatureList = new ArrayList<>();
    
    private long lastSpawnedHintMonsterTime;
    
    TEFeature(int size, String name, boolean featureGenerator, ResourceLocation... requiredAdvancements) {
        this(size, name, featureGenerator, false, requiredAdvancements);
    }
    
    TEFeature(int size, String name, boolean featureGenerator, boolean centerBounds, ResourceLocation... requiredAdvancements) {
        this.size = size;
        this.name = name;
        
        this.centerBounds = centerBounds;
        
        maxPossibleSize = Math.max(this.size, maxPossibleSize);
    }
    static void init() {}
    
    public static int getMaxSize() {
        return maxPossibleSize;
    }
    
    public boolean shouldAdjustToTerrain() {
        return this.adjustToTerrainHeight;
    }
    
    //	@Nullable
//	public MapGenTFMajorFeature createFeatureGenerator() {
//		return this.shouldHaveFeatureGenerator ? new MapGenTFMajorFeature(this) : null;
//	}
    
    public static TEFeature getFeatureAt(int regionX, int regionZ, WorldGenLevel world) {
        return generateFeature(regionX >> 4, regionZ >> 4, world);
    }
    
    public static boolean isInFeatureChunk(int regionX, int regionZ) {
        int chunkX = regionX >> 4;
        int chunkZ = regionZ >> 4;
        BlockPos cc = getNearestCenterXYZ(chunkX, chunkZ);
        
        return chunkX == (cc.getX() >> 4) && chunkZ == (cc.getZ() >> 4);
    }
    
    /**
     * Turns on biome-specific decorations like grass and trees near this feature.
     */
    public TEFeature enableDecorations() {
        this.surfaceDecorationsAllowed = true;
        return this;
    }
    
    /**
     * Tell the chunkgenerator that we don't have an associated structure.
     */
    public TEFeature disableStructure() {
        this.enableDecorations();
        return this;
    }
    
    /**
     * Tell the chunkgenerator that we want the terrain changed nearby.
     */
    public TEFeature enableTerrainAlterations() {
        this.requiresTerraforming = true;
        return this;
    }
    
    public TEFeature disableProtectionAura() {
        return this;
    }
    
    /**
     * Add a monster to spawn list 0
     */
    public TEFeature addMonster(EntityType<? extends LivingEntity> monsterClass, int weight, int minGroup, int maxGroup) {
        this.addMonster(0, monsterClass, weight, minGroup, maxGroup);
        return this;
    }
    
    /**
     * Add a monster to a specific spawn list
     */
    public TEFeature addMonster(int listIndex, EntityType<? extends LivingEntity> monsterClass, int weight, int minGroup, int maxGroup) {
        List<MobSpawnSettings.SpawnerData> monsterList;
        if (this.spawnableMonsterLists.size() > listIndex) {
            monsterList = this.spawnableMonsterLists.get(listIndex);
        } else {
            monsterList = new ArrayList<>();
            this.spawnableMonsterLists.add(listIndex, monsterList);
        }
        
        monsterList.add(new MobSpawnSettings.SpawnerData(monsterClass, weight, minGroup, maxGroup));
        return this;
    }
    
    /**
     * Add a water creature
     */
    public TEFeature addWaterCreature(EntityType<? extends LivingEntity> monsterClass, int weight, int minGroup, int maxGroup) {
        this.waterCreatureList.add(new MobSpawnSettings.SpawnerData(monsterClass, weight, minGroup, maxGroup));
        return this;
    }
    
    /**
     * @return The type of feature directly at the specified Chunk coordinates
     */
    public static TEFeature getFeatureDirectlyAt(int chunkX, int chunkZ, WorldGenLevel world) {
        if (isInFeatureChunk(chunkX << 4, chunkZ << 4)) {
            return getFeatureAt(chunkX << 4, chunkZ << 4, world);
        }
        return NOTHING;
    }
    
    /**
     * What feature would go in this chunk.  Called when we know there is a feature, but there is no cache data,
     * either generating this chunk for the first time, or using the magic map to forecast beyond the edge of the world.
     */
    public static TEFeature generateFeature(int chunkX, int chunkZ, WorldGenLevel world) {
        // set the chunkX and chunkZ to the center of the biome
        chunkX = Math.round(chunkX / 16F) * 16;
        chunkZ = Math.round(chunkZ / 16F) * 16;
        
        // what biome is at the center of the chunk?
        Biome biomeAt = world.getBiome(new BlockPos((chunkX << 4) + 8, 0, (chunkZ << 4) + 8)).value();
        return generateFeature(chunkX, chunkZ, biomeAt, world.getSeed());
    }
    
    public static TEFeature generateFeature(int chunkX, int chunkZ, Biome biome, long seed) {
        // Remove block comment start-marker to enable debug
		/*if (true) {
			return LICH_TOWER;
		}*/
        
        // set the chunkX and chunkZ to the center of the biome in case they arent already
        chunkX = Math.round(chunkX / 16F) * 16;
        chunkZ = Math.round(chunkZ / 16F) * 16;
        
        // does the biome have a feature?
        TEFeature biomeFeature = BIOME_FEATURES.get(biome.getRegistryName());
        
        if(biomeFeature != null)
            return biomeFeature;
        
        int regionOffsetX = Math.abs((chunkX + 64 >> 4) % 8);
        int regionOffsetZ = Math.abs((chunkZ + 64 >> 4) % 8);
        
        return biomeFeature;
    }
    
    /**
     * Returns the feature nearest to the specified chunk coordinates.
     */
    public static TEFeature getNearestFeature(int cx, int cz, WorldGenLevel world) {
        return getNearestFeature(cx, cz, world, null);
    }
    
    /**
     * Returns the feature nearest to the specified chunk coordinates.
     *
     * If a non-null {@code center} is provided and a valid feature is found,
     * it will be set to relative block coordinates indicating the center of
     * that feature relative to the current chunk block coordinate system.
     */
    public static TEFeature getNearestFeature(int cx, int cz, WorldGenLevel world, @Nullable IntPair center) {
        
        int maxSize = getMaxSize();
        int diam = maxSize * 2 + 1;
        TEFeature[] features = new TEFeature[diam * diam];
        
        for (int rad = 1; rad <= maxSize; rad++) {
            for (int x = -rad; x <= rad; x++) {
                for (int z = -rad; z <= rad; z++) {
                    
                    int idx = (x + maxSize) * diam + (z + maxSize);
                    TEFeature directlyAt = features[idx];
                    if (directlyAt == null) {
                        features[idx] = directlyAt = getFeatureDirectlyAt(x + cx, z + cz, world);
                    }
                    
                    if (directlyAt.size == rad) {
                        if (center != null) {
                            center.x = (x << 4) + 8;
                            center.z = (z << 4) + 8;
                        }
                        return directlyAt;
                    }
                }
            }
        }
        
        return NOTHING;
    }
    
    // [Vanilla Copy] from MapGenStructure#findNearestStructurePosBySpacing; changed 2nd param to be TFFeature instead of MapGenStructure
    //TODO: Second parameter doesn't exist in Structure.findNearest
    @Nullable
    public static BlockPos findNearestFeaturePosBySpacing(WorldGenLevel worldIn, TEFeature feature, BlockPos blockPos, int p_191069_3_, int p_191069_4_, int p_191069_5_, boolean p_191069_6_, int p_191069_7_, boolean findUnexplored) {
        int i = blockPos.getX() >> 4;
        int j = blockPos.getZ() >> 4;
        int k = 0;
        
        for (Random random = new Random(); k <= p_191069_7_; ++k) {
            for (int l = -k; l <= k; ++l) {
                boolean flag = l == -k || l == k;
                
                for (int i1 = -k; i1 <= k; ++i1) {
                    boolean flag1 = i1 == -k || i1 == k;
                    
                    if (flag || flag1) {
                        int j1 = i + p_191069_3_ * l;
                        int k1 = j + p_191069_3_ * i1;
                        
                        if (j1 < 0) {
                            j1 -= p_191069_3_ - 1;
                        }
                        
                        if (k1 < 0) {
                            k1 -= p_191069_3_ - 1;
                        }
                        
                        int l1 = j1 / p_191069_3_;
                        int i2 = k1 / p_191069_3_;
//						Random random1 = worldIn.setRandomSeed(l1, i2, p_191069_5_);
                        Random random1 = new Random();
                        l1 = l1 * p_191069_3_;
                        i2 = i2 * p_191069_3_;
                        
                        if (p_191069_6_) {
                            l1 = l1 + (random1.nextInt(p_191069_3_ - p_191069_4_) + random1.nextInt(p_191069_3_ - p_191069_4_)) / 2;
                            i2 = i2 + (random1.nextInt(p_191069_3_ - p_191069_4_) + random1.nextInt(p_191069_3_ - p_191069_4_)) / 2;
                        } else {
                            l1 = l1 + random1.nextInt(p_191069_3_ - p_191069_4_);
                            i2 = i2 + random1.nextInt(p_191069_3_ - p_191069_4_);
                        }
                        
                        //MapGenBase.setupChunkSeed(worldIn.getSeed(), random, l1, i2);
                        random.nextInt();
                        
                        // Check changed for TFFeature
                        if (getFeatureAt(l1 << 4, i2 << 4, worldIn.getLevel()) == feature) {
                            if (!findUnexplored || !worldIn.hasChunk(l1, i2)) {
                                return new BlockPos((l1 << 4) + 8, 64, (i2 << 4) + 8);
                            }
                        } else if (k == 0) {
                            break;
                        }
                    }
                }
                
                if (k == 0) {
                    break;
                }
            }
        }
        
        return null;
    }
    
    /**
     * @return The feature in the chunk "region"
     */
    public static TEFeature getFeatureForRegion(int chunkX, int chunkZ, WorldGenLevel world) {
        //just round to the nearest multiple of 16 chunks?
        int featureX = Math.round(chunkX / 16F) * 16;
        int featureZ = Math.round(chunkZ / 16F) * 16;
        
        return generateFeature(featureX, featureZ, world);
    }
    
    /**
     * @return The feature in the chunk "region"
     */
    public static TEFeature getFeatureForRegionPos(int posX, int posZ, WorldGenLevel world) {
        return getFeatureForRegion(posX >> 4, posZ >> 4, world);
    }
    
    /**
     * Given some coordinates, return the center of the nearest feature.
     * <p>
     * At the moment, with how features are distributed, just get the closest multiple of 256 and add +8 in both directions.
     * <p>
     * Maybe in the future we'll have to actually search for a feature chunk nearby, but for now this will work.
     */
    public static BlockPos getNearestCenterXYZ(int chunkX, int chunkZ) {
        // generate random number for the whole biome area
        int regionX = (chunkX + 8) >> 4;
        int regionZ = (chunkZ + 8) >> 4;
        
        long seed = regionX * 3129871 ^ regionZ * 116129781L;
        seed = seed * seed * 42317861L + seed * 7L;
        
        int num0 = (int) (seed >> 12 & 3L);
        int num1 = (int) (seed >> 15 & 3L);
        int num2 = (int) (seed >> 18 & 3L);
        int num3 = (int) (seed >> 21 & 3L);
        
        // slightly randomize center of biome (+/- 3)
        int centerX = 8 + num0 - num1;
        int centerZ = 8 + num2 - num3;
        
        // centers are offset strangely depending on +/-
        int ccz;
        if (regionZ >= 0) {
            ccz = (regionZ * 16 + centerZ - 8) * 16 + 8;
        } else {
            ccz = (regionZ * 16 + (16 - centerZ) - 8) * 16 + 9;
        }
        
        int ccx;
        if (regionX >= 0) {
            ccx = (regionX * 16 + centerX - 8) * 16 + 8;
        } else {
            ccx = (regionX * 16 + (16 - centerX) - 8) * 16 + 9;
        }
        
        return new BlockPos(ccx, TEGenerationSettings.SEALEVEL, ccz);//  Math.abs(chunkX % 16) == centerX && Math.abs(chunkZ % 16) == centerZ; FIXME (set sea level hard)
    }
    
    public List<MobSpawnSettings.SpawnerData> getCombinedMonsterSpawnableList() {
        List<MobSpawnSettings.SpawnerData> list = new ArrayList<>();
        spawnableMonsterLists.forEach(l -> {
            if(l != null)
                list.addAll(l);
        });
        return list;
    }
    
    public List<MobSpawnSettings.SpawnerData> getCombinedCreatureSpawnableList() {
        List<MobSpawnSettings.SpawnerData> list = new ArrayList<>();
        list.addAll(ambientCreatureList);
        list.addAll(waterCreatureList);
        return list;
    }
    
    /**
     * Returns a list of hostile monsters.  Are we ever going to need passive or water creatures?
     */
    public List<MobSpawnSettings.SpawnerData> getSpawnableList(MobCategory creatureType) {
        return switch (creatureType) {
            case MONSTER -> this.getSpawnableMonsterList(0);
            case AMBIENT -> this.ambientCreatureList;
            case WATER_CREATURE -> this.waterCreatureList;
            default -> new ArrayList<>();
        };
    }
    
    /**
     * Returns a list of hostile monsters in the specified indexed category
     */
    public List<MobSpawnSettings.SpawnerData> getSpawnableMonsterList(int index) {
        if (index >= 0 && index < this.spawnableMonsterLists.size()) {
            return this.spawnableMonsterLists.get(index);
        }
        return new ArrayList<>();
    }
    public static boolean isTheseFeatures(TEFeature feature, TEFeature... predicates) {
        for (TEFeature predicate : predicates)
            if (feature == predicate)
                return true;
        return false;
    }
    
}
