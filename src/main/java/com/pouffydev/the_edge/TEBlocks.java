package com.pouffydev.the_edge;

import com.pouffydev.the_edge.content.block.foliage.BearTrapBlock;
import com.pouffydev.the_edge.content.block.foliage.EdgeBushBlock;
import com.pouffydev.the_edge.content.block.foliage.LevivineBlock;
import com.pouffydev.the_edge.content.block.foliage.SpineCurlBlock;
import com.pouffydev.the_edge.content.equipment.spacesuit.SpaceBacktankBlock;
import com.pouffydev.the_edge.foundation.TETags;
import com.pouffydev.the_edge.foundation.client.TEBlockstateGen;
import com.simibubi.create.foundation.data.*;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SoundType;
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
            .blockstate(BlockStateGen.simpleCubeAll("glitched_nylium_top"))
            .lang("Glitched Nylium")
            .item()
            .build()
            .register();
    public static final BlockEntry<Block> frostedNylium = REGISTRATE.block("frosted_nylium", Block::new)
            .initialProperties(GLITCHED_NYLIUM)
            .properties(p -> p
                    .color(MaterialColor.COLOR_LIGHT_BLUE)
                    .sound(SoundType.SNOW)
            )
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.simpleCubeAll("frosted_nylium_top"))
            .lang("Frosted Nylium")
            .item()
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
    public static final BlockEntry<DoublePlantBlock> tallSpinecurl = REGISTRATE.block("tall_spinecurl", DoublePlantBlock::new)
            .initialProperties(() -> Blocks.TALL_GRASS)
            .properties(p -> p
                    .sound(SoundType.WART_BLOCK)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .lightLevel(s -> 15)
            )
            .blockstate(TEBlockstateGen.existingDoubleFoliage())
            .lang("Tall Spinecurl")
            .simpleItem()
            .register();
    
    public static final BlockEntry<SpineCurlBlock> spinecurl = REGISTRATE.block("spinecurl", SpineCurlBlock::new)
            .initialProperties(() -> Blocks.GRASS)
            .properties(p -> p
                    .sound(SoundType.WART_BLOCK)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .lightLevel(s -> 15)
            )
            .blockstate(TEBlockstateGen.spineCurl())
            .lang("Spinecurl")
            .simpleItem()
            .register();
    
    public static final BlockEntry<LevivineBlock> levivine = REGISTRATE.block("levivine", LevivineBlock::new)
            .initialProperties(() -> Blocks.TALL_GRASS)
            .properties(p -> p
                    .sound(SoundType.WART_BLOCK)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .lightLevel(s -> 15)
            )
            .blockstate(TEBlockstateGen.existingDoubleFoliage())
            .lang("Levivine")
            .simpleItem()
            .register();
    public static final BlockEntry<EdgeBushBlock> frostSpike = REGISTRATE.block("frost_spike", (props) ->  new EdgeBushBlock(props, TETags.AllBlockTags.supportsFrostedPlants.tag))
            .initialProperties(() -> Blocks.TALL_GRASS)
            .properties(p -> p
                    .sound(SoundType.GLASS)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .lightLevel(s -> 15)
            )
            .blockstate(TEBlockstateGen.existingEdgeBush())
            .lang("Frost Spike")
            .simpleItem()
            .register();
    
    public static final BlockEntry<BearTrapBlock> chompweed = REGISTRATE.block("chompweed", BearTrapBlock::new)
            .initialProperties(() -> Blocks.TALL_GRASS)
            .properties(p -> p
                    .sound(SoundType.WART_BLOCK)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .lightLevel(s -> 15)
            )
            .blockstate(TEBlockstateGen.bearTrap())
            .lang("Chompweed")
            .simpleItem()
            .register();
    
    
    public static void register() {}
}
