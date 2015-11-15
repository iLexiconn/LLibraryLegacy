package net.ilexiconn.llibrary.client.model.obj;

import net.minecraft.util.ResourceLocation;

public class ObjModelLoader implements IModelCustomLoader {
    private static final String[] types = {"obj"};

    public String getType() {
        return "OBJ model";
    }

    public String[] getSuffixes() {
        return types;
    }

    public IModelCustom loadInstance(ResourceLocation resource) throws ModelFormatException {
        return new WavefrontObject(resource);
    }
}