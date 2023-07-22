package com.pouffydev.the_edge;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TheEdge.ID)
public class TheEdge
{
    // Directly reference a slf4j logger
    public static final String ID = "the_edge";
    public static final CreateRegistrate registrate = CreateRegistrate.create(TheEdge.ID);
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // TODO: Add new icon for your mod's item group
    public static final CreativeModeTab itemGroup = new CreativeModeTab(ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Items.AIR);
        }
    };
    public TheEdge()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        registrate.registerEventListeners(eventBus);
        eventBus.addListener(EventPriority.LOWEST, TheEdge::gatherData);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void gatherData(@NotNull GatherDataEvent event) {
        //TagGen.datagen();
        DataGenerator gen = event.getGenerator();
        if (event.includeClient()) {
            //gen.addProvider(new LangMerger(gen, ID, "Create: Milkyway", AllLangPartials.values()));
        }
        if (event.includeServer()) {
            //gen.addProvider(new MWAdvancements(gen));
            //MWProcessingRecipeGen.registerAll(gen);
        }
    }
    @Contract("_ -> new")
    public static @NotNull ResourceLocation asResource(String path) {
        return new ResourceLocation(ID, path);
    }

    private void setup(final FMLCommonSetupEvent event) {}

    public static @NotNull CreateRegistrate registrate() {
        return registrate;
    }
}
