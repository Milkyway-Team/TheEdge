package com.pouffydev.the_edge.content.block.foliage;

import com.pouffydev.the_edge.TESoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BearTrapBlock extends Block {
    protected static final VoxelShape shape = Block.box(2.0D, 0.0D, 1.0D, 14.0D, 2.0D, 15.0D);
    protected static final VoxelShape shapeTriggered = Block.box(3.0D, 0.0D, 1.0D, 13.0D, 8.0D, 15.0D);
    public static final BooleanProperty triggered = BooleanProperty.create("triggered");
    public BearTrapBlock(Properties properties) {
        super(properties);
    }
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        if (isTriggered(state.getBlock())) {
            return shapeTriggered;
        }
        return shape;
    }
    public static boolean isTriggered (Block block) {
        return block.defaultBlockState().getValue(triggered);
    }
    public static void trigger (Block block, BlockPos pos, Level level) {
        block.defaultBlockState().setValue(triggered, true);
        level.playSound(null, pos, TESoundEvents.chompWeedTrigger.getMainEvent(), SoundSource.BLOCKS, 0.3F, 0.6F);
    }
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && !isTriggered(state.getBlock())) {
            entity.makeStuckInBlock(state, new Vec3((double)1F, 1D, (double)1F));
            trigger(state.getBlock(), pos, level);
        }
    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(triggered);
    }
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockState state = this.defaultBlockState();
        return state.setValue(triggered, false);
    }
}
