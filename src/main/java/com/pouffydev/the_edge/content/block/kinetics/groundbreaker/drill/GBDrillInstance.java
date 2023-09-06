package com.pouffydev.the_edge.content.block.kinetics.groundbreaker.drill;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.pouffydev.the_edge.TEPartialModels;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class GBDrillInstance extends SingleRotatingInstance<GBDrillBlockEntity> {
    public GBDrillInstance(MaterialManager materialManager, GBDrillBlockEntity blockEntity) {
        super(materialManager, blockEntity);
    }
    
    @Override
    protected Instancer<RotatingData> getModel() {
        BlockState referenceState = blockEntity.getBlockState();
        Direction facing = Direction.NORTH;
        return getRotatingMaterial().getModel(TEPartialModels.GB_DRILL, referenceState, facing);
    }
}
