package com.example.helditem;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraftforge.client.model.IFlexibleBakedModel;
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

    public VertexFormat getFormat() {
        return ((IFlexibleBakedModel) base).getFormat();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return base.getItemCameraTransforms();
    }

    @Override
    public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType type) {
        Pair<? extends IFlexibleBakedModel, Matrix4f> p = ((IPerspectiveAwareModel) base).handlePerspective(type);

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

        Matrix4f m = new Matrix4f();
        if (p.getRight() != null) {
            m.set(p.getRight());
        } else {
            m.setIdentity();
        }

        if (type == ItemCameraTransforms.TransformType.FIRST_PERSON && isBlocking()) {
            // Vanilla applies the block pose (B) before the item matrix (M). Re-express
            // the offset so the sword shifts by the same amount in hand space as when
            // it is just held:  E' = C^-1 * E * C  with  C = M^-1 * B * M
            try {
                Matrix4f mInv = new Matrix4f(m);
                mInv.invert();
                Matrix4f c = new Matrix4f(mInv);
                c.mul(blockPose());
                c.mul(m);
                Matrix4f cInv = new Matrix4f(c);
                cInv.invert();
                Matrix4f adjusted = new Matrix4f(cInv);
                adjusted.mul(extra);
                adjusted.mul(c);
                extra = adjusted;
            } catch (RuntimeException e) {
                // singular matrix: fall back to the plain offset
            }
        }

        Matrix4f result = new Matrix4f(m);
        result.mul(extra);
        return Pair.of(p.getLeft(), result);
    }

    /** Vanilla 1.8.9 sword block pose (ItemRenderer.doBlockTransformations). */
    private static Matrix4f blockPose() {
        Matrix4f b = new Matrix4f();
        b.setIdentity();
        b.setTranslation(new Vector3f(-0.5f, 0.2f, 0.0f));
        Matrix4f tmp = new Matrix4f();
        tmp.rotY((float) Math.toRadians(30.0));
        b.mul(tmp);
        tmp.rotX((float) Math.toRadians(-80.0));
        b.mul(tmp);
        tmp.rotY((float) Math.toRadians(60.0));
        b.mul(tmp);
        return b;
    }
}
