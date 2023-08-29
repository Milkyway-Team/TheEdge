package com.pouffydev.the_edge.mixin;

import com.pouffydev.the_edge.foundation.ModUtils;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityGravityMixin {
    @Unique
    private static final double theEdge$CONSTANT = 0.08;
    
    @Inject(method = "travel", at = @At("TAIL"))
    public void the_edge$travel(CallbackInfo ci) {
            LivingEntity entity = (LivingEntity) (Object) this;
            
            Vec3 velocity = entity.getDeltaMovement();
            
            if (!entity.isNoGravity() && !entity.isInWater() && !entity.isInLava() && !entity.isFallFlying() && !entity.hasEffect(MobEffects.SLOW_FALLING)) {
                double newGravity = theEdge$CONSTANT * ModUtils.getEntityGravity(entity);
                entity.setDeltaMovement(velocity.x(), velocity.y() + theEdge$CONSTANT - newGravity, velocity.z());
            }
    }
    
    // Make fall damage gravity-dependant
    @ModifyVariable(method = "causeFallDamage", at = @At("HEAD"), ordinal = 1, argsOnly = true)
    private float the_edge$causeFallDamage(float damageMultiplier) {
        LivingEntity entity = ((LivingEntity) (Object) this);
        return damageMultiplier * ModUtils.getEntityGravity(entity);
    }
}
