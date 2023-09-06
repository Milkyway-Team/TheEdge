package com.pouffydev.the_edge.foundation.oxygen;

import com.pouffydev.the_edge.TheEdge;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class EffectUtils {
    /**
     * Checks if a biome is freezing, regardless of position.
     */
    public static boolean biomeIsFreezing(LivingEntity entity) {
        return entity.level.getBiome(entity.blockPosition()).is(new ResourceLocation(TheEdge.ID,"freezing_biomes"));
    }
}
