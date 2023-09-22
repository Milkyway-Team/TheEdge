package com.pouffydev.the_edge.content.block.dungeons.heart_door;

import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.util.StringRepresentable;
@SuppressWarnings("ALL")
public enum DungeonDifficulty implements StringRepresentable {
    EASY,
    NORMAL,
    HARD,
    EXTREME,
    IMPOSSIBLE
    ;
    public static DungeonDifficulty byIndex(int index) {
        return values()[index];
    }
    @Override
    public String getSerializedName() {
        return Lang.asId(name());
    }
}
