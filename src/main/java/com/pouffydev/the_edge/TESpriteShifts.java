package com.pouffydev.the_edge;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;
import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import com.simibubi.create.foundation.block.render.SpriteShifter;

public class TESpriteShifts {
    
    public static final CTSpriteShiftEntry glitchedEdgestone = omni("glitched_edgestone");
    public static final CTSpriteShiftEntry heartDoor = omni("heart_door");
    public static final CTSpriteShiftEntry heartDoorVanished = omni("starjump");
    
    private static CTSpriteShiftEntry omni(String name) {
        return getCT(AllCTTypes.OMNIDIRECTIONAL, name);
    }
    
    private static CTSpriteShiftEntry horizontal(String name) {
        return getCT(AllCTTypes.HORIZONTAL, name);
    }
    
    private static CTSpriteShiftEntry vertical(String name) {
        return getCT(AllCTTypes.VERTICAL, name);
    }
    
    private static SpriteShiftEntry get(String originalLocation, String targetLocation) {
        return SpriteShifter.get(TheEdge.asResource(originalLocation), TheEdge.asResource(targetLocation));
    }
    
    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName, String connectedTextureName) {
        return CTSpriteShifter.getCT(type, TheEdge.asResource("block/" + blockTextureName),
                TheEdge.asResource("block/" + connectedTextureName + "_connected"));
    }
    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName) {
        return getCT(type, blockTextureName, blockTextureName);
    }
}
