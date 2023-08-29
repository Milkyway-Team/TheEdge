package com.pouffydev.the_edge.content.equipment.spacesuit;

import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.pouffydev.the_edge.TEPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class SpaceBacktankRenderer extends KineticBlockEntityRenderer<SpaceBacktankBlockEntity> {
    public SpaceBacktankRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    
    @Override
    protected void renderSafe(SpaceBacktankBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        
        BlockState blockState = be.getBlockState();
    }
    
    @Override
    protected SuperByteBuffer getRotatedModel(SpaceBacktankBlockEntity be, BlockState state) {
        return CachedBufferer.partial(getShaftModel(state), state);
    }
    
    public static PartialModel getAirBarModel(SpaceBacktankBlockEntity be) {
        if (be.getAirPercentage() > 0.16F) {
            return TEPartialModels.BACKTANK_AIR_1;
        } else if (be.getAirPercentage() > 0.33F) {
            return TEPartialModels.BACKTANK_AIR_2;
        } else if (be.getAirPercentage() > 0.5F) {
            return TEPartialModels.BACKTANK_AIR_3;
        } else if (be.getAirPercentage() > 0.66F) {
            return TEPartialModels.BACKTANK_AIR_4;
        } else if (be.getAirPercentage() > 0.83F) {
            return TEPartialModels.BACKTANK_AIR_5;
        } else if (be.getAirPercentage() > 0.99F) {
            return TEPartialModels.BACKTANK_AIR_FULL;
        }
        return TEPartialModels.BACKTANK_AIR_EMPTY;
    }
    
    
    public static PartialModel getShaftModel(BlockState state) {
        return TEPartialModels.BACKTANK_SHAFT;
    }
}
