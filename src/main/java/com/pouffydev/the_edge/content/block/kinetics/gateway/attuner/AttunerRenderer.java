package com.pouffydev.the_edge.content.block.kinetics.gateway.attuner;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.pouffydev.the_edge.TEPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.fan.EncasedFanRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

import java.util.Objects;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

public class AttunerRenderer extends KineticBlockEntityRenderer<AttunerBlockEntity> {
    public AttunerRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    @Override
    protected void renderSafe(AttunerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (Backend.canUseInstancing(be.getLevel())) return;
        
        Direction direction = be.getBlockState()
                .getValue(FACING);
        VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());
        
        SuperByteBuffer axis = CachedBufferer.partialFacing(TEPartialModels.ATTUNER_AXIS, be.getBlockState(), direction.getOpposite());
        SuperByteBuffer ring = CachedBufferer.partialFacing(TEPartialModels.ATTUNER_RING, be.getBlockState(), direction.getOpposite());
        
        float time = AnimationTickHolder.getRenderTime(be.getLevel());
        float speed = 0.1f * be.getSpeed();
        float angle = (time * speed * 3 / 10f) % 360;
        angle = angle / 180f * (float) Math.PI;
        if (speed > 0)
            speed = Mth.clamp(speed, 80, 64 * 20);
        if (speed < 0)
            speed = Mth.clamp(speed, -64 * 20, -80);
        
        standardKineticRotationTransform(axis, be, light).renderInto(ms, vb);
        kineticRotationTransform(ring, be, direction.getAxis(), angle, light).translate(0, (float) Math.sin(partialTicks / 20) / 16, 0).renderInto(ms, vb);
    }
}
