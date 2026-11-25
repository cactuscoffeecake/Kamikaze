package com.example.helditem;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModelHandler {

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        IRegistry<ModelResourceLocation, IBakedModel> registry = event.modelRegistry;
        List<ModelResourceLocation> keys = new ArrayList<ModelResourceLocation>(registry.getKeys());
        for (ModelResourceLocation key : keys) {
            if (!"inventory".equals(key.getVariant())) {
                continue; // only item models
            }
            IBakedModel model = registry.getObject(key);
            if (model != null && !(model instanceof TweakedModel)) {
                registry.putObject(key, new TweakedModel(model));
            }
        }
    }
}
