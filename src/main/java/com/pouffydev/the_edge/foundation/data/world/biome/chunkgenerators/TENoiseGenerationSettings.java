package com.pouffydev.the_edge.foundation.data.world.biome.chunkgenerators;

import com.pouffydev.the_edge.TEBlocks;
import com.pouffydev.the_edge.TEFluids;
import com.pouffydev.the_edge.TheEdge;
import com.pouffydev.the_edge.foundation.data.world.biome.chunkgenerators.surface_rules.TESurfaceRules;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.data.worldgen.TerrainProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CubicSpline;
import net.minecraft.world.level.biome.TerrainShaper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class TENoiseGenerationSettings {
    public static final DeferredRegister<NoiseGeneratorSettings> NOISE_GENERATORS = DeferredRegister.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, TheEdge.ID);
    
    public static final RegistryObject<NoiseGeneratorSettings> EDGE_NOISE_GEN = NOISE_GENERATORS.register("edge_noise_gen", TENoiseGenerationSettings::tfDefault);
    public static final RegistryObject<NoiseGeneratorSettings> SKYLIGHT_NOISE_GEN = NOISE_GENERATORS.register("skylight_noise_gen", TENoiseGenerationSettings::skylight);
    public static final RegistryObject<NoiseGeneratorSettings> FLOATING_ISLANDS = NOISE_GENERATORS.register("floating_islands", TENoiseGenerationSettings::floatingIslands);
    
    public static NoiseGeneratorSettings floatingIslands() {
        NoiseSettings tfNoise = NoiseSettings.create(
                0,
                256,
                new NoiseSamplingSettings(2.0D, 1.0D, 80.0D, 160.0D),
                new NoiseSlider(-23.4375D, 64, -46),
                new NoiseSlider(-0.234375D, 7, 1), 2, 1,
                new TerrainShaper(CubicSpline.constant(0.0F), CubicSpline.constant(0.0F), CubicSpline.constant(0.0F))
        );
        return new NoiseGeneratorSettings(
                tfNoise,
                TEBlocks.EDGESTONE.get().defaultBlockState(),
                TEFluids.STARFALL.get().defaultFluidState().createLegacyBlock(),
                new NoiseRouterWithOnlyNoises(
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero()
                ),
                TESurfaceRules.teSurface(),
                0,
                false,
                false,
                false,
                false
        );
    }
    public static NoiseGeneratorSettings tfDefault() {
        NoiseSettings tfNoise = NoiseSettings.create(
                -32, //TODO Deliberate over this. For now it'll be -32
                256,
                new NoiseSamplingSettings(0.9999999814507745D, 0.9999999814507745D, 80.0D, 160.0D),
                new NoiseSlider(-10, 3, 0),
                new NoiseSlider(15, 3, 0),
                1,
                2,
                new TerrainShaper(CubicSpline.constant(-0.45F), CubicSpline.constant(10.0F), CubicSpline.constant(0.0F))
        );
        
        return new NoiseGeneratorSettings(
                tfNoise,
                TEBlocks.EDGESTONE.get().defaultBlockState(),
                TEFluids.STARFALL.get().defaultFluidState().createLegacyBlock(),
                new NoiseRouterWithOnlyNoises(
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero()
                ),
                TESurfaceRules.teSurface(),
                0,
                false,
                false,
                false,
                false
        );
    }
    
    public static NoiseGeneratorSettings skylight() {
        NoiseSettings skylightNoise = NoiseSettings.create(
                -32, //min height
                256, // height
                new NoiseSamplingSettings(3.0D, 1.0D, 256.0D, 16.0D), // sampling
                new NoiseSlider(-3000, 92, -66), // top_slide
                new NoiseSlider(-30, 7, 1), // bottom_slide
                2, // size_horizontal
                1, // size_vertical
                new TerrainShaper(CubicSpline.constant(0.0F), CubicSpline.constant(0.0F), CubicSpline.constant(0.0F)) //terrain_shaper TODO
        );
        
        // Problem island at /tp 9389.60 90.00 11041.66
        return new NoiseGeneratorSettings(
                // https://misode.github.io/worldgen/noise-settings/
                // So far this looks great! We just need to raise the island levels to sea level. Otherwise is generates flat-flakey islands that really show the roots on dirt bottoms from trees
                skylightNoise,
                TEBlocks.EDGESTONE.get().defaultBlockState(),
                TEFluids.STARFALL.get().defaultFluidState().createLegacyBlock(),
                NoiseRouterData.noNewCaves(skylightNoise),
                TESurfaceRules.teSurface(),
                0,
                false,
                false,
                false,
                false
        );
    }
}
