package com.pouffydev.the_edge.foundation.world.gen.feature;

import com.pouffydev.the_edge.foundation.ModUtils;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.List;
import java.util.Objects;

public class OreGeneration {
    
    public static void generateOres(final BiomeLoadingEvent event) {
        List<Holder<PlacedFeature>> base =
                event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES);
        
        base.add(PlacedFeatures.platinumOrePlaced);
        base.add(PlacedFeatures.bismuthOrePlaced);
        
    }
    public static void addFeatureToBiomes(BiomeLoadingEvent event) {
        Biome.BiomeCategory c = event.getCategory();
        if (Objects.equals(event.getName(), new ResourceLocation("the_edge", "glacial_peaks"))) {
            event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(PlacedFeatures.glacioniteOrePlaced);
        }
    }
}
