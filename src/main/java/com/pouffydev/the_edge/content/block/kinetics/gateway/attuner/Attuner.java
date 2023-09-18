package com.pouffydev.the_edge.content.block.kinetics.gateway.attuner;

import com.pouffydev.the_edge.TEBlockEntities;
import com.pouffydev.the_edge.TEShapes;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.logistics.chute.AbstractChuteBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Attuner extends DirectionalKineticBlock implements IBE<AttunerBlockEntity> {
    
    public static final BooleanProperty linked = BooleanProperty.create("linked");
    public Attuner(Properties properties) {
        super(properties);
    }
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return TEShapes.largeGearDirectional.get(pState.getValue(FACING));
    }
    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING)
                .getAxis();
    }
    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING)
                .getOpposite();
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction face = context.getClickedFace();
        
        BlockState placedOn = world.getBlockState(pos.relative(face.getOpposite()));
        BlockState placedOnOpposite = world.getBlockState(pos.relative(face));
        if (AbstractChuteBlock.isChute(placedOn))
            return defaultBlockState().setValue(FACING, face.getOpposite());
        if (AbstractChuteBlock.isChute(placedOnOpposite))
            return defaultBlockState().setValue(FACING, face);
        
        Direction preferredFacing = getPreferredFacing(context);
        if (preferredFacing == null)
            preferredFacing = context.getNearestLookingDirection();
        return defaultBlockState().setValue(FACING, context.getPlayer() != null && context.getPlayer()
                .isShiftKeyDown() ? preferredFacing : preferredFacing.getOpposite());
    }
    @Override
    public Class<AttunerBlockEntity> getBlockEntityClass() {
        return AttunerBlockEntity.class;
    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(linked);
        super.createBlockStateDefinition(builder);
    }
    @Override
    public BlockEntityType<? extends AttunerBlockEntity> getBlockEntityType() {
        return TEBlockEntities.ATTUNER.get();
    }
}
