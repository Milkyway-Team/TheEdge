package com.pouffydev.the_edge.foundation.world;

import com.pouffydev.the_edge.TheEdge;
import com.pouffydev.the_edge.foundation.world.gen.feature.OreGeneration;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TheEdge.ID)
public class TEWorldEvents {
    @SubscribeEvent
    public static void biomeLoadingEvent(final BiomeLoadingEvent event) {
        OreGeneration.generateOres(event);
        OreGeneration.addFeatureToBiomes(event);
    }
}
