package com.pouffydev.the_edge.content.effect;

import com.pouffydev.the_edge.foundation.config.TEConfigs;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FreezingEffect extends MobEffect {
    public static final DamageSource frostDamage = new DamageSource("frost").bypassArmor();
    protected FreezingEffect(MobEffectCategory category, int color) {
        super(category, color);
    }
    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        Level world = pLivingEntity.level;
        Vec3 pos = pLivingEntity.position();
        Vec3 basemotion;
        basemotion = VecHelper.offsetRandomly(pos, world.random, 0.5F);
        if(!pLivingEntity.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            pLivingEntity.hurt(frostDamage, TEConfigs.server().world.freezingDamage.get());
            pLivingEntity.setSpeed(0.3f);
            world.addParticle(ParticleTypes.SNOWFLAKE, basemotion.x, pos.y + 1.5, basemotion.z, 0.0, 0.10000000149011612, 0.0);
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }
    
    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
    
}
