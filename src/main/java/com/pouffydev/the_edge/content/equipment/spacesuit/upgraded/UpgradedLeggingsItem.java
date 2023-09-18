package com.pouffydev.the_edge.content.equipment.spacesuit.upgraded;

import com.pouffydev.the_edge.TEKeys;
import com.pouffydev.the_edge.content.equipment.spacesuit.SpaceArmorItem;
import com.pouffydev.the_edge.foundation.config.TEConfigs;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;

public class UpgradedLeggingsItem extends SpaceArmorItem {
    public UpgradedLeggingsItem(ArmorMaterial armorMaterial, Properties properties, ResourceLocation textureLoc) {
        super(armorMaterial, EquipmentSlot.LEGS, properties, textureLoc);
    }
    static float boost = TEConfigs.server().equipment.spaceSuitBoost.getF();
    @Override
    public void inventoryTick(@Nonnull ItemStack stack, Level level, @Nonnull Entity entity, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, level, entity, itemSlot, isSelected);
        Player player = (Player) entity;
        Vec3 vec3 = entity.getDeltaMovement();
        Direction dir = entity.getMotionDirection();
        if (player.isFallFlying()) {
            entity.setDeltaMovement(vec3.add(dir.getStepX() * boost, dir.getStepY() * boost, dir.getStepZ() * boost));
        }
        if (TEKeys.boost.isDown() && !entity.isSteppingCarefully() && !player.isFallFlying() && !((Player) entity).getCooldowns().isOnCooldown(this)) {
            entity.setDeltaMovement(vec3.x, vec3.y + boost, vec3.z);
            ((Player) entity).getCooldowns().addCooldown(this, 300);
        }
        if (entity.fallDistance == 0)
            ((Player) entity).getCooldowns().removeCooldown(this);
    }
}
