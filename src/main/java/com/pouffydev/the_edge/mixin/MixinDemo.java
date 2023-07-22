package com.pouffydev.the_edge.mixin;

import com.pouffydev.the_edge.TheEdge;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class MixinDemo {
    @Inject(method = "init", at = @At("TAIL"))
    private void exampleMixin(CallbackInfo ci) {
        TheEdge.LOGGER.info("Hello World From {}", TheEdge.ID);
    }
}
