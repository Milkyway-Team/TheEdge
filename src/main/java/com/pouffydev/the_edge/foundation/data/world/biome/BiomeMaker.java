package com.pouffydev.the_edge.foundation.data.world.biome;

import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;

import java.util.Map;

public final class BiomeMaker extends BiomeHelper {
    public static final Map<ResourceKey<Biome>, Biome> BIOMES = generateBiomes();
    
    private static Map<ResourceKey<Biome>, Biome> generateBiomes() {
        final ImmutableMap.Builder<ResourceKey<Biome>, Biome> biomes = ImmutableMap.builder();
        
        commonBiomes(biomes);
        rareBiomes(biomes);
        swampBiomes(biomes);
        snowRegionBiomes(biomes);
        
        return biomes.build();
    }
    
    private static void commonBiomes(ImmutableMap.Builder<ResourceKey<Biome>, Biome> biomes) {
        biomes.put(BiomeKeys.theEdge,
                biomeWithDefaults(soulParticles(edgeAmbientBuilder()), defaultMobSpawning(), theEdgeGen())
                        .build()
        );
        
        biomes.put(BiomeKeys.glitchwoodForest,
                biomeWithDefaults(soulParticles(edgeAmbientBuilder()).waterColor(0x005522), defaultMobSpawning(), glitchedForestGen())
                        .temperature(0.7F)
                        .downfall(0.8F)
                        ////.depth(0.1f)
                        ////.scale(0.2F)
                        .build()
        );
    }
    
    private static void rareBiomes(ImmutableMap.Builder<ResourceKey<Biome>, Biome> biomes) {
        //biomes.put(BiomeKeys.glacialPeaks,
        //        biomeWithDefaults(defaultAmbientBuilder().grassColorOverride(0xC45123).foliageColorOverride(0xFF8501).waterColor(0xFA9111).grassColorModifier(BiomeGrassColors.SPOOKY_FOREST),  spookSpawning(), spookyForestGen())
        //                .temperature(0.5F)
        //                .downfall(1)
        //                //.scale(0.05F)
        //                .build()
        //);
    }
    
    private static void swampBiomes(ImmutableMap.Builder<ResourceKey<Biome>, Biome> biomes) {
        biomes.put(BiomeKeys.starfallSwamp,
                biomeWithDefaults(edgeAmbientBuilder().grassColorOverride(0x5C694E).foliageColorOverride(0x496137).waterColor(0xE0FFAE).grassColorModifier(BiomeSpecialEffects.GrassColorModifier.SWAMP), swampSpawning(), swampGen())
                        .biomeCategory(Biome.BiomeCategory.SWAMP)
                        .temperature(0.8F)
                        .downfall(0.9F)
                        //.depth(-0.125f)
                        //.scale(0.15F)
                        .build()
        );
    }
    
    private static void snowRegionBiomes(ImmutableMap.Builder<ResourceKey<Biome>, Biome> biomes) {
        biomes.put(BiomeKeys.glacialPeaks,
                biomeWithDefaults(edgeAmbientBuilder(), glacierSpawning(), glacialPeaksGen())
                        .precipitation(Biome.Precipitation.SNOW)
                        .biomeCategory(Biome.BiomeCategory.ICY)
                        .temperature(0.09F)
                        .downfall(0.9F)
                        //.depth(0.05f)
                        //.scale(0.15F)
                        .build()
        );
    }
}
