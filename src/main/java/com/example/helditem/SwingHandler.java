package com.example.helditem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Turns the big arm swing into a small, quick flick.
 *
 * The renderer reads the player's swingProgress (0..1). Each frame we work out
 * the real progress from the swing tick counter and overwrite swingProgress with
 * a shorter, smaller curve. Vanilla's swing animation peaks at progress 0.25, so
 * we scale the peak so the visible motion is "swingAmount" times the normal size.
 */
public class SwingHandler {
    private static final float SPEED = 1.5f; // >1 = finishes sooner

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            return;
        }
        float amount = ModConfig.swingAmount;
        if (amount >= 0.999f) {
            return; // vanilla
        }
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP p = mc.thePlayer;
        if (p == null) {
            return;
        }
        float out = 0.0f;
        if (p.isSwingInProgress) {
            float raw = (p.swingProgressInt + event.renderTickTime) / (float) swingEnd(p);
            float q = Math.max(0.0f, Math.min(1.0f, raw * SPEED));
            float peak = (float) Math.asin(Math.max(0.0f, amount)) / (float) Math.PI;
            peak = peak * peak; // progress value whose vanilla motion has size "amount"
            out = peak * (float) Math.sin(q * Math.PI);
        }
        p.prevSwingProgress = out;
        p.swingProgress = out;
    }

    private static int swingEnd(EntityPlayerSP p) {
        if (p.isPotionActive(Potion.digSpeed)) {
            return 6 - (1 + p.getActivePotionEffect(Potion.digSpeed).getAmplifier());
        }
        if (p.isPotionActive(Potion.digSlowdown)) {
            return 6 + (1 + p.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2;
        }
        return 6;
    }
}
