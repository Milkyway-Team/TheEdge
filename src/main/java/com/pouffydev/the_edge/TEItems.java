package com.pouffydev.the_edge;

import com.pouffydev.the_edge.content.equipment.TEArmorMaterials;
import com.pouffydev.the_edge.content.equipment.spacesuit.SpaceArmorItem;
import com.pouffydev.the_edge.content.equipment.spacesuit.SpaceBacktankItem;
import com.pouffydev.the_edge.foundation.TETags;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import static com.pouffydev.the_edge.foundation.TETags.forgeItemTag;

public class TEItems {
    private static final CreateRegistrate REGISTRATE = TheEdge.registrate()
            .creativeModeTab(() -> TheEdge.itemGroup);
    static TagKey<Item> gems(String material) {
        return forgeItemTag("gems/" + material);
    }
    static TagKey<Item> gems() {
        return forgeItemTag("gems");
    }
    static TagKey<Item> rawMaterials(String material) {
        return forgeItemTag("raw_materials/" + material);
    }
    static TagKey<Item> rawMaterials() {
        return forgeItemTag("raw_materials");
    }
    static TagKey<Item> ingots(String material) {return forgeItemTag("ingots/" + material.replace("_ingot", ""));}
    static TagKey<Item> ingots() {
        return forgeItemTag("ingots");
    }
    static TagKey<Item> nuggets(String material) {
        return forgeItemTag("nuggets/" + material);
    }
    static TagKey<Item> nuggets() {return forgeItemTag("nuggets");}
    static TagKey<Item> plates(String material) {
        return forgeItemTag("plates/" + material);
    }
    static TagKey<Item> plates() {
        return forgeItemTag("plates");
    }
    static TagKey<Item> randomiumBlacklist() {
        return TETags.modItemTag("randomium", "blacklist");
    }
    
    private static ItemEntry<Item> sheet(String material) {return REGISTRATE.item(material + "_sheet", Item::new).properties(p->p.tab(TheEdge.itemGroup)).tag(plates(material)).tag(plates()).register();}
    private static ItemEntry<Item> ingot(String material) {
        return REGISTRATE.item(material + "_ingot", Item::new)
                .properties(p->p.tab(TheEdge.itemGroup))
                .tag(ingots(material))
                .tag(ingots())
                .register();
    }
    private static ItemEntry<Item> nugget(String material) {return REGISTRATE.item(material + "_nugget", Item::new).properties(p->p.tab(TheEdge.itemGroup)).tag(nuggets(material)).tag(nuggets()).register();}
    private static ItemEntry<Item> rawOre(String material) {return REGISTRATE.item("raw_" + material, Item::new).properties(p->p.tab(TheEdge.itemGroup)).tag(rawMaterials(material)).tag(rawMaterials()).register();}
    
    /**
     Ingots and Raw Ores
     */
    public static final ItemEntry<Item>
    platinumIngot = ingot("platinum"),
    rawPlatinum = rawOre("platinum"),
    glacioniteIngot = ingot("glacionite"),
    rawGlacionite = rawOre("glacionite"),
    bismuthIngot = ingot("ancient_bismuth"),
    rawBismuth = rawOre("ancient_bismuth");
    
    /**
    Sheets
     */
    public static final ItemEntry<Item>
    platinumSheet = sheet("platinum"),
    glacioniteSheet = sheet("glacionite"),
    bismuthSheet = sheet("ancient_bismuth");
    
    
    
    public static final ItemEntry<SpaceBacktankItem.SpaceBacktankBlockItem> platinumBacktankPlaceable = REGISTRATE
            .item("platinum_backtank_placeable",
                    p -> new SpaceBacktankItem.SpaceBacktankBlockItem(TEBlocks.platinumBacktank.get(), TEItems.platinumBacktank::get, p))
            .model((c, p) -> p.withExistingParent(c.getName(), p.mcLoc("item/barrier")))
            .register();
    public static final ItemEntry<? extends SpaceBacktankItem>
            platinumBacktank = REGISTRATE.item("platinum_backtank",p -> new SpaceBacktankItem.Layered(TEArmorMaterials.platinum, p, TheEdge.asResource("platinum_spacesuit"), platinumBacktankPlaceable))
            .model(AssetLookup.customGenericItemModel("_", "item"))
            .properties(Item.Properties::fireResistant)
            .tag(AllTags.AllItemTags.PRESSURIZED_AIR_SOURCES.tag)
			.tag(forgeItemTag("armors/chestplates"))
            .register();
    public static final ItemEntry<SpaceArmorItem> platinumHelmet = REGISTRATE.item("platinum_helmet", p -> new SpaceArmorItem(TEArmorMaterials.platinum, EquipmentSlot.HEAD, p, TheEdge.asResource("platinum_spacesuit")))
            .properties(Item.Properties::fireResistant)
            .tag(forgeItemTag("armors/helmets"))
            .register();
    
    public static final ItemEntry<SpaceArmorItem> platinumLeggings = REGISTRATE.item("platinum_leggings", p -> new SpaceArmorItem(TEArmorMaterials.platinum, EquipmentSlot.LEGS, p, TheEdge.asResource("platinum_spacesuit")))
            .properties(Item.Properties::fireResistant)
            .tag(forgeItemTag("armors/leggings"))
            .register();
    
    public static final ItemEntry<SpaceArmorItem> platinumBoots = REGISTRATE.item("platinum_boots", p -> new SpaceArmorItem(TEArmorMaterials.platinum, EquipmentSlot.FEET, p, TheEdge.asResource("platinum_spacesuit")))
            .properties(Item.Properties::fireResistant)
            .tag(forgeItemTag("armors/boots"))
            .register();
    public static void register() {}
}
