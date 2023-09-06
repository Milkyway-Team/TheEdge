package com.pouffydev.the_edge.foundation;

import com.pouffydev.the_edge.TheEdge;
import com.pouffydev.the_edge.foundation.data.world.biome.BiomeKeys;
import com.pouffydev.the_edge.foundation.data.world.biome.TEBiomeProvider;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.Objects;
import java.util.stream.StreamSupport;

public class ModUtils {
    public static final ResourceKey<Level> EDGE_KEY = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(TheEdge.ID, "the_edge"));
    public static final ResourceKey<Biome> glacialPeaksKey = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(TheEdge.ID, "glacial_peaks"));
    public static boolean isEdgelevel(Level level) {
        return level.dimension().equals(EDGE_KEY);
    }
    public static boolean checkTag(Entity entity, TagKey<EntityType<?>> tag) {
        return entity.getType().is(tag);
    }
    public static float getEntityGravity(Entity entity) {
        return getEdgeGravity(entity.level);
    }
    public static float getEdgeGravity(Level level) {
        // Do not affect gravity for non-Ad Astra dimensions
        if (!ModUtils.isEdgelevel(level)) {
            return 1.0f;
        }
        return 0.2f;
    }
    public static boolean checkTag(ItemStack stack, TagKey<Item> tag) {
        return stack.is(tag);
    }
    public static boolean armourIsOxygenated(LivingEntity entity) {
        return StreamSupport.stream(entity.getArmorSlots().spliterator(), false).allMatch(s -> s.is(TETags.AllItemTags.OXYGENATED_ARMOR.tag));
    }
    
}
