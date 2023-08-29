package com.pouffydev.the_edge.content.equipment.spacesuit;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;

public class SpaceBacktankInstance extends SingleRotatingInstance<SpaceBacktankBlockEntity> {
    
    public SpaceBacktankInstance(MaterialManager materialManager, SpaceBacktankBlockEntity blockEntity) {
        super(materialManager, blockEntity);
    }
    
    @Override
    protected Instancer<RotatingData> getModel() {
        return getRotatingMaterial().getModel(SpaceBacktankRenderer.getShaftModel(blockState), blockState);
    }
}
