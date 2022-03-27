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
import com.mygdx.game.util.Const;
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
        assetManager.load(Const.ASSETS_PATH + "entity_configs/field",
                Array.class,
                new ArrayLoaderParameter(FieldConfig.class, "json"));
        assetManager.finishLoading();
    }

    public void loadAssets() {
        loadModels(FieldConfig.class);
        loadTextures();
        assetManager.finishLoading();
    }

    public Model getModel(@NonNull EntityConfig config) {
        var filename = assetManager.getAssetFileName(config);
        var directory = filename.substring(0, filename.lastIndexOf("/") + 1);
        return assetManager.get(directory + config.getName() + ".g3db");
    }

    public Texture getTexture(@NonNull String path) {
        return assetManager.get(path, Texture.class);
    }

    public GameConfigsService getGameContentService() {
        return new GameConfigsService(assetManager);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    private <T extends EntityConfig> void loadModels(@NonNull Class<T> clazz) {
        var entityConfigs = new Array<T>();
        assetManager.getAll(clazz, entityConfigs);
        for (var i = 0; i < entityConfigs.size; i++) {
            var entityConfig = entityConfigs.get(i);
            var filename = assetManager.getAssetFileName(entityConfig);
            var directory = filename.substring(0, filename.lastIndexOf("/") + 1);
            assetManager.load(directory + entityConfig.getModelPath(), Model.class);
        }
    }

    private void loadTextures() {
        assetManager.load(Const.DEMO_TEXTURE_PATH, Texture.class);
    }
}
