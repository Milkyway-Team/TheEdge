package com.pouffydev.the_edge.content.block.kinetics.groundbreaker.gearbox;

import com.simibubi.create.content.kinetics.base.DirectionalShaftHalvesBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GBGearboxBlockEntity extends DirectionalShaftHalvesBlockEntity {
    public GBGearboxBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    @Override
    protected boolean isNoisy() {
        return false;
    }
}
