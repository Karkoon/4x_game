package com.mygdx.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.assets.assetloaders.ArrayLoader;
import com.mygdx.assets.assetloaders.ArrayLoader.ArrayLoaderParameter;
import com.mygdx.assets.assetloaders.JsonLoader;
import com.mygdx.config.EntityConfig;
import com.mygdx.config.FieldConfig;
import com.mygdx.config.GameConfigsService;
import lombok.NonNull;

public class Assets implements Disposable {

    @NonNull
    private final AssetManager assetManager;

    public Assets() {
        this.assetManager = new AssetManager();
        initCustomLoaders();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void initCustomLoaders() {
        assetManager.setLoader(Array.class, new ArrayLoader(new InternalFileHandleResolver()));
        assetManager.setLoader(FieldConfig.class, new JsonLoader<>(new InternalFileHandleResolver()));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void loadConfig() {
        assetManager.load(AssetPaths.ASSETS_PATH + "entity_configs/field",
                Array.class,
                new ArrayLoaderParameter(FieldConfig.class, "json"));
        assetManager.finishLoading();
    }

    public void loadAssets() {
        loadModels(FieldConfig.class);
        loadTextures();
        assetManager.finishLoading();
    }

    @NonNull
    public Model getModel(@NonNull EntityConfig config) {
        return assetManager.get(AssetPaths.MODEL_DIR + config.getModelPath());
    }

    @NonNull
    public Texture getTexture(@NonNull String path) {
        return assetManager.get(path, Texture.class);
    }

    @NonNull
    public GameConfigsService getGameContentService() {
        return new GameConfigsService(assetManager);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    private <T extends EntityConfig> void loadModels(@NonNull Class<T> entityConfigClass) {
        var entityConfigs = new Array<T>();
        assetManager.getAll(entityConfigClass, entityConfigs);
        for (var i = 0; i < entityConfigs.size; i++) {
            var entityConfig = entityConfigs.get(i);
            assetManager.load(AssetPaths.MODEL_DIR + entityConfig.getModelPath(), Model.class);
        }
    }

    private void loadTextures() {
        assetManager.load(AssetPaths.DEMO_TEXTURE_PATH, Texture.class);
    }
}
