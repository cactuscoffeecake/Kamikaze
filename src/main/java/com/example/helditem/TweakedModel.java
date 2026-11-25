package com.example.helditem;

import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;

import org.lwjgl.util.vector.Vector3f;

/**
 * Wraps a vanilla item model and adjusts its first/third person camera
 * transforms from the config every time they are requested, so changes in the
 * config menu apply instantly.
 */
public class TweakedModel implements IBakedModel {
    private final IBakedModel base;

    public TweakedModel(IBakedModel base) {
        this.base = base;
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing facing) {
        return base.getFaceQuads(facing);
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        return base.getGeneralQuads();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return base.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return base.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return base.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return base.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        ItemCameraTransforms o = base.getItemCameraTransforms();
        return new ItemCameraTransforms(
                adjust(o.thirdPerson, ModConfig.thirdPerson),
                adjust(o.firstPerson, ModConfig.firstPerson),
                o.head,
                o.gui,
                o.ground,
                o.fixed);
    }

    private static ItemTransformVec3f adjust(ItemTransformVec3f b, ModConfig.Transform t) {
        if (!t.enabled) {
            return b;
        }
        Vector3f rot = new Vector3f(b.rotation.x + t.rotX, b.rotation.y + t.rotY, b.rotation.z + t.rotZ);
        Vector3f pos = new Vector3f(b.translation.x + t.posX, b.translation.y + t.posY, b.translation.z + t.posZ);
        Vector3f scl = new Vector3f(b.scale.x * t.scale, b.scale.y * t.scale, b.scale.z * t.scale);
        return new ItemTransformVec3f(rot, pos, scl);
    }
}
