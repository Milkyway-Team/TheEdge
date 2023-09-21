package com.pouffydev.the_edge.content.block.dungeons.heart_door;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class HeartDoorBlockEntity extends BlockEntity {
    public HeartDoorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    public static Integer getDifficulty(HeartDoorBlockEntity be) {
        return be.getBlockState().getValue(HeartDoorBlock.difficulty);
    }
}
