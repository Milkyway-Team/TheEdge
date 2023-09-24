package com.pouffydev.the_edge.content.block.dungeons.completion_monolith;

import com.pouffydev.the_edge.TEBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public class CompletionMonolithItem extends BlockItem {
    public CompletionMonolithItem(Block block, Item.Properties properties) {
        super(block, properties);
    }
    
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos().above();
        BlockState blockstate = level.isWaterAt(blockpos) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
        level.setBlock(blockpos, blockstate, 27);
        return super.placeBlock(context, Objects.requireNonNull(TEBlocks.completionMonolith.get().getStateForPlacement(context)));
    }
}
