package com.pouffydev.the_edge.mixin;

import com.pouffydev.the_edge.foundation.ModUtils;
import com.pouffydev.the_edge.foundation.oxygen.EntityEffectsSystem;
import com.pouffydev.the_edge.foundation.oxygen.EntityOxygenSystem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    public void the_edge$tick(CallbackInfo ci) {
        
        LivingEntity entity = ((LivingEntity) (Object) this);
        Level level = entity.level;
        if (!level.isClientSide) {
            if (level.getGameTime() % 10 == 0) {
                if (entity instanceof Player player && (player.isCreative() || player.isSpectator())) {
                    return;
                }
                
                EntityOxygenSystem.oxygenTick(entity, (ServerLevel) level);
                EntityEffectsSystem.frostTick(entity, (ServerLevel) level);
                
                if (!ModUtils.isEdgelevel(level)) {
                    return;
                }
            }
        }
    }
}
