package com.pouffydev.the_edge.content.block.kinetics.groundbreaker;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GroundbreakerPress extends HorizontalKineticBlock implements IBE<MechanicalPressBlockEntity> {
    public GroundbreakerPress(Properties properties) {
        super(properties);
    }
    
    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING)
                .getAxis();
    }
    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(HORIZONTAL_FACING)
                .getAxis();
    }
    
    @Override
    public Class<MechanicalPressBlockEntity> getBlockEntityClass() {
        return MechanicalPressBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends MechanicalPressBlockEntity> getBlockEntityType() {
        return AllBlockEntityTypes.MECHANICAL_PRESS.get();
    }
}
