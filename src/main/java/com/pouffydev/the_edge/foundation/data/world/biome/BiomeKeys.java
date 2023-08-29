package com.pouffydev.the_edge.foundation.data.world.biome;

import com.pouffydev.the_edge.TheEdge;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("deprecation")
public class BiomeKeys {
    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, TheEdge.ID);
    public static final ResourceKey<Biome> glitchwoodForest = makeKey("glitchwood_forest");
    public static final ResourceKey<Biome> starfallSwamp = makeKey("starfall_swamp");
    public static final ResourceKey<Biome> starryStream = makeKey("starry_stream");
    public static final ResourceKey<Biome> glacialPeaks = makeKey("glacial_peaks");
    public static final ResourceKey<Biome> frozenWastes = makeKey("frozen_wastes");
    public static final ResourceKey<Biome> overgrownLake = makeKey("overgrown_lake");
    public static final ResourceKey<Biome> theEdge = makeKey("the_edge");
    
    
    private static ResourceKey<Biome> makeKey(String name) {
        // Apparently this resolves biome shuffling /shrug
        BIOMES.register(name, () -> new Biome.BiomeBuilder()
                .precipitation(Biome.Precipitation.NONE)
                .biomeCategory(Biome.BiomeCategory.NONE)
                //.depth(0)
                .downfall(0)
                //.scale(0)
                .temperature(0)
                .specialEffects(new BiomeSpecialEffects.Builder().fogColor(0).waterColor(0).waterFogColor(0).skyColor(0).build())
                .generationSettings(new BiomeGenerationSettings.Builder().build())
                .mobSpawnSettings(new MobSpawnSettings.Builder().build())
                .temperatureAdjustment(Biome.TemperatureModifier.NONE)
                .build());
        return ResourceKey.create(Registry.BIOME_REGISTRY, TheEdge.asResource(name));
    }
    public static final BiomeDictionary.Type EDGE = BiomeDictionary.Type.getType("EDGE");
    
    public static void addBiomeTypes() {
        BiomeDictionary.addTypes(theEdge, EDGE, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.SPARSE);
        BiomeDictionary.addTypes(glitchwoodForest, EDGE,  BiomeDictionary.Type.FOREST);
        BiomeDictionary.addTypes(starfallSwamp, EDGE, BiomeDictionary.Type.SWAMP, BiomeDictionary.Type.RARE);
        BiomeDictionary.addTypes(glacialPeaks, EDGE, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.RARE);
    }
    
}
