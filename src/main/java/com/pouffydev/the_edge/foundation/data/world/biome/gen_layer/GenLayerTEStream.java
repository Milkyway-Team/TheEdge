package com.pouffydev.the_edge.foundation.data.world.biome.gen_layer;

import com.pouffydev.the_edge.foundation.data.world.biome.BiomeKeys;
import com.pouffydev.the_edge.foundation.data.world.biome.TEBiomeProvider;
import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.context.Context;
import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.traits.CastleTransformer;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;

public enum GenLayerTEStream implements CastleTransformer {
    INSTANCE;
    
    private Registry<Biome> registry;
    
    GenLayerTEStream() { }
    
    public GenLayerTEStream setup(Registry<Biome> registry) {
        this.registry = registry;
        return this;
    }
    
    @Override
    public int apply(Context iNoiseRandom, int up, int left, int down, int right, int mid) {
        if (shouldStream(mid, left) || shouldStream(mid, right) || shouldStream(mid, down) || shouldStream(mid, up)) {
            return TEBiomeProvider.getBiomeId(BiomeKeys.starryStream, registry);
        } else {
            return mid;
        }
    }
    
    
    boolean shouldStream(int biome1, int biome2) {
        if (biome1 == biome2) {
            return false;
        }
        
        if (biome1 == -biome2) {
            return false;
        }
        
        final int tfLake = TEBiomeProvider.getBiomeId(BiomeKeys.overgrownLake, registry);
        
        return !(testEitherBiomeOR(biome1, biome2, tfLake, tfLake)
                || testEitherBiomeOR(biome1, biome2, TEBiomeProvider.getBiomeId(BiomeKeys.theEdge, registry), TEBiomeProvider.getBiomeId(BiomeKeys.glitchwoodForest, registry))
                || testEitherBiomeAND(biome1, biome2, TEBiomeProvider.getBiomeId(BiomeKeys.frozenWastes, registry), TEBiomeProvider.getBiomeId(BiomeKeys.glacialPeaks, registry))
                || testEitherBiomeAND(biome1, biome2, TEBiomeProvider.getBiomeId(BiomeKeys.starfallSwamp, registry), tfLake));
    }
    
    private boolean testEitherBiomeAND(int test1, int test2, int predicate1, int predicate2) {
        return (test1 == predicate1 && test2 == predicate2) || (test2 == predicate1 && test1 == predicate2);
    }
    
    private boolean testEitherBiomeOR(int test1, int test2, int predicate1, int predicate2) {
        return (test1 == predicate1 || test2 == predicate2) || (test2 == predicate1 || test1 == predicate2);
    }
}
