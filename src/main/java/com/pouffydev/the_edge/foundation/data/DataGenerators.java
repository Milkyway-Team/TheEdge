package com.pouffydev.the_edge.foundation.data;

import com.pouffydev.the_edge.TELangPartials;
import com.pouffydev.the_edge.TheEdge;
import com.pouffydev.the_edge.foundation.data.recipe.TEStandardRecipeGen;
import com.pouffydev.the_edge.foundation.data.world.WorldGenerator;
import com.pouffydev.the_edge.foundation.data.world.biome.TEBiomeProvider;
import com.simibubi.create.foundation.data.LangMerger;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = TheEdge.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent evt) {
        DataGenerator generator = evt.getGenerator();
        ExistingFileHelper helper = evt.getExistingFileHelper();
        generator.addProvider(new LangMerger(generator, TheEdge.ID, "The Edge", TELangPartials.values()));
        generator.addProvider(new WorldGenerator(generator));
        generator.addProvider(new TEStandardRecipeGen(generator));
        
    }
    
}
