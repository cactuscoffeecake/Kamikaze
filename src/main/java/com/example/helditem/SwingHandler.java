package com.example.helditem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Controls the first-person swing with two settings:
 *  - swingTicks: how long the swing lasts (6 = vanilla)
 *  - swingAmount: how far the item moves (1.0 = vanilla)
 *
 * The renderer derives its rotation/translation from the player's swingProgress.
 * Vanilla's main motion is sin(sqrt(p) * PI). We pick p so that this motion is a
 * smooth bump of height "swingAmount" over the swing, instead of the full arc.
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
        int vanilla = vanillaEnd(p);
        int length = Math.min(ModConfig.swingTicks, vanilla);
        float amount = ModConfig.swingAmount;
        if (length >= vanilla && amount >= 0.999f) {
            return; // fully vanilla
        }

        float elapsed = Math.max(0.0f, p.swingProgressInt + event.renderTickTime);
        float q = Math.min(1.0f, elapsed / (float) length);

        float out = q;
        if (amount < 0.999f) {
            double bump = Math.max(0.0, amount) * Math.sin(q * Math.PI);
            double x = Math.asin(Math.min(1.0, bump)) / Math.PI;
            out = (float) (x * x);
        }
        p.prevSwingProgress = out;
        p.swingProgress = out;
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
