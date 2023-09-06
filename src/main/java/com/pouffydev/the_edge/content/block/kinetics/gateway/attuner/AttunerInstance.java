package com.pouffydev.the_edge.content.block.kinetics.gateway.attuner;

import com.jozufozu.flywheel.api.MaterialManager;
import com.pouffydev.the_edge.TEPartialModels;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.content.kinetics.fan.FanInstance;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

public class AttunerInstance extends KineticBlockEntityInstance<AttunerBlockEntity> {
    protected final RotatingData axis;
    protected final RotatingData ring;
    final Direction direction;
    private final Direction opposite;
    
    public AttunerInstance(MaterialManager materialManager, AttunerBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        direction = blockState.getValue(FACING);
        
        opposite = direction.getOpposite();
        axis = getRotatingMaterial().getModel(TEPartialModels.ATTUNER_AXIS, blockState, opposite).createInstance();
        ring = materialManager.defaultCutout()
                .material(AllMaterialSpecs.ROTATING)
                .getModel(TEPartialModels.ATTUNER_RING, blockState, opposite)
                .createInstance();
        
        setup(axis);
        setup(ring);
    }
    
    @Override
    public void update() {
        updateRotation(axis);
        updateRotation(ring);
    }
    
    @Override
    public void updateLight() {
        BlockPos behind = pos.relative(opposite);
        relight(behind, axis);
        
        BlockPos inFront = pos.relative(direction);
        relight(inFront, ring);
    }
    
    @Override
    public void remove() {
        axis.delete();
        ring.delete();
    }
    
}
