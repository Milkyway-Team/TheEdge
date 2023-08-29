package com.pouffydev.the_edge.foundation.world.gen.feature;

import com.pouffydev.the_edge.foundation.config.TEConfigs;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class PlacedFeatures {
    public static final Holder<PlacedFeature> platinumOrePlaced = PlacementUtils.register("platinum_ore_placed",
            ConfiguredFeatures.platinumOre, OrePlacement.rareOrePlacement(TEConfigs.server().world.platinumOreVeinsPerChunk.get(), // VeinsPerChunk
                    HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(30))));
    
    public static final Holder<PlacedFeature> glacioniteOrePlaced = PlacementUtils.register("glacionite_ore_placed",
            ConfiguredFeatures.glacioniteOre, OrePlacement.commonOrePlacement(TEConfigs.server().world.glacioniteOreVeinsPerChunk.get(), // VeinsPerChunk
                    HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(342))));
    
    public static final Holder<PlacedFeature> bismuthOrePlaced = PlacementUtils.register("bismuth_ore_placed",
            ConfiguredFeatures.bismuthOre, OrePlacement.rareOrePlacement(TEConfigs.server().world.bismuthOreVeinsPerChunk.get(), // VeinsPerChunk
                    HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(10))));
}
