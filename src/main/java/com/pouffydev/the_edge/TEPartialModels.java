package com.pouffydev.the_edge;

import com.jozufozu.flywheel.core.PartialModel;

public class TEPartialModels {
    public static final PartialModel
            BACKTANK_AIR_FULL = block("platinum_backtank/air_bar_full"),
            BACKTANK_AIR_EMPTY = block("platinum_backtank/air_bar_empty"),
            BACKTANK_AIR_1 = block("platinum_backtank/air_bar_1"),
            BACKTANK_AIR_2 = block("platinum_backtank/air_bar_2"),
            BACKTANK_AIR_3 = block("platinum_backtank/air_bar_3"),
            BACKTANK_AIR_4 = block("platinum_backtank/air_bar_4"),
            BACKTANK_AIR_5 = block("platinum_backtank/air_bar_5"),
            BACKTANK_SHAFT = block("platinum_backtank/shaft"),
    GB_DRILL = block("groundbreaker/drill"),
    ATTUNER_AXIS = block("attuner/axis"),
    ATTUNER_RING = block("attuner/ring"),
            
            hd_dif0 = block("heart_door/dif0"),
            hd_dif1 = block("heart_door/dif1"),
            hd_dif2 = block("heart_door/dif2"),
            hd_dif3 = block("heart_door/dif3"),
            hd_dif4 = block("heart_door/dif4")
    
    
    ;
    
    public TEPartialModels() {
    }
    private static PartialModel block(String path) {
        return new PartialModel(TheEdge.asResource("block/" + path));
    }
    
    public static void register() {
    }
}
