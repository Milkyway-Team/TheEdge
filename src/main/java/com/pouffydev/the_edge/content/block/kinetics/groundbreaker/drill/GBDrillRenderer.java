package com.pouffydev.the_edge.content.block.kinetics.groundbreaker.drill;

import com.pouffydev.the_edge.TEPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class GBDrillRenderer extends KineticBlockEntityRenderer<GBDrillBlockEntity> {
    public GBDrillRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    @Override
    protected SuperByteBuffer getRotatedModel(GBDrillBlockEntity be, BlockState state) {
        return CachedBufferer.partialFacing(TEPartialModels.GB_DRILL, state);
    }
}
