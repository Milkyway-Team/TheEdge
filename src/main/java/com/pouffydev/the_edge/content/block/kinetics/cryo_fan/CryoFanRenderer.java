package com.pouffydev.the_edge.content.block.kinetics.cryo_fan;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class CryoFanRenderer extends KineticBlockEntityRenderer<CryoFanBlockEntity> {
    
    public CryoFanRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    @Override
    public boolean shouldRenderOffScreen(CryoFanBlockEntity be) {
        return true;
    }
    
    @Override
    protected void renderSafe(CryoFanBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        if (Backend.canUseInstancing(be.getLevel())) return;
        
        BlockState blockState = be.getBlockState();
        
        VertexConsumer vb = buffer.getBuffer(RenderType.solid());
        
        float speed = be.getSpeed();
        float time = AnimationTickHolder.getRenderTime(be.getLevel());
        float angle = ((time * speed * 6 / 10f) % 360) / 180 * (float) Math.PI;
    }
}
