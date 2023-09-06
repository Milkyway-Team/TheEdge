package com.pouffydev.the_edge.content.equipment.insulation_pack;

import com.pouffydev.the_edge.TEItems;
import com.simibubi.create.content.equipment.armor.BacktankUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;

public class InsulationPackItem extends Item implements ICurio {
    public InsulationPackItem(Properties properties) {
        super(properties);
    }
    public static final int BAR_COLOR = 0xc63f51;
    @Override
    public int getBarColor(ItemStack stack) {
        return BAR_COLOR;
    }
    public static boolean isEquipped(ItemStack stack, LivingEntity entity) {
        return CuriosApi.getCuriosHelper()
                .findEquippedCurio(stack.getItem(), entity)
                .isPresent();
    }
    public static int getRemainingHeat(ItemStack stack) {
        CompoundTag orCreateTag = stack.getOrCreateTag();
        return orCreateTag.getInt("Air");
    }
    public void onEquip(SlotContext slotContext, ItemStack prevStack) {
        CompoundTag tag = prevStack.getOrCreateTag();
        tag.putBoolean("Equipped", true);
    }
    public void onUnequip(SlotContext slotContext, ItemStack newStack) {
        CompoundTag tag = newStack.getOrCreateTag();
        tag.putBoolean("Equipped", false);
    }
    
    public static boolean hasInsulatedPack(LivingEntity entity) {
        ItemStack stack = TEItems.insulationPack.asStack();
        return isEquipped(stack, entity) && getRemainingHeat(stack) > 0;
    }
    public static void consumeHeat(LivingEntity entity, long amount) {
        ItemStack stack = TEItems.insulationPack.asStack();
        List<SlotResult> insulator = CuriosApi.getCuriosHelper().findCurios(entity, stack.getItem());
        ItemStack insulatorStack = insulator.get(0).stack();
        if (insulatorStack.getItem() instanceof InsulationPackItem) {
            BacktankUtil.consumeAir(entity, insulatorStack, amount);
        }
    }
    @Override
    public ItemStack getStack() {
        return null;
    }
}
