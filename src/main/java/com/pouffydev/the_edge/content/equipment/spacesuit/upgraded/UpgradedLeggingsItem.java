package com.pouffydev.the_edge.content.equipment.spacesuit.upgraded;

import com.mojang.math.Vector3f;
import com.pouffydev.the_edge.TEKeys;
import com.pouffydev.the_edge.TheEdge;
import com.pouffydev.the_edge.content.equipment.spacesuit.SpaceArmorItem;
import com.pouffydev.the_edge.foundation.config.TEConfigs;
import com.simibubi.create.content.legacy.ChromaticCompoundItem;
import com.simibubi.create.content.trains.CubeParticleData;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
    @SuppressWarnings("SuspiciousIndentAfterControlStatement")
    @Override
    public void inventoryTick(@Nonnull ItemStack stack, Level level, @Nonnull Entity entity, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, level, entity, itemSlot, isSelected);
        CompoundTag tag = entity.getPersistentData();
        Player player = (Player) entity;
        if (TEKeys.boost.isDown() && !entity.isSteppingCarefully() && !entity.isOnGround() && !player.isFallFlying() && !((Player) entity).getCooldowns().isOnCooldown(this)) {
            //entity.setDeltaMovement(vec3.x, vec3.y + boost, vec3.z);
            boostInLookDirection(entity, level);
            ((Player) entity).getCooldowns().addCooldown(this, 999999999);
        }
        if (entity.isOnGround())
            if (tag.getBoolean("Boosting")) {
                ParticleOptions dust = new DustParticleOptions(new Color(255, 255, 255).asVectorF(), 2.0F);
                Vec3 positionVec = entity.position();
                Vec3 vec3 = entity.getDeltaMovement();
                Vec3 start = VecHelper.offsetRandomly(positionVec, level.random, 4);
                entity.fallDistance = 0;
                for (int i = 0; i < 5; i++) {
                    level.addParticle(dust, positionVec.x, positionVec.y, positionVec.z, vec3.x * -2.5, vec3.y * -2.5, vec3.z * -2.5);
                    level.addParticle(dust, positionVec.x, positionVec.y, positionVec.z, vec3.x * -2.5, vec3.y * -2.5, vec3.z * -2.5);
                    level.addParticle(dust, positionVec.x, positionVec.y, positionVec.z, vec3.x * -2.5, vec3.y * -2.5, vec3.z * -2.5);
                    level.addParticle(dust, positionVec.x, positionVec.y, positionVec.z, vec3.x * -2.5, vec3.y * -2.5, vec3.z * -2.5);
                    level.addParticle(dust, positionVec.x, positionVec.y, positionVec.z, vec3.x * -2.5, vec3.y * -2.5, vec3.z * -2.5);
                    level.addParticle(dust, positionVec.x, positionVec.y, positionVec.z, vec3.x * -2.5, vec3.y * -2.5, vec3.z * -2.5);
                }
                tag.putBoolean("Boosting", false);
            }
            ((Player) entity).getCooldowns().removeCooldown(this);
    }
    
    private void boostInLookDirection(Entity entity, Level level) {
        CompoundTag tag = entity.getPersistentData();
        Vec3 vec3 = entity.getDeltaMovement();
        Vec3 dir = entity.getLookAngle();
        Vec3 positionVec = entity.position();
        Vec3 start = VecHelper.offsetRandomly(positionVec, level.random, 4);
        Vec3 cubeStart = VecHelper.offsetRandomly(positionVec, level.random, 3);
        Vector3f colorBright = new Color(0x64C9FD).asVectorF();
        Vector3f colorDark = new Color(0x3f74e8).asVectorF();
        Vec3 motion = positionVec.subtract(start).normalize().multiply(2f, 2f, 2f).scale(1.8f);
        Vec3 cubeMotion = positionVec.subtract(cubeStart).normalize().scale(3.5f);
        ParticleOptions dashLight = new DustParticleOptions(colorBright, 2.0F);
        ParticleOptions dashDark = new DustParticleOptions(colorDark, 2.0F);
        CubeParticleData lightCube = new CubeParticleData(191, 82, 91, 1f, 10, true);
        
        
        if (((Player) entity).getCooldowns().isOnCooldown(this)) {
            entity.sendMessage(Lang.translate(TheEdge.ID, "message.boost_cooldown").component(), entity.getUUID());
        } else {
            for (int i = 0; i < 10; i++) {
                level.addParticle(dashLight, start.x, start.y, start.z, motion.x, motion.y, motion.z);
                level.addParticle(dashDark, start.x, start.y, start.z, motion.x, motion.y, motion.z);
                level.addParticle(ParticleTypes.END_ROD, start.x, start.y, start.z, motion.x, motion.y, motion.z);
                level.addParticle(lightCube, cubeStart.x, cubeStart.y, cubeStart.z, cubeMotion.x, cubeMotion.y, cubeMotion.z);
            }
            entity.setDeltaMovement(vec3.add(dir.get(Direction.Axis.X) * boost, dir.get(Direction.Axis.Y) * boost, dir.get(Direction.Axis.Z) * boost));
            tag.putBoolean("Boosting", true);
        }
    }
}
