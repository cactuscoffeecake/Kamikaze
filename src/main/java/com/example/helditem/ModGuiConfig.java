package com.example.helditem;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.common.config.ConfigElement;

public class ModGuiConfig extends GuiConfig {
    public ModGuiConfig(GuiScreen parent) {
        super(parent, elements(), HeldItemTweaks.MODID, false, false, "Held Item Tweaks");
    }

    private static List<IConfigElement> elements() {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        list.add(new ConfigElement(HeldItemTweaks.config.getCategory(ModConfig.FIRST)));
        list.add(new ConfigElement(HeldItemTweaks.config.getCategory(ModConfig.THIRD)));
        return list;
    }
}
