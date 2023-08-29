package com.pouffydev.the_edge.foundation.config;

import com.simibubi.create.foundation.config.ConfigBase;
import com.simibubi.create.infrastructure.config.CClient;

public class TEClient extends ConfigBase {
    public final ConfigBool creativeSearchTab = b(true, "creativeSearchTab",
            Comments.creativeSearchTab);
    public final ConfigBool useCustomTabTexture = b(true, "useCustomTabTexture",
            Comments.useCustomTabTexture);
    @Override
    public String getName() {
        return "client";
    }
    private static class Comments {
        static String creativeSearchTab = "Adds a search bar to The Edge's creative tab";
        static String useCustomTabTexture = "Uses a custom texture for The Edge's creative tab";
    }
}
