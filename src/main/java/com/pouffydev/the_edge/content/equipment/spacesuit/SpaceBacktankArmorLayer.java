package com.pouffydev.the_edge.content.equipment.spacesuit;

import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.pouffydev.the_edge.TEPartialModels;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class SpaceBacktankArmorLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    public SpaceBacktankArmorLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }
    
    @Override
    public void render(PoseStack ms, MultiBufferSource buffer, int light, LivingEntity entity, float yaw, float pitch,
                       float pt, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        if (entity.getPose() == Pose.SLEEPING)
            return;
        
        SpaceBacktankItem item = SpaceBacktankItem.getWornBy(entity);
        ItemStack stack = entity.getItemBySlot(EquipmentSlot.CHEST);
        if (item == null)
            return;
        
        M entityModel = getParentModel();
        if (!(entityModel instanceof HumanoidModel))
            return;
        
        HumanoidModel<?> model = (HumanoidModel<?>) entityModel;
        RenderType renderType = Sheets.cutoutBlockSheet();
        BlockState renderedState = item.getBlock().defaultBlockState().setValue(SpaceBacktankBlock.HORIZONTAL_FACING, Direction.SOUTH);
        SuperByteBuffer backtank = CachedBufferer.block(renderedState);
        
        ms.pushPose();
        
        model.body.translateAndRotate(ms);
        ms.translate(-1 / 2f, 10 / 16f, 1f);
        ms.scale(1, -1, -1);
        
        backtank.forEntityRender()
                .light(light)
                .renderInto(ms, buffer.getBuffer(renderType));
        ms.popPose();
    }
    public static void registerOnAll(EntityRenderDispatcher renderManager) {
        for (EntityRenderer<? extends Player> renderer : renderManager.getSkinMap().values())
            registerOn(renderer);
        for (EntityRenderer<?> renderer : renderManager.renderers.values())
            registerOn(renderer);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void registerOn(EntityRenderer<?> entityRenderer) {
        if (!(entityRenderer instanceof LivingEntityRenderer))
            return;
        LivingEntityRenderer<?, ?> livingRenderer = (LivingEntityRenderer<?, ?>) entityRenderer;
        if (!(livingRenderer.getModel() instanceof HumanoidModel))
            return;
        SpaceBacktankArmorLayer<?, ?> layer = new SpaceBacktankArmorLayer<>(livingRenderer);
        livingRenderer.addLayer((SpaceBacktankArmorLayer) layer);
    }
}
