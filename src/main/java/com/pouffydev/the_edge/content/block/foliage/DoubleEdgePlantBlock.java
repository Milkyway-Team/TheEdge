package com.pouffydev.the_edge.content.block.foliage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import javax.annotation.Nullable;

public class DoubleEdgePlantBlock extends EdgeBushBlock {
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    
    private TagKey<Block> blockTag;
    public DoubleEdgePlantBlock(BlockBehaviour.Properties properties, TagKey<Block> blockTag) {
        super(properties, blockTag);
        this.blockTag = blockTag;
        this.registerDefaultState(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER));
    }
    
    public BlockState updateShape(BlockState state, Direction direction, BlockState blockState, LevelAccessor accessor, BlockPos pos1, BlockPos pos2) {
        DoubleBlockHalf doubleblockhalf = state.getValue(HALF);
        if (direction.getAxis() != Direction.Axis.Y || doubleblockhalf == DoubleBlockHalf.LOWER != (direction == Direction.UP) || blockState.is(this) && blockState.getValue(HALF) != doubleblockhalf) {
            return doubleblockhalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canSurvive(accessor, pos1) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, blockState, accessor, pos1, pos2);
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }
    
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        Level level = context.getLevel();
        return blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(context) ? super.getStateForPlacement(context) : null;
    }
    
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
        BlockPos blockpos = pos.above();
        level.setBlock(blockpos, copyWaterloggedFrom(level, blockpos, this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER)), 3);
    }
    
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        if (state.getValue(HALF) != DoubleBlockHalf.UPPER) {
            return super.canSurvive(state, reader, pos);
        } else {
            BlockState blockstate = reader.getBlockState(pos.below());
            if (state.getBlock() != this) return state.is(blockTag) || super.canSurvive(state, reader, pos); //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
            return blockstate.is(this) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER;
        }
    }
    
    public static void placeAt(LevelAccessor accessor, BlockState state, BlockPos pos, int p_153177_) {
        BlockPos blockpos = pos.above();
        accessor.setBlock(pos, copyWaterloggedFrom(accessor, pos, state.setValue(HALF, DoubleBlockHalf.LOWER)), p_153177_);
        accessor.setBlock(blockpos, copyWaterloggedFrom(accessor, blockpos, state.setValue(HALF, DoubleBlockHalf.UPPER)), p_153177_);
    }
    
    public static BlockState copyWaterloggedFrom(LevelReader reader, BlockPos pos, BlockState state) {
        return state.hasProperty(BlockStateProperties.WATERLOGGED) ? state.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(reader.isWaterAt(pos))) : state;
    }
    
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            if (player.isCreative()) {
                preventCreativeDropFromBottomPart(level, pos, state, player);
            } else {
                dropResources(state, level, pos, (BlockEntity)null, player, player.getMainHandItem());
            }
        }
        
        super.playerWillDestroy(level, pos, state, player);
    }
    
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity entity, ItemStack stack) {
        super.playerDestroy(level, player, pos, Blocks.AIR.defaultBlockState(), entity, stack);
    }
    
    protected static void preventCreativeDropFromBottomPart(Level p_52904_, BlockPos p_52905_, BlockState p_52906_, Player p_52907_) {
        DoubleBlockHalf doubleblockhalf = p_52906_.getValue(HALF);
        if (doubleblockhalf == DoubleBlockHalf.UPPER) {
            BlockPos blockpos = p_52905_.below();
            BlockState blockstate = p_52904_.getBlockState(blockpos);
            if (blockstate.is(p_52906_.getBlock()) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER) {
                BlockState blockstate1 = blockstate.hasProperty(BlockStateProperties.WATERLOGGED) && blockstate.getValue(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                p_52904_.setBlock(blockpos, blockstate1, 35);
                p_52904_.levelEvent(p_52907_, 2001, blockpos, Block.getId(blockstate));
            }
        }
        
    }
    
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_52901_) {
        p_52901_.add(HALF);
    }
    
    public BlockBehaviour.OffsetType getOffsetType() {
        return BlockBehaviour.OffsetType.XZ;
    }
    
    public long getSeed(BlockState p_52891_, BlockPos p_52892_) {
        return Mth.getSeed(p_52892_.getX(), p_52892_.below(p_52891_.getValue(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), p_52892_.getZ());
    }
}
