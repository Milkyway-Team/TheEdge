package com.pouffydev.the_edge.foundation.oxygen;

import com.pouffydev.the_edge.content.equipment.insulation_pack.InsulationPackItem;
import com.pouffydev.the_edge.foundation.ModUtils;
import com.pouffydev.the_edge.foundation.TETags;
import com.pouffydev.the_edge.foundation.config.TEConfigs;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class EntityEffectsSystem {
    public static final DamageSource frostDamage = new DamageSource("frost").bypassArmor();
    public static final DamageSource pressureDamage = new DamageSource("pressure").bypassArmor();
    
    public static void frostTick(LivingEntity entity, ServerLevel level) {
        if (!TEConfigs.server().world.doEdgeFreezing.get()) {
            return;
        }
        if (entity.isInvertedHealAndHarm()) {
            return;
        }
        
        if (ModUtils.checkTag(entity, TETags.AllEntityTags.canSurviveFrost.tag)) {
            return;
        }
        
        if (!EffectUtils.biomeIsFreezing(entity)) {
            return;
        }
        
        boolean biomeIsFreezing = EffectUtils.biomeIsFreezing(entity);
        boolean hasInsulatedPack = InsulationPackItem.hasInsulatedPack(entity);
        
        if (biomeIsFreezing && hasInsulatedPack && entity.isUnderWater() && !entity.canBreatheUnderwater() && !entity.hasEffect(MobEffects.WATER_BREATHING)) {
            consumeHeat(entity);
            return;
        }
        
        if (biomeIsFreezing) {
            if (hasInsulatedPack) {
                consumeHeat(entity);
            } else {
                entity.hurt(frostDamage, TEConfigs.server().world.freezingDamage.get());
            }
        }
    }
    private static void consumeHeat(LivingEntity entity) {
        if (entity.getLevel().getGameTime() % 3 == 0) {
            entity.setAirSupply(Math.min(entity.getMaxAirSupply(), entity.getAirSupply() + 4 * 10));
            InsulationPackItem.consumeHeat(entity, 1);
        }
    }
}
