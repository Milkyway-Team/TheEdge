package com.pouffydev.the_edge;

import com.pouffydev.the_edge.content.equipment.spacesuit.SpaceBacktankBlockEntity;
import com.pouffydev.the_edge.content.equipment.spacesuit.SpaceBacktankInstance;
import com.pouffydev.the_edge.content.equipment.spacesuit.SpaceBacktankRenderer;
import com.simibubi.create.AllBlocks;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.simibubi.create.Create.REGISTRATE;

public class TEBlockEntities {
    
    public static final BlockEntityEntry<SpaceBacktankBlockEntity> BACKTANK = REGISTRATE
            .blockEntity("space_backtank", SpaceBacktankBlockEntity::new)
            .instance(() -> SpaceBacktankInstance::new)
            .validBlocks(TEBlocks.platinumBacktank)
            .renderer(() -> SpaceBacktankRenderer::new)
            .register();
    
    public static void register() {}
}
