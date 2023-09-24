package com.pouffydev.the_edge.foundation.client;

import com.pouffydev.the_edge.TEBlocks;
import com.pouffydev.the_edge.content.equipment.spacesuit.SpaceBacktankArmorLayer;
import com.pouffydev.the_edge.content.equipment.spacesuit.SpaceBacktankFirstPersonRenderer;
import com.pouffydev.the_edge.foundation.TETags;
import com.simibubi.create.content.equipment.armor.NetheriteBacktankFirstPersonRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.jozufozu.flywheel.backend.Backend.isGameActive;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class TEClient {
    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event) {
        if (!isGameActive())
            return;
        SpaceBacktankFirstPersonRenderer.clientTick();
    }
    public static void onCtorClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(TEClient::clientInit);
    }
    public static void clientInit(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(TEBlocks.tallSpinecurl.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(TEBlocks.spinecurl.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(TEBlocks.levivine.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(TEBlocks.frostSpike.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(TEBlocks.chompweed.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(TEBlocks.groundbreakerDrill.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(TEBlocks.attuner.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(TEBlocks.heartDoor.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(TEBlocks.lockedHeartDoor.get(), RenderType.cutout());
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
