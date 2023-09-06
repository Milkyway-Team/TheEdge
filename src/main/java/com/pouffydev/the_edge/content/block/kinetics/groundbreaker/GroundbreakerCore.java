package com.pouffydev.the_edge.content.block.kinetics.groundbreaker;

import com.pouffydev.the_edge.TEBlocks;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.decoration.girder.GirderBlock;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class GroundbreakerCore extends Block {
    public GroundbreakerCore(Properties properties) {
        super(properties);
    }
    
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
    if (level.isClientSide) {
        return InteractionResult.SUCCESS;
    }
    if (player.getItemInHand(hand).is(AllItems.WRENCH.get()) && isCorrectStructurePresent(state, level, pos)) {
        assemble(pos, state, level);
        return InteractionResult.SUCCESS;
    }
        return InteractionResult.FAIL;
    }
    
    public boolean isAreaClear(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockState(pos).getBlock() instanceof GroundbreakerCore) {
            for (BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 0, 1))) {
                if (level.getBlockState(blockpos).isAir()) {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean isCorrectStructurePresent(BlockState state, Level level, BlockPos pos) {
        boolean northPress = level.getBlockState(pos.offset(0, 0, 1)).getBlock() instanceof MechanicalPressBlock;
        boolean eastPress = level.getBlockState(pos.offset(-1, 0, 0)).getBlock() instanceof MechanicalPressBlock;
        boolean southPress = level.getBlockState(pos.offset(0, 0, -1)).getBlock() instanceof MechanicalPressBlock;
        boolean westPress = level.getBlockState(pos.offset(1, 0, 0)).getBlock() instanceof MechanicalPressBlock;
        boolean northEastGirder = level.getBlockState(pos.offset(1, 0, -1)).getBlock() instanceof GirderBlock;
        boolean southEastGirder = level.getBlockState(pos.offset(1, 0, 1)).getBlock() instanceof GirderBlock;
        boolean southWestGirder = level.getBlockState(pos.offset(-1, 0, 1)).getBlock() instanceof GirderBlock;
        boolean northWestGirder = level.getBlockState(pos.offset(-1, 0, -1)).getBlock() instanceof GirderBlock;
        boolean lowerNorthEastGirder = level.getBlockState(pos.offset(1, -1, -1)).getBlock() instanceof GirderBlock;
        boolean lowerSouthEastGirder = level.getBlockState(pos.offset(1, -1, 1)).getBlock() instanceof GirderBlock;
        boolean lowerSouthWestGirder = level.getBlockState(pos.offset(-1, -1, 1)).getBlock() instanceof GirderBlock;
        boolean lowerNorthWestGirder = level.getBlockState(pos.offset(-1, -1, -1)).getBlock() instanceof GirderBlock;
        //boolean drill = level.getBlockState(pos.offset(0, -1, 0)).getBlock() instanceof DrillBlock;
        if (level.getBlockState(pos).getBlock() instanceof GroundbreakerCore) {
            return northPress && eastPress && southPress && westPress && northEastGirder && southEastGirder && southWestGirder && northWestGirder && lowerNorthEastGirder && lowerSouthEastGirder && lowerSouthWestGirder && lowerNorthWestGirder /*&& drill*/;
        }
        return false;
    }
    public void assemble(BlockPos pos, BlockState state, Level level) {
        if (level.isClientSide) {
            return;
        }
        if (state.getBlock() instanceof GroundbreakerCore && isCorrectStructurePresent(state, level, pos)) {
            level.setBlockAndUpdate(pos, TEBlocks.groundbreakerGearbox.getDefaultState());
            level.setBlockAndUpdate(pos.offset(0, 0, 1), AllBlocks.MECHANICAL_PRESS.getDefaultState().setValue(MechanicalPressBlock.HORIZONTAL_FACING, Direction.NORTH));
            level.setBlockAndUpdate(pos.offset(-1, 0, 0), AllBlocks.MECHANICAL_PRESS.getDefaultState().setValue(MechanicalPressBlock.HORIZONTAL_FACING, Direction.EAST));
            level.setBlockAndUpdate(pos.offset(0, 0, -1), AllBlocks.MECHANICAL_PRESS.getDefaultState().setValue(MechanicalPressBlock.HORIZONTAL_FACING, Direction.SOUTH));
            level.setBlockAndUpdate(pos.offset(1, 0, 0), AllBlocks.MECHANICAL_PRESS.getDefaultState().setValue(MechanicalPressBlock.HORIZONTAL_FACING, Direction.WEST));
            level.setBlockAndUpdate(pos.offset(1, 0, -1), AllBlocks.METAL_GIRDER.getDefaultState().setValue(GirderBlock.BOTTOM, true));
            level.setBlockAndUpdate(pos.offset(1, 0, 1), AllBlocks.METAL_GIRDER.getDefaultState().setValue(GirderBlock.BOTTOM, true));
            level.setBlockAndUpdate(pos.offset(-1, 0, 1), AllBlocks.METAL_GIRDER.getDefaultState().setValue(GirderBlock.BOTTOM, true));
            level.setBlockAndUpdate(pos.offset(-1, 0, -1), AllBlocks.METAL_GIRDER.getDefaultState().setValue(GirderBlock.BOTTOM, true));
            level.setBlockAndUpdate(pos.offset(1, -1, -1), AllBlocks.METAL_GIRDER.getDefaultState().setValue(GirderBlock.BOTTOM, true));
            level.setBlockAndUpdate(pos.offset(1, -1, 1), AllBlocks.METAL_GIRDER.getDefaultState().setValue(GirderBlock.BOTTOM, true));
            level.setBlockAndUpdate(pos.offset(-1, -1, 1), AllBlocks.METAL_GIRDER.getDefaultState().setValue(GirderBlock.BOTTOM, true));
            level.setBlockAndUpdate(pos.offset(-1, -1, -1), AllBlocks.METAL_GIRDER.getDefaultState().setValue(GirderBlock.BOTTOM, true));
            level.setBlockAndUpdate(pos.offset(0, -1, 0), TEBlocks.groundbreakerDrill.getDefaultState());
        }
    }
}
