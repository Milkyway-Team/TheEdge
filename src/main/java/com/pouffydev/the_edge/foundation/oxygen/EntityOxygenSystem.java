package com.pouffydev.the_edge.foundation.oxygen;

import com.pouffydev.the_edge.content.equipment.spacesuit.SpaceArmorItem;
import com.pouffydev.the_edge.foundation.ModUtils;
import com.pouffydev.the_edge.foundation.TETags;
import com.pouffydev.the_edge.foundation.config.TEConfigs;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class EntityOxygenSystem {
    public static final DamageSource oxygenDamage = new DamageSource("oxygen").bypassArmor();
    public static void oxygenTick(LivingEntity entity, ServerLevel level) {
        if (!TEConfigs.server().world.doEdgeOxygen.get()) {
            return;
        }
        if (entity.isInvertedHealAndHarm()) {
            return;
        }
        
        if (ModUtils.checkTag(entity, TETags.AllEntityTags.livesWithoutOxygen.tag)) {
            return;
        }
        
        if (OxygenUtils.levelHasOxygen(level) && !entity.isUnderWater()) {
            return;
        }
        
        boolean levelHasOxygen = OxygenUtils.levelHasOxygen(level);
        boolean hasOxygenatedSpaceSuit = SpaceArmorItem.hasOxygenatedSpaceSuit(entity) && SpaceArmorItem.hasFullSet(entity);
        
        if (levelHasOxygen && hasOxygenatedSpaceSuit && entity.isUnderWater() && !entity.canBreatheUnderwater() && !entity.hasEffect(MobEffects.WATER_BREATHING)) {
            consumeOxygen(entity);
            return;
        }
        
        if (!levelHasOxygen) {
            if (hasOxygenatedSpaceSuit) {
                consumeOxygen(entity);
            } else if (!ModUtils.armourIsOxygenated(entity)) {
                entity.hurt(oxygenDamage, TEConfigs.server().world.oxygenDamage.get());
                entity.setAirSupply(-40);
            }
        }
    }
    private static void consumeOxygen(LivingEntity entity) {
        if (entity.getLevel().getGameTime() % 3 == 0) {
            entity.setAirSupply(Math.min(entity.getMaxAirSupply(), entity.getAirSupply() + 4 * 10));
            SpaceArmorItem.consumeSpaceSuitOxygen(entity, 1);
        }
    }
}
