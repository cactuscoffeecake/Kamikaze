package com.example.helditem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import org.lwjgl.input.Keyboard;

public class KeyHandler {
    private final KeyBinding openMenu = new KeyBinding("Open Kamikaze menu", Keyboard.KEY_RSHIFT, "Kamikaze");

    public KeyHandler() {
        ClientRegistry.registerKeyBinding(openMenu);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && openMenu.isPressed()) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.currentScreen == null) {
                mc.displayGuiScreen(new ModGuiConfig(null));
            }
        }
    }
}
