package com.pouffydev.the_edge.foundation.data.world.biome.feature;

import com.pouffydev.the_edge.TEBlocks;
import com.pouffydev.the_edge.TEFluids;
import com.pouffydev.the_edge.TESoundEvents;
import com.pouffydev.the_edge.TheEdge;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.sounds.Music;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.NoiseProvider;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.List;

@SuppressWarnings("deprecation")
public final class TEConfiguredFeatures {
    /**
     * Generic features for most if not all biomes
     */
    public static final Holder<ConfiguredFeature<LakeFeature.Configuration, ?>> starfallLake = register("starfall_lake", Feature.LAKE, new LakeFeature.Configuration(BlockStateProvider.simple(Blocks.WATER), BlockStateProvider.simple(TEBlocks.EDGESTONE.get())));
    public static final Holder<ConfiguredFeature<OreConfiguration,?>> smallStarfilledEdgestone = register("small_starfilled_edgestone", Feature.ORE, new OreConfiguration(OreFeatures.NATURAL_STONE, TEBlocks.STARFILLED_EDGESTONE.get().defaultBlockState(), 16));
    
    /**
     * Nature stuff for biomes that aren't a wasteland
     */
    public static final RandomPatchConfiguration forestFoliage = (new RandomPatchConfiguration(64, 7, 7,
            PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
                    new NoiseProvider(2345L, new NormalNoise.NoiseParameters(0, 1.0D), 0.020833334F, List.of(
                            //TFBlocks.MAYAPPLE.defaultBlockState(),
                            TEBlocks.tallSpinecurl.get().defaultBlockState(),
                            TEBlocks.spinecurl.get().defaultBlockState(),
                            TEBlocks.levivine.get().defaultBlockState())
                    )), BlockPredicate.allOf(BlockPredicate.replaceable(), BlockPredicate.matchesBlock(TEBlocks.GLITCHED_NYLIUM.get(), new BlockPos(0, -1, 0))))));
    public static final RandomPatchConfiguration frostedFoliage = (new RandomPatchConfiguration(64, 7, 7,
            PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
                    new NoiseProvider(2345L, new NormalNoise.NoiseParameters(0, 1.0D), 0.020833334F, List.of(
                            TEBlocks.frostSpike.get().defaultBlockState())
                    )), BlockPredicate.allOf(BlockPredicate.replaceable(), BlockPredicate.matchesBlock(TEBlocks.frostedNylium.get(), new BlockPos(0, -1, 0))))));
    //music!
    public static final Music TheEdgeMusicType = new Music(TESoundEvents.musicBackground.getMainEvent(), 1200, 12000, true);
    public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> forestFoliagePlacer = register("forest_foliage_placer", Feature.RANDOM_PATCH, forestFoliage);
    public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> frostedFoliagePlacer = register("frosted_foliage_placer", Feature.RANDOM_PATCH, frostedFoliage);
    public static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<ConfiguredFeature<FC, ?>> register(String name, F feature, FC featureConfiguration) {
        return BuiltinRegistries.registerExact(BuiltinRegistries.CONFIGURED_FEATURE, TheEdge.asResource(name).toString(), new ConfiguredFeature<>(feature, featureConfiguration));
    }
}
