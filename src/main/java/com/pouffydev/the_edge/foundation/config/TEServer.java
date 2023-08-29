package com.pouffydev.the_edge.foundation.config;

import com.pouffydev.the_edge.foundation.config.server.TEEquipment;
import com.pouffydev.the_edge.foundation.config.server.TEWorld;
import com.simibubi.create.foundation.config.ConfigBase;

public class TEServer extends ConfigBase {
    
    public final TEWorld world = nested(0, TEWorld::new, Comments.world);
    public final TEEquipment equipment = nested(1, TEEquipment::new, Comments.equipment);
    
    
    @Override
    public String getName() {
        return "server";
    }
    private static class Comments {
        static String kinetics = "Parameters and abilities of The Edge's kinetic machinery";
        static String equipment = "Equipment and gadgets used to traverse The Edge";
        static String world = "Control over the behavior of The Edge";
    }
}
