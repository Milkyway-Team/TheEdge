package com.pouffydev.the_edge;

import com.pouffydev.the_edge.content.block.dungeons.completion_monolith.CompletionMonolithBlock;
import com.pouffydev.the_edge.content.block.dungeons.heart_door.HeartDoorBlock;
import com.pouffydev.the_edge.content.block.dungeons.heart_door.LockedHeartDoorBlock;
import com.pouffydev.the_edge.content.block.kinetics.gateway.attuner.Attuner;
import com.pouffydev.the_edge.content.block.kinetics.groundbreaker.GroundbreakerCore;
import com.pouffydev.the_edge.content.block.kinetics.cryo_fan.CryoFan;
import com.pouffydev.the_edge.content.block.kinetics.groundbreaker.drill.GBDrillBlock;
import com.pouffydev.the_edge.content.block.kinetics.groundbreaker.gearbox.GBGearbox;
import com.pouffydev.the_edge.content.block.kinetics.groundbreaker.json.ExposedBlock;
import com.pouffydev.the_edge.content.block.foliage.*;
import com.pouffydev.the_edge.content.equipment.spacesuit.SpaceBacktankBlock;
import com.pouffydev.the_edge.foundation.TETags;
import com.pouffydev.the_edge.foundation.client.TEBlockstateGen;
import com.pouffydev.the_edge.foundation.client.TEBuilderTransformers;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.*;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraftforge.common.Tags;

import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
import static com.simibubi.create.foundation.data.TagGen.tagBlockAndItem;

public class TEBlocks {

    private static final CreateRegistrate REGISTRATE = TheEdge.registrate().creativeModeTab(
            () -> TheEdge.itemGroup
    );
    public static final BlockEntry<Block> EDGESTONE = REGISTRATE.block("edgestone", Block::new)
            .initialProperties(() -> Blocks.STONE)
            .properties(p -> p
                    .color(MaterialColor.GLOW_LICHEN)
                    .sound(SoundType.CALCITE)
            )
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.naturalStoneTypeBlock("edgestone"))
            .lang("Edgestone")
            .item()
            .model((c, p) -> p.cubeAll(c.getName(), p.modLoc("block/palettes/stone_types/natural/edgestone_3")))
            .build()
            .register();
    public static final BlockEntry<Block> EDGESLATE = REGISTRATE.block("edgeslate", Block::new)
            .initialProperties(() -> Blocks.STONE)
            .properties(p -> p
                    .color(MaterialColor.GLOW_LICHEN)
                    .sound(SoundType.ANCIENT_DEBRIS)
            )
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.naturalStoneTypeBlock("edgeslate"))
            .lang("Edgeslate")
            .item()
            .model((c, p) -> p.cubeAll(c.getName(), p.modLoc("block/palettes/stone_types/natural/edgeslate_2")))
            .build()
            .register();
    public static final BlockEntry<Block> STARFILLED_EDGESTONE = REGISTRATE.block("starfilled_edgestone", Block::new)
            .initialProperties(() -> Blocks.STONE)
            .properties(p -> p
                    .color(MaterialColor.GLOW_LICHEN)
                    .sound(SoundType.CALCITE)
                    .lightLevel(s -> 15)
            )
            .transform(pickaxeOnly())
            .simpleItem()
            .lang("Starfilled Edgestone")
            .register();
    public static final BlockEntry<Block> GLITCHED_NYLIUM = REGISTRATE.block("glitched_nylium", Block::new)
            .initialProperties(() -> Blocks.WARPED_NYLIUM)
            .properties(p -> p
                    .color(MaterialColor.COLOR_BLACK)
                    .sound(SoundType.NYLIUM)
            )
            .transform(pickaxeOnly())
            .blockstate(TEBlockstateGen.variantNyliumBlock("glitched_nylium", "edgestone"))
            .tag(TETags.AllBlockTags.supportsForestPlants.tag)
            .lang("Glitched Nylium")
            .item()
            .model((c, p) -> p.cubeBottomTop(c.getName(), p.modLoc("block/glitched_nylium_side_2"), p.modLoc("block/palettes/stone_types/natural/edgestone_2"), p.modLoc("block/glitched_nylium_top")))
            .build()
            .register();
    public static final BlockEntry<Block> frostedNylium = REGISTRATE.block("frosted_nylium", Block::new)
            .initialProperties(GLITCHED_NYLIUM)
            .properties(p -> p
                    .color(MaterialColor.COLOR_LIGHT_BLUE)
                    .sound(SoundType.SNOW)
            )
            .transform(pickaxeOnly())
            .blockstate(TEBlockstateGen.variantNyliumBlock("frosted_nylium", "edgestone"))
            .tag(TETags.AllBlockTags.supportsFrostedPlants.tag)
            .lang("Frosted Nylium")
            .item()
            .model((c, p) -> p.cubeBottomTop(c.getName(), p.modLoc("block/frosted_nylium_side_2"), p.modLoc("block/palettes/stone_types/natural/edgestone_2"), p.modLoc("block/frosted_nylium_top")))
            .build()
            .register();
    public static final BlockEntry<SpaceBacktankBlock> platinumBacktank =
            REGISTRATE.block("platinum_backtank", SpaceBacktankBlock::new)
                    .initialProperties(SharedProperties::netheriteMetal)
                    .transform(BuilderTransformers.backtank(TEItems.platinumBacktank::get))
                    .register();
    
    public static final BlockEntry<Block> deepslatePlatinumOre = REGISTRATE.block("deepslate_platinum_ore", Block::new)
            .initialProperties(() -> Blocks.DEEPSLATE_GOLD_ORE)
            .properties(p -> p.color(MaterialColor.METAL))
            .properties(p -> p.requiresCorrectToolForDrops()
                    .sound(SoundType.DEEPSLATE))
            .transform(TagGen.pickaxeOnly())
            .loot((lt, b) -> lt.add(b,
                    RegistrateBlockLootTables.createSilkTouchDispatchTable(b,
                            RegistrateBlockLootTables.applyExplosionDecay(b, LootItem.lootTableItem(TEItems.rawPlatinum.get())
                                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
            .tag(BlockTags.NEEDS_DIAMOND_TOOL)
            .tag(Tags.Blocks.ORES)
            .transform(tagBlockAndItem("ores/platinum", "ores_in_ground/deepslate"))
            .tag(Tags.Items.ORES)
            .build()
            .simpleItem()
            .register();
    
    public static final BlockEntry<Block> glacioniteOre = REGISTRATE.block("glacionite_ore", Block::new)
            .initialProperties(EDGESTONE)
            .properties(p -> p.color(MaterialColor.ICE))
            .properties(p -> p.requiresCorrectToolForDrops()
                    .sound(SoundType.CALCITE))
            .transform(TagGen.pickaxeOnly())
            .loot((lt, b) -> lt.add(b,
                    RegistrateBlockLootTables.createSilkTouchDispatchTable(b,
                            RegistrateBlockLootTables.applyExplosionDecay(b, LootItem.lootTableItem(TEItems.rawGlacionite.get())
                                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
            .tag(TETags.AllBlockTags.needsPlatinumTool.tag)
            .tag(Tags.Blocks.ORES)
            .transform(tagBlockAndItem("ores/glacionite", "ores_in_ground/edgestone"))
            .tag(Tags.Items.ORES)
            .build()
            .simpleItem()
            .register();
    
    public static final BlockEntry<Block> edgeslateGlacioniteOre = REGISTRATE.block("edgeslate_glacionite_ore", Block::new)
            .initialProperties(EDGESLATE)
            .properties(p -> p.color(MaterialColor.ICE))
            .properties(p -> p.requiresCorrectToolForDrops()
                    .sound(SoundType.ANCIENT_DEBRIS))
            .transform(TagGen.pickaxeOnly())
            .loot((lt, b) -> lt.add(b,
                    RegistrateBlockLootTables.createSilkTouchDispatchTable(b,
                            RegistrateBlockLootTables.applyExplosionDecay(b, LootItem.lootTableItem(TEItems.rawGlacionite.get())
                                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
            .tag(TETags.AllBlockTags.needsPlatinumTool.tag)
            .tag(Tags.Blocks.ORES)
            .transform(tagBlockAndItem("ores/glacionite", "ores_in_ground/edgeslate"))
            .tag(Tags.Items.ORES)
            .build()
            .simpleItem()
            .register();
    
    public static final BlockEntry<Block> edgeslateBismuthOre = REGISTRATE.block("edgeslate_ancient_bismuth_ore", Block::new)
            .initialProperties(EDGESLATE)
            .properties(p -> p.color(MaterialColor.COLOR_BROWN))
            .properties(p -> p.requiresCorrectToolForDrops()
                    .sound(SoundType.ANCIENT_DEBRIS))
            .transform(TagGen.pickaxeOnly())
            .loot((lt, b) -> lt.add(b,
                    RegistrateBlockLootTables.createSilkTouchDispatchTable(b,
                            RegistrateBlockLootTables.applyExplosionDecay(b, LootItem.lootTableItem(TEItems.rawBismuth.get())
                                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
            .tag(TETags.AllBlockTags.needsPlatinumTool.tag)
            .tag(Tags.Blocks.ORES)
            .transform(tagBlockAndItem("ores/bismuth", "ores_in_ground/edgeslate"))
            .tag(Tags.Items.ORES)
            .build()
            .simpleItem()
            .register();
    
    //Plants
    public static final BlockEntry<DoubleEdgePlantBlock> tallSpinecurl = REGISTRATE.block("tall_spinecurl", (props) ->  new DoubleEdgePlantBlock(props, TETags.AllBlockTags.supportsForestPlants.tag))
            .initialProperties(() -> Blocks.TALL_GRASS)
            .properties(p -> p
                    .sound(SoundType.WART_BLOCK)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
            )
            .blockstate(TEBlockstateGen.existingDoubleFoliage())
            .lang("Tall Spinecurl")
            .item()
            .model(AssetLookup.customBlockItemModel("spinecurl/item_tall"))
            .build()
            .register();
    
    public static final BlockEntry<SpineCurlBlock> spinecurl = REGISTRATE.block("spinecurl", SpineCurlBlock::new)
            .initialProperties(() -> Blocks.GRASS)
            .properties(p -> p
                    .sound(SoundType.WART_BLOCK)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
            )
            .blockstate(TEBlockstateGen.spineCurl())
            .lang("Spinecurl")
            .item()
            .model(AssetLookup.customBlockItemModel("spinecurl/item"))
            .build()
            .register();
    
    public static final BlockEntry<LevivineBlock> levivine = REGISTRATE.block("levivine", LevivineBlock::new)
            .initialProperties(() -> Blocks.TALL_GRASS)
            .properties(p -> p
                    .sound(SoundType.WART_BLOCK)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
            )
            .blockstate(TEBlockstateGen.existingDoubleFoliage())
            .lang("Levivine")
            .item()
            .model(AssetLookup.customBlockItemModel("levivine/item"))
            .build()
            .register();
    public static final BlockEntry<EdgeBushBlock> frostSpike = REGISTRATE.block("frost_spike", (props) ->  new EdgeBushBlock(props, TETags.AllBlockTags.supportsFrostedPlants.tag))
            .initialProperties(() -> Blocks.TALL_GRASS)
            .properties(p -> p
                    .sound(SoundType.GLASS)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
            )
            .blockstate(TEBlockstateGen.existingEdgeBush())
            .lang("Frost Spike")
            .item()
            .model(AssetLookup.customBlockItemModel("frost_spike/item"))
            .build()
            .register();
    
    public static final BlockEntry<BearTrapBlock> chompweed = REGISTRATE.block("chompweed", BearTrapBlock::new)
            .initialProperties(() -> Blocks.TALL_GRASS)
            .properties(p -> p
                    .sound(SoundType.WART_BLOCK)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
            )
            .blockstate(TEBlockstateGen.bearTrap())
            .lang("Chompweed")
            .item()
            .model(AssetLookup.customBlockItemModel("chompweed/triggered_false"))
            .build()
            .register();
    
    public static final BlockEntry<CryoFan> areaHeater = REGISTRATE.block("cryo_fan", CryoFan::new)
            .initialProperties(deepslatePlatinumOre)
            .properties(p -> p
                    .sound(SoundType.METAL)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
            )
            .transform(BlockStressDefaults.setImpact(8.0))
            .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
            .lang("Area Heater")
            .item()
            .model(AssetLookup.customBlockItemModel("cryo_fan/item"))
            .build()
            .register();
    
    public static final BlockEntry<GroundbreakerCore> groundbreakerCore = REGISTRATE.block("groundbreaker_core", GroundbreakerCore::new)
            .initialProperties(deepslatePlatinumOre)
            .properties(p -> p
                    .sound(SoundType.METAL)
            )
            .transform(BlockStressDefaults.setImpact(8.0))
            .blockstate(TEBlockstateGen.cubeBottomTop("groundbreaker_core", "bismuth_casing"))
            .lang("Groundbreaker Core")
            .item()
            .build()
            .register();
    
    public static final BlockEntry<GBDrillBlock> groundbreakerDrill = REGISTRATE.block("groundbreaker_drill", GBDrillBlock::new)
            .initialProperties(deepslatePlatinumOre)
            .properties(p -> p
                    .sound(SoundType.METAL)
                    .noOcclusion()
            )
            .transform(BlockStressDefaults.setImpact(12.0))
            .blockstate(TEBlockstateGen.transparentBaseModel("groundbreaker/drill_block"))
            .lang("Groundbreaker Drill")
            .item()
            .model(AssetLookup.customBlockItemModel("groundbreaker/drill"))
            .build()
            .register();
    public static final BlockEntry<GBGearbox> groundbreakerGearbox = REGISTRATE.block("groundbreaker_gearbox", GBGearbox::new)
            .initialProperties(deepslatePlatinumOre)
            .properties(p -> p
                    .sound(SoundType.METAL)
                    .noOcclusion()
            )
            .transform(BlockStressDefaults.setImpact(12.0))
            .blockstate(TEBlockstateGen.transparentBaseModel("groundbreaker/gearbox"))
            .lang("Groundbreaker Gearbox")
            .item()
            .model(AssetLookup.customBlockItemModel("groundbreaker/gearbox_item"))
            .build()
            .register();
    
    public static final BlockEntry<Attuner> attuner = REGISTRATE.block("attuner", Attuner::new)
            .initialProperties(deepslatePlatinumOre)
            .properties(p -> p
                    .sound(SoundType.METAL)
                    .noOcclusion()
            )
            .transform(BlockStressDefaults.setImpact(12.0))
            .blockstate(TEBlockstateGen.transparentBaseModel("attuner/block"))
            .lang("Edge Attuner")
            .item()
            .model(AssetLookup.customBlockItemModel("attuner/item"))
            .build()
            .register();
    public static final BlockEntry<ExposedBlock> exposedStone = REGISTRATE.block("exposed_stone", ExposedBlock::new)
            .initialProperties(Material.STONE)
            .properties(p -> p
                    .sound(SoundType.STONE)
            )
            .blockstate(TEBlockstateGen.exposedBlock("stone", Blocks.STONE))
            .lang("Exposed Stone")
            .item()
            .build()
            .register();
    public static final BlockEntry<ExposedBlock> exposedDeepslate = REGISTRATE.block("exposed_deepslate", ExposedBlock::new)
            .initialProperties(Material.STONE)
            .properties(p -> p
                    .sound(SoundType.DEEPSLATE)
            )
            .blockstate(TEBlockstateGen.exposedBlock("deepslate", Blocks.DEEPSLATE))
            .lang("Exposed Deepslate")
            .item()
            .build()
            .register();
    public static final BlockEntry<Block> glitchedEdgestone = REGISTRATE.block("glitched_edgestone", Block::new)
            .initialProperties(() -> Blocks.STONE)
            .properties(p -> p
                    .color(MaterialColor.GLOW_LICHEN)
                    .sound(SoundType.CALCITE)
            )
            .transform(pickaxeOnly())
            .lang("Glitched Edgestone")
            .transform(TEBuilderTransformers.ctBlock(() -> TESpriteShifts.glitchedEdgestone))
            .register();
    
    public static final BlockEntry<HeartDoorBlock> heartDoor = REGISTRATE.block("heart_door", HeartDoorBlock::new)
            .initialProperties(() -> Blocks.BEDROCK)
            .properties(p -> p
                    .sound(SoundType.GLASS)
                    .strength(50.0F, 1200.0F)
                    .noOcclusion()
            )
            .blockstate(TEBlockstateGen.heartDoor())
            .transform(TEBuilderTransformers.heartDoor())
            .lang("Heart Door")
            .item()
            .build()
            .register();
    public static final BlockEntry<LockedHeartDoorBlock> lockedHeartDoor = REGISTRATE.block("locked_heart_door", LockedHeartDoorBlock::new)
            .initialProperties(() -> Blocks.BEDROCK)
            .properties(p -> p
                    .sound(SoundType.GLASS)
                    .strength(50.0F, 1200.0F)
                    .noOcclusion()
            )
            .blockstate(TEBlockstateGen.lockedHeartDoor())
            .lang("Locked Heart Door")
            .item()
            .build()
            .register();
    
    public static final BlockEntry<CompletionMonolithBlock> completionMonolith = REGISTRATE.block("completion_monolith", CompletionMonolithBlock::new)
            .initialProperties(() -> Blocks.BEDROCK)
            .properties(p -> p
                    .sound(SoundType.STONE)
                    .strength(50.0F, 1200.0F)
                    .noOcclusion()
            )
            .blockstate(TEBlockstateGen.completionMonolith())
            .lang("Completion Monolith")
            .item(DoubleHighBlockItem::new)
            .build()
            .register();
    
    public static void register() {}
}
