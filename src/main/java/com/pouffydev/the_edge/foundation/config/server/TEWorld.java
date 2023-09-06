package com.pouffydev.the_edge.foundation.config.server;

import com.simibubi.create.foundation.config.ConfigBase;

public class TEWorld extends ConfigBase {
    
    public final ConfigBool doEdgeOxygen = b(true, "doEdgeOxygen", Comments.doEdgeOxygen);
    public final ConfigInt oxygenDamage = i(4, 0, "oxygenDamage", Comments.oxygenDamage);
    public final ConfigBool doEdgeFreezing = b(true, "doEdgeFreezing", Comments.doEdgeFreezing);
    public final ConfigInt freezingDamage = i(1, 0, "freezingDamage", Comments.freezingDamage);
    public final ConfigBool doDepthPressure = b(true, "doDepthPressure", Comments.doDepthPressure);
    public final ConfigInt depthPressureDamage = i(1, 0, "depthPressureDamage", Comments.depthPressureDamage);
    public final ConfigInt platinumOreVeinSize = i(4, 0, "platinumOreVeinSize", Comments.platinumOreVeinSize);
    public final ConfigInt platinumOreVeinsPerChunk = i(1, 0, "platinumOreVeinsPerChunk", Comments.platinumOreVeinsPerChunk);
    public final ConfigInt glacioniteOreVeinSize = i(8, 0, "glacioniteOreVeinSize", Comments.glacioniteOreVeinSize);
    public final ConfigInt glacioniteOreVeinsPerChunk = i(3, 0, "glacioniteOreVeinsPerChunk", Comments.glacioniteOreVeinsPerChunk);
    public final ConfigInt bismuthOreVeinSize = i(4, 0, "bismuthOreVeinSize", Comments.bismuthOreVeinSize);
    public final ConfigInt bismuthOreVeinsPerChunk = i(1, 0, "bismuthOreVeinsPerChunk", Comments.bismuthOreVeinsPerChunk);
    @Override
    public String getName() {
        return "world";
    }
    private static class Comments {
        static String doEdgeOxygen = "Whether or not The Edge should have oxygen";
        static String oxygenDamage = "The amount of damage done to the player per tick when they are in The Edge without oxygen";
        static String platinumOreVeinSize = "The size of platinum ore veins in the Overworld";
        static String platinumOreVeinsPerChunk = "The number of platinum ore veins per chunk in the Overworld";
        static String glacioniteOreVeinSize = "The size of glacionite ore veins in The Edge";
        static String glacioniteOreVeinsPerChunk = "The number of glacionite ore veins per chunk in The Edge";
        static String bismuthOreVeinSize = "The size of bismuth ore veins in The Edge";
        static String bismuthOreVeinsPerChunk = "The number of bismuth ore veins per chunk in The Edge";
        static String doEdgeFreezing = "Whether or not cold biomes in The Edge should freeze the player";
        static String freezingDamage = "The amount of damage done to the player per tick when they are freezing in The Edge";
        static String doDepthPressure = "Whether or not the player should gain debuffs when they are too deep in The Edge";
        static String depthPressureDamage = "The amount of damage done to the player per tick when they are too deep in The Edge";
        
    }
}
