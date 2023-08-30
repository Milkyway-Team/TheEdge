package com.pouffydev.the_edge.content.block.foliage;

import com.pouffydev.the_edge.foundation.TETags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class LevivineBlock extends DoubleEdgePlantBlock {
    public LevivineBlock(Properties properties) {
        super(properties, TETags.AllBlockTags.supportsForestPlants.tag);
    }
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity) {
            if (!level.isClientSide) {
                ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.LEVITATION, 10, 10), entity);
            }
            
        }
    }
}
