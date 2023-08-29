package com.pouffydev.the_edge.foundation.data.world.biome.feature;

import com.google.common.collect.ImmutableList;
import com.pouffydev.the_edge.TheEdge;
import com.pouffydev.the_edge.foundation.data.world.biome.chunkgenerators.OutOfStructureFilter;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.heightproviders.HeightProviderType;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class TEPlacedFeatures {
    public static final Holder<PlacedFeature> starfallLakePlaced = register("starfall_lake", TEConfiguredFeatures.starfallLake, teFeatureCheckArea(OutOfStructureFilter.checkBoth(), 10).build());
    public static final Holder<PlacedFeature> starfallLakeUndergroundPlaced = register("starfall_lake_underground", TEConfiguredFeatures.starfallLake, teFeatureCheckArea(OutOfStructureFilter.checkBoth(), 10, HeightRangePlacement.of((HeightProvider) HeightProviderType.BIASED_TO_BOTTOM)).build());
    public static final Holder<PlacedFeature> smallStarfilledEdgestonePlaced = register("small_starfilled_edgestone", TEConfiguredFeatures.smallStarfilledEdgestone, ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(60), InSquarePlacement.spread(), CountPlacement.of(5)).build());
    
    
    public static final Holder<PlacedFeature> forestFoliagePlacer = register("forest_foliage_placer", TEConfiguredFeatures.forestFoliagePlacer, ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, CountPlacement.of(20), InSquarePlacement.spread(), RarityFilter.onAverageOnceEvery(3)).build());
    
    public static final Holder<PlacedFeature> frostedFoliagePlacer = register("frosted_foliage_placer", TEConfiguredFeatures.frostedFoliagePlacer, ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, CountPlacement.of(20), InSquarePlacement.spread(), RarityFilter.onAverageOnceEvery(3)).build());
    //public static final Holder<ConfiguredFeature<RandomFeatureConfiguration, ?>> defaultFallenLogs = TEConfiguredFeatures.register("default_fallen_logs", Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(ImmutableList.of(new WeightedPlacedFeature(TEPlacedFeatures.fallenGlitchwoodPlaced, 0.2F))));
    //public static final Holder<PlacedFeature> defaultFallenLogsPlaced = register("default_fallen_logs", defaultFallenLogs, fallenLog(OutOfStructureFilter.checkSurface(), 40).build());
    private static ImmutableList.Builder<PlacementModifier> teFeatureCheckArea(OutOfStructureFilter filter, int rarity) {
        return ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(rarity), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, filter, BiomeFilter.biome());
    }
    
    private static ImmutableList.Builder<PlacementModifier> teFeatureCheckArea(OutOfStructureFilter filter, int rarity, PlacementModifier... extra) {
        return ImmutableList.<PlacementModifier>builder().add(extra).add(filter, RarityFilter.onAverageOnceEvery(rarity), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
    }
    private static ImmutableList.Builder<PlacementModifier> fallenLog(OutOfStructureFilter filter, int rarity) {
        return ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(rarity), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, filter);
    }
    public static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<PlacedFeature> register(String name, Holder<? extends ConfiguredFeature<?, ?>> feature, List<PlacementModifier> placements) {
        return BuiltinRegistries.registerExact(BuiltinRegistries.PLACED_FEATURE, TheEdge.asResource(name).toString(), new PlacedFeature(Holder.hackyErase(feature), List.copyOf(placements)));
    }
}
