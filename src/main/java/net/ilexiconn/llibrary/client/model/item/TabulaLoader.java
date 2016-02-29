package net.ilexiconn.llibrary.client.model.item;

import com.google.common.collect.ImmutableList;
import com.google.gson.*;
import net.ilexiconn.llibrary.common.json.JsonHelper;
import net.ilexiconn.llibrary.common.json.container.JsonTabulaModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@SideOnly(Side.CLIENT)
public enum TabulaLoader implements ICustomModelLoader, JsonDeserializationContext {
    INSTANCE;

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ItemTransformVec3f.class, new ItemTransformVec3fDeserializer())
            .registerTypeAdapter(ItemCameraTransforms.class, new ItemCameraTransformsDeserializer())
            .create();

    private static final JsonParser PARSER = new JsonParser();

    private static final ModelBlock.Deserializer MODEL_BLOCK_DESERIALIZER = new ModelBlock.Deserializer();

    private final Set<String> enabledDomains = new HashSet<String>();

    private IResourceManager manager;

    public void addDomain(String domain) {
        enabledDomains.add(domain);
    }

    @Override
    public void onResourceManagerReload(IResourceManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return enabledDomains.contains(modelLocation.getResourceDomain()) && modelLocation.getResourcePath().endsWith(".tbl");
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws IOException {
        String modelPath = modelLocation.getResourcePath();
        modelPath = modelPath.substring(0, modelPath.lastIndexOf('.')) + ".json";
        IResource resource = manager.getResource(new ResourceLocation(modelLocation.getResourceDomain(), modelPath));
        InputStreamReader jsonStream = new InputStreamReader(resource.getInputStream());
        JsonElement json = PARSER.parse(jsonStream);
        jsonStream.close();
        ModelBlock modelBlock = MODEL_BLOCK_DESERIALIZER.deserialize(json, ModelBlock.class, this);
        String tblLocationStr = json.getAsJsonObject().get("tabula").getAsString() + ".tbl";
        ResourceLocation tblLocation = new ResourceLocation(tblLocationStr);
        IResource tblResource = manager.getResource(tblLocation);
        InputStream modelStream = getModelJsonStream(tblLocation.toString(), tblResource.getInputStream());
        JsonTabulaModel modelJson = JsonHelper.parseTabulaModel(modelStream);
        modelStream.close();
        ImmutableList.Builder<ResourceLocation> builder = ImmutableList.builder();
        builder.add(new ResourceLocation(modelBlock.textures.get("texture")));
        return new TabulaModel(modelJson, builder.build(), IPerspectiveAwareModel.MapWrapper.getTransforms(modelBlock.func_181682_g()));
    }

    private InputStream getModelJsonStream(String name, InputStream file) throws IOException {
        ZipInputStream zip = new ZipInputStream(file);
        ZipEntry entry;
        while ((entry = zip.getNextEntry()) != null) {
            if (entry.getName().equals("model.json")) {
                return zip;
            }
        }
        throw new RuntimeException("No model.json present in " + name);
    }

    @Override
    public <T> T deserialize(JsonElement json, Type type) throws JsonParseException {
        return GSON.fromJson(json, type);
    }
}
