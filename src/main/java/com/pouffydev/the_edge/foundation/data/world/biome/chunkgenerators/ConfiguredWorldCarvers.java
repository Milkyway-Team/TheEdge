package com.pouffydev.the_edge.foundation.data.world.biome.chunkgenerators;

import com.pouffydev.the_edge.TheEdge;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.*;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TheEdge.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfiguredWorldCarvers {
    public static final TECavesCarver TECAVES = new TECavesCarver(CaveCarverConfiguration.CODEC);
    
    static {
        TECAVES.setRegistryName(TheEdge.ID, "edge_caves");
    }
    
    @SubscribeEvent
    public static void register(RegistryEvent.Register<WorldCarver<?>> evt) {
        evt.getRegistry().register(TECAVES);
    }
    
    
    //FIXME if I can go back to the old way of registering this that would be great, but this will do for now
    public static final Holder<ConfiguredWorldCarver<CaveCarverConfiguration>> TECAVES_CONFIGURED = register(TheEdge.asResource("edge_caves").toString(), TECAVES.configured(new CaveCarverConfiguration(0.1F, UniformHeight.of(VerticalAnchor.aboveBottom(5), VerticalAnchor.absolute(-5)), ConstantFloat.of(0.6F), VerticalAnchor.bottom(), CarverDebugSettings.of(false, Blocks.GLASS.defaultBlockState(), Blocks.BLUE_STAINED_GLASS.defaultBlockState(), Blocks.RED_STAINED_GLASS.defaultBlockState(), Blocks.RED_WOOL.defaultBlockState()), ConstantFloat.of(1.0F), ConstantFloat.of(1.0F), ConstantFloat.of(-0.7F))));
    
    
    private static <WC extends CarverConfiguration> Holder<ConfiguredWorldCarver<WC>> register(String name, ConfiguredWorldCarver<WC> carver) {
        return BuiltinRegistries.registerExact(BuiltinRegistries.CONFIGURED_CARVER, name, carver);
    }
}
