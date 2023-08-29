package com.pouffydev.the_edge.foundation.data.world.biome.gen_layer;

import com.pouffydev.the_edge.foundation.data.world.biome.BiomeKeys;
import com.pouffydev.the_edge.foundation.data.world.biome.TEBiomeProvider;
import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.context.Context;
import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.traits.CastleTransformer;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;

public enum GenLayerTECompanionBiomes implements CastleTransformer {
    INSTANCE;
    
    GenLayerTECompanionBiomes() { }
    
    private Registry<Biome> registry;
    
    public GenLayerTECompanionBiomes setup(Registry<Biome> registry) {
        this.registry = registry;
        return this;
    }
    
    @Override
    public int apply(Context noise, int up, int left, int down, int right, int center) {
        int glacialPeaks        = TEBiomeProvider.getBiomeId(BiomeKeys.glacialPeaks, registry);
        int glitchwoodForest            = TEBiomeProvider.getBiomeId(BiomeKeys.glitchwoodForest, registry);
        int starfallSwamp          = TEBiomeProvider.getBiomeId(BiomeKeys.starfallSwamp, registry);
        int starryStream      = TEBiomeProvider.getBiomeId(BiomeKeys.starryStream, registry);
        int theEdge = TEBiomeProvider.getBiomeId(BiomeKeys.theEdge, registry);
        int frozenWastes       = TEBiomeProvider.getBiomeId(BiomeKeys.frozenWastes, registry);
        int overgrownLake  = TEBiomeProvider.getBiomeId(BiomeKeys.overgrownLake, registry);
        
        if (isKey(overgrownLake, center, right, left, up, down)) {
            return starryStream;
        } else if (isKey(starfallSwamp, center, right, left, up, down)) {
            return glitchwoodForest;
        } else if (isKey(glacialPeaks, center, right, left, up, down)) {
            return frozenWastes;
        } else if (isKey(theEdge, center, right, left, up, down)) {
            return glitchwoodForest;
        } else {
            return center;
        }
    }
    
    /**
     * Returns true if any of the surrounding biomes is the specified biome
     */
    boolean isKey(int biome, int center, int right, int left, int up, int down) {
        return center != biome && (right == biome || left == biome || up == biome || down == biome);
    }
}
