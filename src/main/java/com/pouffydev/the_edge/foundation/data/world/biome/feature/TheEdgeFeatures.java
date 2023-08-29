package com.pouffydev.the_edge.foundation.data.world.biome.feature;

import com.pouffydev.the_edge.TheEdge;
import com.pouffydev.the_edge.foundation.data.world.biome.chunkgenerators.OutOfStructureFilter;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public final class TheEdgeFeatures {
    public static long seed;
    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIERS = DeferredRegister.create(Registry.PLACEMENT_MODIFIER_REGISTRY, TheEdge.ID);
    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACERS = DeferredRegister.create(ForgeRegistries.FOLIAGE_PLACER_TYPES, TheEdge.ID);
    public static final RegistryObject<PlacementModifierType<OutOfStructureFilter>> NO_STRUCTURE = registerPlacer("no_structure", () -> () -> OutOfStructureFilter.CODEC);
    
    
    
    //goofy but needed
    private static <P extends PlacementModifier> RegistryObject<PlacementModifierType<P>> registerPlacer(String name, Supplier<PlacementModifierType<P>> factory) {
        return PLACEMENT_MODIFIERS.register(name, factory);
    }
}
