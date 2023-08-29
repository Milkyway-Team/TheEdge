package com.pouffydev.the_edge;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;

public class TEKeys {
    public static final KeyMapping boost = new KeyMapping("key.the_edge.boost", InputConstants.KEY_GRAVE, "key.category.the_edge");
    
    public static void register() {
        ClientRegistry.registerKeyBinding(boost);
    }
}
