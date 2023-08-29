package com.pouffydev.the_edge.foundation.world.gen.feature;

import com.pouffydev.the_edge.foundation.ModUtils;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.List;

public class OreGeneration {
    
    public static void generateOres(final BiomeLoadingEvent event) {
        List<Holder<PlacedFeature>> base =
                event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES);
        
        base.add(PlacedFeatures.platinumOrePlaced);
        base.add(PlacedFeatures.bismuthOrePlaced);
        
    }
    public static void addFeatureToBiomes(BiomeLoadingEvent event) {
        Biome.BiomeCategory c = event.getCategory();
        if (c == Biome.BiomeCategory.MOUNTAIN) {
            event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(PlacedFeatures.glacioniteOrePlaced);
        }
    }
}
