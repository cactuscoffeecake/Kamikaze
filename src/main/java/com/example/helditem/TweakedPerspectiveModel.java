package com.example.helditem;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Forge item models (most vanilla items) supply their held transforms through
 * IPerspectiveAwareModel. A plain wrapper hides that and breaks the vanilla
 * position/rotation, so this one passes it through and adds our offsets.
 */
public class TweakedPerspectiveModel extends TweakedModel implements IPerspectiveAwareModel {

    public TweakedPerspectiveModel(IBakedModel base) {
        super(base);
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return base.getItemCameraTransforms();
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType type) {
        Pair<? extends IBakedModel, Matrix4f> p = ((IPerspectiveAwareModel) base).handlePerspective(type);

        ModConfig.Transform t = null;
        if (type == ItemCameraTransforms.TransformType.FIRST_PERSON) {
            t = ModConfig.firstPerson;
        } else if (type == ItemCameraTransforms.TransformType.THIRD_PERSON) {
            t = ModConfig.thirdPerson;
        }
        if (t == null || isDefault(t)) {
            return p;
        }

        Matrix4f extra = new Matrix4f();
        extra.setIdentity();
        extra.setTranslation(new Vector3f(t.posX, t.posY, t.posZ));
        Matrix4f tmp = new Matrix4f();
        tmp.rotY((float) Math.toRadians(t.rotY));
        extra.mul(tmp);
        tmp.rotX((float) Math.toRadians(t.rotX));
        extra.mul(tmp);
        tmp.rotZ((float) Math.toRadians(t.rotZ));
        extra.mul(tmp);
        tmp.setIdentity();
        tmp.m00 = t.scale;
        tmp.m11 = t.scale;
        tmp.m22 = t.scale;
        extra.mul(tmp);

        Matrix4f result = new Matrix4f();
        if (p.getRight() != null) {
            result.set(p.getRight());
        } else {
            result.setIdentity();
        }
        result.mul(extra);
        return Pair.of(p.getLeft(), result);
    }
}
