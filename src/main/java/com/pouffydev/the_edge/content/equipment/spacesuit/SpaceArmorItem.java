package com.pouffydev.the_edge.content.equipment.spacesuit;

import com.simibubi.create.content.equipment.armor.BacktankUtil;
import com.simibubi.create.content.equipment.armor.BaseArmorItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

public class SpaceArmorItem extends BaseArmorItem {
    public SpaceArmorItem(ArmorMaterial armorMaterial, EquipmentSlot slot, Properties properties, ResourceLocation textureLoc) {
        super(armorMaterial, slot, properties, textureLoc);
    }
    public static boolean hasFullSet(LivingEntity entity) {
        int slotCount = 0;
        int armorCount = 0;
        for (ItemStack stack : entity.getArmorSlots()) {
            slotCount++;
            if (stack.getItem() instanceof SpaceArmorItem) {
                armorCount++;
            }
        }
        return slotCount > 0 && armorCount == slotCount;
    }
    /**
     * Checks if the entity is wearing a space suit and if that space suit has air.
     *
     * @param entity The entity wearing the space suit
     * @return Whether the entity has air or not
     */
    public static boolean hasOxygenatedSpaceSuit(LivingEntity entity) {
        ItemStack chest = entity.getItemBySlot(EquipmentSlot.CHEST);
        if (chest.getItem() instanceof SpaceBacktankItem) {
            return SpaceBacktankItem.getRemainingAir(chest) > 0;
        }
        
        return false;
    }
    public static void consumeSpaceSuitOxygen(LivingEntity entity, long amount) {
        ItemStack chest = new ItemStack(entity.getItemBySlot(EquipmentSlot.CHEST).getItem());
        if (chest.getItem() instanceof SpaceBacktankItem) {
            BacktankUtil.consumeAir(entity, chest, amount);
        }
    }
}
