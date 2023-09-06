package com.pouffydev.the_edge.content.block.kinetics.groundbreaker.gearbox;

import com.pouffydev.the_edge.TEBlockEntities;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;

public class GBGearbox extends KineticBlock implements IBE<GBGearboxBlockEntity> {
    
    public GBGearbox(Properties properties) {
        super(properties);
    }
    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.IGNORE;
    }
    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Z;
    }
    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return Direction.Axis.X == face.getAxis() || Direction.Axis.Z == face.getAxis() || Direction.Axis.Y == face.getAxis();
    }
    
    @Override
    public Class<GBGearboxBlockEntity> getBlockEntityClass() {
        return GBGearboxBlockEntity.class;
    }
    @Override
    public BlockEntityType<? extends GBGearboxBlockEntity> getBlockEntityType() {
        return TEBlockEntities.GB_GEARBOX.get();
    }
}
