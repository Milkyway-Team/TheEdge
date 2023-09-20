package com.pouffydev.the_edge.foundation.config.server;

import com.simibubi.create.foundation.config.ConfigBase;

public class TEEquipment extends ConfigBase {
    
    public final ConfigGroup upgradedSpaceGear = group(0, "upgradedSpaceGear", Comments.upgradedSpaceGear);
    public final ConfigFloat spaceSuitBoost = f(0.9f, 0, "spaceSuitBoost", Comments.spaceSuitBoost);
    @Override
    public String getName() {
        return "equipment";
    }
    private static class Comments {
        static String upgradedSpaceGear = "Configurations for upgraded space gear";
        static String spaceSuitBoost = "The power upgraded leggings can boost the player in space";
    }
}
