package com.pouffydev.the_edge.content.block.kinetics.groundbreaker.drill;

import com.pouffydev.the_edge.TEBlockEntities;
import com.pouffydev.the_edge.TEShapes;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GBDrillBlock extends DirectionalKineticBlock implements IBE<GBDrillBlockEntity> {
    public GBDrillBlock(Properties properties) {
        super(properties);
    }
    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.UP;
    }
    
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return TEShapes.drillHead.get(Direction.UP);
    }
    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }
    @Override
    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.FAST;
    }
    @Override
    public Class<GBDrillBlockEntity> getBlockEntityClass() {
        return GBDrillBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends GBDrillBlockEntity> getBlockEntityType() {
        return TEBlockEntities.GB_DRILL.get();
    }
    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }
}
