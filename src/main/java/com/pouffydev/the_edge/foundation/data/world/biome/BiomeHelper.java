package com.pouffydev.the_edge.foundation.data.world.biome;

import com.pouffydev.the_edge.foundation.data.world.biome.chunkgenerators.ConfiguredWorldCarvers;
import com.pouffydev.the_edge.foundation.data.world.biome.feature.TEConfiguredFeatures;
import com.pouffydev.the_edge.foundation.data.world.biome.feature.TEPlacedFeatures;
import com.pouffydev.the_edge.foundation.world.gen.feature.PlacedFeatures;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;

public abstract class BiomeHelper {
    public static BiomeGenerationSettings.Builder theEdgeGen() {
        BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder();
        addForestVegetation(biome);
        commonFeatures(biome);
        
        return biome;
    }
    public static BiomeGenerationSettings.Builder swampGen() {
        BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder();
        addForestVegetation(biome);
        commonFeatures(biome);
        return biome;
    }
    public static MobSpawnSettings.Builder swampSpawning() {
        MobSpawnSettings.Builder spawnInfo = new MobSpawnSettings.Builder();
        
        spawnInfo.creatureGenerationProbability(0.2f);
        
        return spawnInfo;
    }
    public static BiomeGenerationSettings.Builder glitchedForestGen() {
        BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder();
        addForestVegetation(biome);
        commonFeatures(biome);
        //biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_VANILLA_TF_TREES);
        return biome;
    }
    public static BiomeGenerationSettings.Builder glacialPeaksGen() {
        BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder();
        
        //biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_SNOWY_FOREST_TREES);
        //biome.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, TFPlacedFeatures.PLACED_SNOW_UNDER_TREES);
        
        biome.addFeature(GenerationStep.Decoration.LAKES, TEPlacedFeatures.starfallLakePlaced);
        biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TEPlacedFeatures.frostedFoliagePlacer);
        
        BiomeDefaultFeatures.addSurfaceFreezing(biome);
        
        addCaves(biome);
        addGlacierOres(biome);
        
        return biome;
    }
    public static BiomeGenerationSettings.Builder glacierGen() {
        BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder();
        addCaves(biome);
        addGlacierOres(biome);
        return biome;
    }
    
    public static void commonFeatures(BiomeGenerationSettings.Builder biome) {
        biome.addFeature(GenerationStep.Decoration.LAKES, TEPlacedFeatures.starfallLakePlaced);
        biome.addFeature(GenerationStep.Decoration.LAKES, TEPlacedFeatures.starfallLakeUndergroundPlaced);
        biome.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, TEPlacedFeatures.smallStarfilledEdgestonePlaced);
    }
    public static void addForestVegetation(BiomeGenerationSettings.Builder biome) {
        biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TEPlacedFeatures.forestFoliagePlacer);
    }
    public static void addCaves(BiomeGenerationSettings.Builder biome) {
        biome.addCarver(GenerationStep.Carving.AIR, ConfiguredWorldCarvers.TECAVES_CONFIGURED);
        //biome.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, TEPlacedFeatures.oxyRootsPlaced);
        addDefaultEdgeOres(biome);
    }
    public static void addDefaultEdgeOres(BiomeGenerationSettings.Builder biome) {
        biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacedFeatures.bismuthOrePlaced);
    }
    public static void addGlacierOres(BiomeGenerationSettings.Builder biome) {
        biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacedFeatures.glacioniteOrePlaced);
    }
    public static void addSmallStoneClusters(BiomeGenerationSettings.Builder biome) {
        biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, TEPlacedFeatures.smallStarfilledEdgestonePlaced);
    }
    //Biome Effects
    public static BiomeSpecialEffects.Builder soulParticles(BiomeSpecialEffects.Builder builder) {
        builder.ambientParticle(new AmbientParticleSettings(ParticleTypes.SOUL, 0.00025f));
        return builder;
    }
    // Defaults
    public static BiomeSpecialEffects.Builder edgeAmbientBuilder() {
        return new BiomeSpecialEffects.Builder()
                .fogColor(0xC0FFD8)
                .waterColor(0x3F76E4)
                .waterFogColor(0x050533)
                .skyColor(0x20224A)
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS) // We should probably change it
                .backgroundMusic(TEConfiguredFeatures.TheEdgeMusicType);
        
    }
    public static BiomeGenerationSettings.Builder defaultGenSettingBuilder() {
        BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder();
		addSmallStoneClusters(biome);
        addCaves(biome);
        return biome;
    }
    public static Biome.BiomeBuilder biomeWithDefaults(BiomeSpecialEffects.Builder biomeAmbience, MobSpawnSettings.Builder mobSpawnInfo, BiomeGenerationSettings.Builder biomeGenerationSettings) {
        return new Biome.BiomeBuilder()
                .precipitation(Biome.Precipitation.RAIN)
                .biomeCategory(Biome.BiomeCategory.NONE)
                //.depth(0.025f)
                //.scale(0.05f)
                .temperature(0.5F)
                .downfall(0.5F)
                .specialEffects(biomeAmbience.build())
                .mobSpawnSettings(mobSpawnInfo.build())
                .generationSettings(biomeGenerationSettings.build())
                .temperatureAdjustment(Biome.TemperatureModifier.NONE);
    }
    public static MobSpawnSettings.Builder defaultMobSpawning() {
        MobSpawnSettings.Builder spawnInfo = new MobSpawnSettings.Builder();
        
        spawnInfo.creatureGenerationProbability(0.1f);
        //spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.DEER.get(), 15, 4, 5));
        //spawnInfo.addSpawn(MobCategory. MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 10, 4, 4));
        
        return spawnInfo;
    }
    public static MobSpawnSettings.Builder glacierSpawning() {
        MobSpawnSettings.Builder spawnInfo = new MobSpawnSettings.Builder();
        
        spawnInfo.creatureGenerationProbability(0.1f);
        //spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.WINTER_WOLF.get(), 5, 1, 2));
        //spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.YETI.get(), 5, 1, 1));
        
        return spawnInfo;
    }
}
