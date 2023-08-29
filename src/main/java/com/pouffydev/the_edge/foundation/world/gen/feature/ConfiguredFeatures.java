package com.pouffydev.the_edge.foundation.world.gen.feature;

import com.google.common.collect.ImmutableList;
import com.pouffydev.the_edge.TEBlocks;
import com.pouffydev.the_edge.foundation.TETags;
import com.pouffydev.the_edge.foundation.config.TEConfigs;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ConfiguredFeatures {
    public static final List<OreConfiguration.TargetBlockState> platinumOres = List.of(
            OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, TEBlocks.deepslatePlatinumOre.get().defaultBlockState()));
    public static final ImmutableList<OreConfiguration.TargetBlockState> glacioniteOres = ImmutableList.of(
            OreConfiguration.target(new TagMatchTest(TETags.AllBlockTags.edgestoneOreReplaceables.tag), TEBlocks.glacioniteOre.get().defaultBlockState()),
            OreConfiguration.target(new TagMatchTest(TETags.AllBlockTags.edgeslateOreReplaceables.tag), TEBlocks.edgeslateGlacioniteOre.get().defaultBlockState()));
    
    public static final ImmutableList<OreConfiguration.TargetBlockState> bismuthOres = ImmutableList.of(
            OreConfiguration.target(new TagMatchTest(TETags.AllBlockTags.edgeslateOreReplaceables.tag), TEBlocks.edgeslateBismuthOre.get().defaultBlockState()));
    
    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> platinumOre = FeatureUtils.register("platinum_ore",
            Feature.ORE, new OreConfiguration(platinumOres, TEConfigs.server().world.platinumOreVeinSize.get()));
    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> glacioniteOre = FeatureUtils.register("glacionite_ore",
            Feature.ORE, new OreConfiguration(glacioniteOres, TEConfigs.server().world.glacioniteOreVeinSize.get()));
    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> bismuthOre = FeatureUtils.register("bismuth_ore",
            Feature.ORE, new OreConfiguration(bismuthOres, TEConfigs.server().world.bismuthOreVeinSize.get()));
}
