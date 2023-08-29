package com.pouffydev.the_edge;

import com.mojang.logging.LogUtils;
import com.pouffydev.the_edge.foundation.ModItemTab;
import com.pouffydev.the_edge.foundation.TETags;
import com.pouffydev.the_edge.foundation.config.TEConfigs;
import com.pouffydev.the_edge.foundation.data.world.biome.BiomeKeys;
import com.pouffydev.the_edge.foundation.data.world.biome.TEBiomeProvider;
import com.pouffydev.the_edge.foundation.data.world.biome.chunkgenerators.TENoiseGenerationSettings;
import com.pouffydev.the_edge.foundation.data.world.biome.feature.TEBiomeFeatures;
import com.pouffydev.the_edge.foundation.data.world.biome.feature.TheEdgeFeatures;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import com.pouffydev.the_edge.foundation.data.world.ChunkGeneratorEdge;

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
    public static final CreativeModeTab itemGroup = new ModItemTab(ID);
    public TheEdge() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        
        //Worldgen stuff (I hate this)
        BiomeKeys.BIOMES.register(eventBus);
        TENoiseGenerationSettings.NOISE_GENERATORS.register(eventBus);
        //TEBiomeFeatures.FEATURES.register(eventBus);
        TheEdgeFeatures.FOLIAGE_PLACERS.register(eventBus);
        
        TEFluids.register();
        TEBlocks.register();
        TEItems.register();
        TEBlockEntities.register();
        TEPartialModels.register();
        TEConfigs.register(modLoadingContext);
        TETags.init();
        registrate.registerEventListeners(eventBus);
        
        MinecraftForge.EVENT_BUS.register(this);
    }
    @Contract("_ -> new")
    public static @NotNull ResourceLocation asResource(String path) {
        return new ResourceLocation(ID, path);
    }
    //@SubscribeEvent
    //public static void registerSerializers(RegistryEvent.Register<RecipeSerializer<?>> evt) {
    //    //TODO find a better place for these? they work fine here but idk
    //    Registry.register(Registry.BIOME_SOURCE, asResource("edge_biomes"), TEBiomeProvider.TE_CODEC);
    //
    //    Registry.register(Registry.CHUNK_GENERATOR, asResource("structure_locating_wrapper"), ChunkGeneratorEdge.CODEC);
    //}
    private void setup(final FMLCommonSetupEvent event) {}

    public static @NotNull CreateRegistrate registrate() {
        return registrate;
    }
}
