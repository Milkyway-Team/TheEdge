package com.pouffydev.the_edge.foundation;

import com.pouffydev.the_edge.TEItems;
import com.pouffydev.the_edge.TheEdge;
import com.pouffydev.the_edge.foundation.config.TEConfigs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModItemTab extends CreativeModeTab {
    private static final String PATH = "textures/gui/container/creative_inventory/";
    public ModItemTab(String label) {
        super(label);
        //this.setBackgroundImage(new ResourceLocation(TheEdge.ID, PATH + "tab_the_edge.png"));
        
    }
    public static boolean searchBar() {
        return TEConfigs.client().creativeSearchTab.get();
    }
    public static boolean customTexture() {
        return TEConfigs.client().useCustomTabTexture.get();
    }
    @Override
    public ItemStack makeIcon() {
        return TEItems.platinumIngot.get().getDefaultInstance();
    }
    @Override
    public boolean hasSearchBar() {
        return false;
    }
}
