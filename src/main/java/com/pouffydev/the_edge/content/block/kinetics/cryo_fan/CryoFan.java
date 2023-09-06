package com.pouffydev.the_edge.content.block.kinetics.cryo_fan;

import com.pouffydev.the_edge.TEBlockEntities;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class CryoFan extends KineticBlock implements IBE<CryoFanBlockEntity>, ICogWheel {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public CryoFan(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.valueOf(false)));
    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(LIT);
    }
    @Override
    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.of(256);
    }
    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }
    
    @Override
    public Class<CryoFanBlockEntity> getBlockEntityClass() {
        return CryoFanBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends CryoFanBlockEntity> getBlockEntityType() {
        return TEBlockEntities.AREA_HEATER.get();
    }
}
