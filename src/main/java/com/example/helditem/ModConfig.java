package com.example.helditem;

import net.minecraftforge.common.config.Configuration;

public class ModConfig {
    public static final String FIRST = "firstperson";
    public static final String THIRD = "thirdperson";

    public static class Transform {
        public boolean enabled;
        public float scale;
        public float posX, posY, posZ;
        public float rotX, rotY, rotZ;
    }

    public static final Transform firstPerson = new Transform();
    public static final Transform thirdPerson = new Transform();

    public static void sync() {
        Configuration c = HeldItemTweaks.config;
        load(c, FIRST, firstPerson);
        load(c, THIRD, thirdPerson);
        if (c.hasChanged()) {
            c.save();
        }
    }

    private static void load(Configuration c, String cat, Transform t) {
        t.enabled = c.getBoolean("enabled", cat, true, "Apply these settings");
        t.scale = c.getFloat("scale", cat, 1.0f, 0.1f, 3.0f, "Size multiplier (1.0 = normal)");
        t.posX = c.getFloat("posX", cat, 0.0f, -1.0f, 1.0f, "Move left/right (blocks)");
        t.posY = c.getFloat("posY", cat, 0.0f, -1.0f, 1.0f, "Move up/down (blocks)");
        t.posZ = c.getFloat("posZ", cat, 0.0f, -1.0f, 1.0f, "Move forward/back (blocks)");
        t.rotX = c.getFloat("rotX", cat, 0.0f, -180.0f, 180.0f, "Rotation around X axis (degrees)");
        t.rotY = c.getFloat("rotY", cat, 0.0f, -180.0f, 180.0f, "Rotation around Y axis (degrees)");
        t.rotZ = c.getFloat("rotZ", cat, 0.0f, -180.0f, 180.0f, "Rotation around Z axis (degrees)");
    }
}
