package com.example.helditem;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = HeldItemTweaks.MODID, name = HeldItemTweaks.NAME, version = HeldItemTweaks.VERSION,
        clientSideOnly = true, acceptedMinecraftVersions = "[1.8.9]",
        guiFactory = "com.example.helditem.ConfigGuiFactory")
public class HeldItemTweaks {
    public static final String MODID = "kamikaze";
    public static final String NAME = "Kamikaze";
    public static final String VERSION = "1.0";

    public static Configuration config;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        ModConfig.sync();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ModelHandler());
        FMLCommonHandler.instance().bus().register(this);
        FMLCommonHandler.instance().bus().register(new KeyHandler());
        FMLCommonHandler.instance().bus().register(new BlockAnimHandler());
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (MODID.equals(event.modID)) {
            ModConfig.sync();
        }
    }
}
