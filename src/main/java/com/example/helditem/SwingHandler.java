package com.example.helditem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Normal vanilla swing shape, just faster (like the swing speed option in
 * custom-animation mods). Each frame we rescale how far through the swing we
 * are so it finishes in "swingTicks" instead of the vanilla length.
 */
public class SwingHandler {

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            return;
        }
        EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
        if (p == null || !p.isSwingInProgress) {
            return;
        }
        int target = ModConfig.swingTicks;
        if (target >= vanillaEnd(p)) {
            return; // can't make it longer than the game's own swing, so leave it
        }
        float elapsed = Math.max(0.0f, p.swingProgressInt + event.renderTickTime);
        float q = Math.min(1.0f, elapsed / (float) target);
        p.prevSwingProgress = q;
        p.swingProgress = q;
    }

    private static int vanillaEnd(EntityPlayerSP p) {
        if (p.isPotionActive(Potion.digSpeed)) {
            return 6 - (1 + p.getActivePotionEffect(Potion.digSpeed).getAmplifier());
        }
        if (p.isPotionActive(Potion.digSlowdown)) {
            return 6 + (1 + p.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2;
        }
        return 6;
    }
}
