package com.pouffydev.the_edge.foundation.data.world.biome.gen_layer;

import com.google.common.collect.ImmutableList;
import com.pouffydev.the_edge.foundation.data.world.biome.BiomeKeys;
import com.pouffydev.the_edge.foundation.data.world.biome.TEBiomeProvider;
import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.context.Context;
import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.traits.AreaTransformer0;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import java.util.List;

public enum GenLayerTEBiomes implements AreaTransformer0 {
    INSTANCE;
    private static final int RARE_BIOME_CHANCE = 15;
    
    protected static final List<ResourceKey<Biome>> commonBiomes = ImmutableList.of(
            BiomeKeys.glitchwoodForest,
            BiomeKeys.theEdge,
            BiomeKeys.starryStream
    );
    protected static final List<ResourceKey<Biome>> rareBiomes = ImmutableList.of(
            BiomeKeys.glacialPeaks,
            BiomeKeys.starfallSwamp
    );
    private Registry<Biome> registry;
    
    public GenLayerTEBiomes setup(Registry<Biome> registry) {
        this.registry = registry;
        return this;
    }
    
    GenLayerTEBiomes() {
    
    }
    
    @Override
    public int applyPixel(Context iNoiseRandom, int x, int y) {
        //return 0; //getRandomBiome(iNoiseRandom, commonBiomes));
        
        if (iNoiseRandom.nextRandom(RARE_BIOME_CHANCE) == 0) {
            // make specialBiomes biome
            return getRandomBiome(iNoiseRandom, rareBiomes);
        } else {
            // make common biome
            return getRandomBiome(iNoiseRandom, commonBiomes);
        }
    }
    
    private int getRandomBiome(Context random, List<ResourceKey<Biome>> biomes) {
        return TEBiomeProvider.getBiomeId(biomes.get(random.nextRandom(biomes.size())), registry);
    }
}
