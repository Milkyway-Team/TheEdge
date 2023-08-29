package com.pouffydev.the_edge.foundation.client;

import com.pouffydev.the_edge.content.equipment.spacesuit.SpaceBacktankArmorLayer;
import com.pouffydev.the_edge.content.equipment.spacesuit.SpaceBacktankFirstPersonRenderer;
import com.simibubi.create.content.equipment.armor.NetheriteBacktankFirstPersonRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.jozufozu.flywheel.backend.Backend.isGameActive;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class TEClient {
    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event) {
        if (!isGameActive())
            return;
        SpaceBacktankFirstPersonRenderer.clientTick();
    }
    
    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {
        @SubscribeEvent
        public static void addEntityRendererLayers(EntityRenderersEvent.AddLayers event) {
            EntityRenderDispatcher dispatcher = Minecraft.getInstance()
                    .getEntityRenderDispatcher();
            SpaceBacktankArmorLayer.registerOnAll(dispatcher);
        }
    }
}
