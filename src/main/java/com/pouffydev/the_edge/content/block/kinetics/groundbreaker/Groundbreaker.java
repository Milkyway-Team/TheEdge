package com.pouffydev.the_edge.content.block.kinetics.groundbreaker;

import com.simibubi.create.content.kinetics.base.KineticBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class Groundbreaker extends KineticBlock {
    
    public Groundbreaker(Properties properties) {
        super(properties);
    }
    
    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return null;
    }
}
