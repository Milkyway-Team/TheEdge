package com.pouffydev.the_edge;

import com.pouffydev.the_edge.content.block.kinetics.cryo_fan.CryoFanBlockEntity;
import com.pouffydev.the_edge.content.block.kinetics.cryo_fan.CryoFanRenderer;
import com.pouffydev.the_edge.content.block.kinetics.gateway.attuner.AttunerBlockEntity;
import com.pouffydev.the_edge.content.block.kinetics.gateway.attuner.AttunerInstance;
import com.pouffydev.the_edge.content.block.kinetics.gateway.attuner.AttunerRenderer;
import com.pouffydev.the_edge.content.block.kinetics.groundbreaker.drill.GBDrillBlockEntity;
import com.pouffydev.the_edge.content.block.kinetics.groundbreaker.drill.GBDrillInstance;
import com.pouffydev.the_edge.content.block.kinetics.groundbreaker.drill.GBDrillRenderer;
import com.pouffydev.the_edge.content.block.kinetics.groundbreaker.gearbox.GBGearboxBlockEntity;
import com.pouffydev.the_edge.content.block.kinetics.groundbreaker.gearbox.GBGearboxInstance;
import com.pouffydev.the_edge.content.block.kinetics.groundbreaker.gearbox.GBGearboxRenderer;
import com.pouffydev.the_edge.content.equipment.spacesuit.SpaceBacktankBlockEntity;
import com.pouffydev.the_edge.content.equipment.spacesuit.SpaceBacktankInstance;
import com.pouffydev.the_edge.content.equipment.spacesuit.SpaceBacktankRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.simibubi.create.Create.REGISTRATE;

public class TEBlockEntities {
    
    public static final BlockEntityEntry<SpaceBacktankBlockEntity> BACKTANK = REGISTRATE
            .blockEntity("space_backtank", SpaceBacktankBlockEntity::new)
            .instance(() -> SpaceBacktankInstance::new)
            .validBlocks(TEBlocks.platinumBacktank)
            .renderer(() -> SpaceBacktankRenderer::new)
            .register();
    
    public static final BlockEntityEntry<CryoFanBlockEntity> AREA_HEATER = REGISTRATE
            .blockEntity("area_heater", CryoFanBlockEntity::new)
            .validBlocks(TEBlocks.areaHeater)
            .renderer(() -> CryoFanRenderer::new)
            .register();
    
    public static final BlockEntityEntry<GBDrillBlockEntity> GB_DRILL = REGISTRATE
            .blockEntity("gb_drill", GBDrillBlockEntity::new)
            .instance(() -> GBDrillInstance::new)
            .renderer(() -> GBDrillRenderer::new)
            .validBlocks(TEBlocks.groundbreakerDrill)
            .register();
    
    public static final BlockEntityEntry<GBGearboxBlockEntity> GB_GEARBOX = REGISTRATE
            .blockEntity("gb_gearbox", GBGearboxBlockEntity::new)
            .instance(() -> GBGearboxInstance::new)
            .validBlocks(TEBlocks.groundbreakerGearbox)
            .renderer(() -> GBGearboxRenderer::new)
            .register();
    public static final BlockEntityEntry<AttunerBlockEntity> ATTUNER = REGISTRATE
            .blockEntity("attuner", AttunerBlockEntity::new)
            .instance(() -> AttunerInstance::new)
            .validBlocks(TEBlocks.attuner)
            .renderer(() -> AttunerRenderer::new)
            .register();
    public static void register() {}
}
