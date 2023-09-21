package com.pouffydev.the_edge.content.block.dungeons.heart_door;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pouffydev.the_edge.TEPartialModels;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class HeartDoorRenderer extends SafeBlockEntityRenderer<HeartDoorBlockEntity> {
    
    @Override
    protected void renderSafe(HeartDoorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        Direction direction = be.getBlockState().getValue(HORIZONTAL_FACING);
        int difficulty = HeartDoorBlockEntity.getDifficulty(be);
        SuperByteBuffer hrt0 = CachedBufferer.partialFacing(TEPartialModels.hd_dif0, be.getBlockState(), direction.getOpposite());
        SuperByteBuffer hrt1 = CachedBufferer.partialFacing(TEPartialModels.hd_dif1, be.getBlockState(), direction.getOpposite());
        SuperByteBuffer hrt2 = CachedBufferer.partialFacing(TEPartialModels.hd_dif2, be.getBlockState(), direction.getOpposite());
        SuperByteBuffer hrt3 = CachedBufferer.partialFacing(TEPartialModels.hd_dif3, be.getBlockState(), direction.getOpposite());
        SuperByteBuffer hrt4 = CachedBufferer.partialFacing(TEPartialModels.hd_dif4, be.getBlockState(), direction.getOpposite());
        if (difficulty == 0) {
            hrt0.renderInto(ms, bufferSource.getBuffer(RenderType.cutout()));
        } else if (difficulty == 1) {
            hrt1.renderInto(ms, bufferSource.getBuffer(RenderType.cutout()));
        } else if (difficulty == 2) {
            hrt2.renderInto(ms, bufferSource.getBuffer(RenderType.cutout()));
        } else if (difficulty == 3) {
            hrt3.renderInto(ms, bufferSource.getBuffer(RenderType.cutout()));
        } else if (difficulty == 4) {
            hrt4.renderInto(ms, bufferSource.getBuffer(RenderType.cutout()));
        }
        
    }
}
