package com.mygdx.game.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.assets.assetloaders.ArrayLoader;
import com.mygdx.game.assets.assetloaders.JsonLoader;
import com.mygdx.game.config.FieldConfig;
import com.mygdx.game.config.GameConfigs;
import com.mygdx.game.config.SubFieldConfig;
import com.mygdx.game.config.TechnologyConfig;
import com.mygdx.game.config.UnitConfig;
import lombok.NonNull;

import javax.inject.Inject;

public class GameConfigAssets {

  @NonNull
  private final GameConfigs gameConfigs;
  private final AssetManager assetManager;

  @Inject
  public GameConfigAssets(
      @NonNull AssetManager assetManager,
      @NonNull GameConfigs gameConfigs
  ) {
    this.assetManager = assetManager;
    this.gameConfigs = gameConfigs;
    initCustomLoaders();
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void initCustomLoaders() {
    assetManager.setLoader(Array.class, new ArrayLoader(new InternalFileHandleResolver()));
    assetManager.setLoader(FieldConfig.class, new JsonLoader<>(new InternalFileHandleResolver()));
    assetManager.setLoader(UnitConfig.class, new JsonLoader<>(new InternalFileHandleResolver()));
    assetManager.setLoader(SubFieldConfig.class, new JsonLoader<>(new InternalFileHandleResolver()));
    assetManager.setLoader(TechnologyConfig.class, new JsonLoader<>(new InternalFileHandleResolver()));
  }

  public void loadAssetsAsync() {
    loadConfigs();
  }

  @NonNull
  public GameConfigs getGameConfigs() {
    if (gameConfigs.size() == 0) {
      populateGameConfigs();
    }
    return gameConfigs;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void loadConfigs() {
    assetManager.load(GameConfigAssetPaths.FIELD_CONFIG_DIR, Array.class,
        new ArrayLoader.ArrayLoaderParameter(FieldConfig.class, "json"));
    assetManager.load(GameConfigAssetPaths.UNIT_CONFIG_DIR, Array.class,
        new ArrayLoader.ArrayLoaderParameter(UnitConfig.class, "json"));
    assetManager.load(GameConfigAssetPaths.SUB_FIELD_CONFIG_DIR, Array.class,
        new ArrayLoader.ArrayLoaderParameter(SubFieldConfig.class, "json"));
    assetManager.load(GameConfigAssetPaths.TECHNOLOGY_CONFIG_DIR, Array.class,
            new ArrayLoader.ArrayLoaderParameter(TechnologyConfig.class, "json"));
  }

  private void populateGameConfigs() {
    gameConfigs.putAll(assetManager.getAll(FieldConfig.class, new Array<>()));
    gameConfigs.putAll(assetManager.getAll(UnitConfig.class, new Array<>()));
    gameConfigs.putAll(assetManager.getAll(SubFieldConfig.class, new Array<>()));
    gameConfigs.putAll(assetManager.getAll(TechnologyConfig.class, new Array<>()));
  }

  public void loadAssetsSync() {
    loadAssetsAsync();
    assetManager.finishLoading();
  }
}
