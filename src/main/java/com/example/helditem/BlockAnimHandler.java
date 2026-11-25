package com.example.helditem;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * Removes the item "equip" animation:
 *  - the sword dropping/popping up when you start blocking
 *  - the held item lowering and rising when you change hotbar slots
 *
 * The item renderer only swaps its item after the equip progress falls to ~0.
 * Every frame we tell it the current item/slot are already equipped and keep
 * the progress at 1, so it always shows the right item with no animation.
 */
public class BlockAnimHandler {
    private Field equipped;
    private Field prevEquipped;
    private Field itemToRender;
    private Field equippedSlot;
    private boolean initialized;
    private boolean failed;
    private boolean canSwap;

    private void init() {
        initialized = true;
        try {
            equipped = ReflectionHelper.findField(ItemRenderer.class, "field_78454_c", "equippedProgress");
            prevEquipped = ReflectionHelper.findField(ItemRenderer.class, "field_78451_d", "prevEquippedProgress");
        } catch (Throwable t) {
            failed = true;
            return;
        }
        try {
            itemToRender = ReflectionHelper.findField(ItemRenderer.class, "field_78453_b", "itemToRender");
            equippedSlot = ReflectionHelper.findField(ItemRenderer.class, "field_78450_g", "equippedItemSlot");
            canSwap = true;
        } catch (Throwable t) {
            canSwap = false; // only the blocking fix will work
        }
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.START || failed) {
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) {
            return;
        }
        if (!initialized) {
            init();
            if (failed) {
                return;
            }
        }
        try {
            ItemRenderer r = mc.getItemRenderer();
            if (canSwap) {
                ItemStack current = mc.thePlayer.inventory.getCurrentItem();
                itemToRender.set(r, current);
                equippedSlot.setInt(r, mc.thePlayer.inventory.currentItem);
            } else if (!mc.thePlayer.isBlocking()) {
                return;
            }
            equipped.setFloat(r, 1.0F);
            prevEquipped.setFloat(r, 1.0F);
        } catch (Throwable t) {
            failed = true;
        }
    }
}
