package com.pouffydev.the_edge.foundation.data.world.biome;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pouffydev.the_edge.foundation.data.world.biome.chunkgenerators.TerrainPoint;
import com.pouffydev.the_edge.foundation.data.world.biome.feature.TheEdgeFeatures;
import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.*;
import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.Layer;
import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.SmoothLayer;
import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.ZoomLayer;
import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.area.Area;
import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.area.AreaFactory;
import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.area.LazyArea;
import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.context.BigContext;
import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.context.LazyAreaContext;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.LongFunction;

public class TEBiomeProvider extends BiomeSource {
    public static final Codec<TEBiomeProvider> TE_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.LONG.fieldOf("seed").stable().orElseGet(() -> TheEdgeFeatures.seed).forGetter((obj) -> obj.seed),
            RegistryOps.retrieveRegistry(Registry.BIOME_REGISTRY).forGetter(provider -> provider.registry),
            RecordCodecBuilder.<Pair<TerrainPoint, Holder<Biome>>>create((pair) -> pair.group(
                    TerrainPoint.CODEC.fieldOf("parameters").forGetter(Pair::getFirst),
                    Biome.CODEC.fieldOf("biome").forGetter(Pair::getSecond)
            ).apply(pair, Pair::of)).listOf().fieldOf("biomes").forGetter((obj) -> obj.biomeList),
            Codec.FLOAT.fieldOf("base_offset").forGetter((obj) -> obj.baseOffset),
            Codec.FLOAT.fieldOf("base_factor").forGetter((obj) -> obj.baseFactor)
    ).apply(instance, instance.stable(TEBiomeProvider::new)));
    
    private static final List<ResourceKey<Biome>> biomes = ImmutableList.of( //TODO: Can we do this more efficiently?
            BiomeKeys.theEdge,
            BiomeKeys.glitchwoodForest,
            BiomeKeys.glacialPeaks,
            BiomeKeys.starryStream,
            BiomeKeys.starfallSwamp
    );
    private final Registry<Biome> registry;
    private final List<Pair<TerrainPoint, Holder<Biome>>> biomeList;
    private final Layer genBiomes;
    private final long seed;
    private final float baseOffset;
    private final float baseFactor;
    
    public TEBiomeProvider(long seed, Registry<Biome> registryIn, List<Pair<TerrainPoint, Holder<Biome>>> list, float offset, float factor) {
        super(biomes
                .stream()
                .map(registryIn::getHolder)
                .filter(Optional::isPresent)
                .map(Optional::get)
        );
        
        this.seed = seed;
        this.baseOffset = offset;
        this.baseFactor = factor;
        //getBiomesToSpawnIn().clear();
        //getBiomesToSpawnIn().add(TFBiomes.twilightForest.get());
        //getBiomesToSpawnIn().add(TFBiomes.denseTwilightForest.get());
        //getBiomesToSpawnIn().add(TFBiomes.clearing.get());
        //getBiomesToSpawnIn().add(TFBiomes.tfSwamp.get());
        //getBiomesToSpawnIn().add(TFBiomes.mushrooms.get());
        
        registry = registryIn;
        biomeList = list;
        genBiomes = makeLayers(seed, registryIn);
    }
    
    public static int getBiomeId(ResourceKey<Biome> biome, Registry<Biome> registry) {
        return registry.getId(registry.get(biome));
    }
    
    private static <T extends Area, C extends BigContext<T>> AreaFactory<T> makeLayers(LongFunction<C> seed, Registry<Biome> registry, long rawSeed) {
        
        AreaFactory<T> biomes = GenLayerTEBiomes.INSTANCE.setup(registry).run(seed.apply(1L));
        biomes = GenLayerTECompanionBiomes.INSTANCE.setup(registry).run(seed.apply(1000L), biomes);
        
        biomes = ZoomLayer.NORMAL.run(seed.apply(1000L), biomes);
        biomes = ZoomLayer.NORMAL.run(seed.apply(1001L), biomes);
        
        biomes = GenLayerTEBiomeStabilize.INSTANCE.run(seed.apply(700L), biomes);
        
        biomes = ZoomLayer.NORMAL.run(seed.apply(1002), biomes);
        biomes = ZoomLayer.NORMAL.run(seed.apply(1003), biomes);
        biomes = ZoomLayer.NORMAL.run(seed.apply(1004), biomes);
        biomes = ZoomLayer.NORMAL.run(seed.apply(1005), biomes);
        
        AreaFactory<T> riverLayer = GenLayerTEStream.INSTANCE.setup(registry).run(seed.apply(1L), biomes);
        riverLayer = SmoothLayer.INSTANCE.run(seed.apply(7000L), riverLayer);
        biomes = GenLayerTERiverMix.INSTANCE.setup(registry).run(seed.apply(100L), biomes, riverLayer);
        
        return biomes;
    }
    
    public static Layer makeLayers(long seed, Registry<Biome> registry) {
        AreaFactory<LazyArea> areaFactory = makeLayers((context) -> new LazyAreaContext(25, seed, context), registry, seed);
        return new Layer(areaFactory) {
            @Override
            public Holder<Biome> get(Registry<Biome> registry, int p_242936_2_, int p_242936_3_) {
                int i = this.area.get(p_242936_2_, p_242936_3_);
                Optional<Holder<Biome>> biome = registry.getHolder(i);
                if (biome.isEmpty())
                    throw new IllegalStateException("Unknown biome id emitted by layers: " + i);
                return biome.get();
            }
        };
    }
    
    @Override
    protected Codec<? extends BiomeSource> codec() {
        return TE_CODEC;
    }
    
    @Override
    public BiomeSource withSeed(long l) {
        return new TEBiomeProvider(l, registry, biomeList, baseOffset, baseFactor);
    }
    
    public float getBaseOffset() {
        return this.baseOffset;
    }
    
    public float getBaseFactor() {
        return this.baseFactor;
    }
    
    public float getBiomeDepth(int x, int y, int z, Climate.Sampler sampler) {
        Biome biome = this.getNoiseBiome(x, y, z, sampler).value();
        return this.getBiomeDepth(biome);
    }
    
    public float getBiomeDepth(Biome biome) {
        return this.getBiomeValue(biome, TerrainPoint::depth);
    }
    
    public float getBiomeScale(int x, int y, int z, Climate.Sampler sampler) {
        Biome biome = this.getNoiseBiome(x, y, z, sampler).value();
        return this.getBiomeScale(biome);
    }
    
    public float getBiomeScale(Biome biome) {
        return getBiomeValue(biome, TerrainPoint::scale);
    }
    
    private float getBiomeValue(Biome biome, Function<? super TerrainPoint, Float> function) {
        return this.biomeList.stream().filter(p -> p.getSecond().value().equals(biome)).map(Pair::getFirst).map(function).findFirst().orElse(0.0F);
    }
    
    @Override
    public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
        return genBiomes.get(registry, x, z);
    }
}
