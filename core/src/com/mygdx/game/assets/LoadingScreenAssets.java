package com.mygdx.game.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.utils.Disposable;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LoadingScreenAssets implements Disposable {

  @NonNull
  private final AssetManager assetManager;

  @Inject
  public LoadingScreenAssets(AssetManager manager) {
    this.assetManager = manager;
    initCustomLoaders();
  }

  @NonNull
  public TextureAtlas getTextureAtlas(@NonNull String path) {
    return assetManager.get(path, TextureAtlas.class);
  }

  public void loadAssetsSync() {
    assetManager.load(LoadingScreenAssetPaths.LOADING_SCREEN_TEXTURE_ATLAS, TextureAtlas.class);
    assetManager.finishLoading();
  }

  @Override
  public void dispose() {
    assetManager.unload(LoadingScreenAssetPaths.LOADING_SCREEN_TEXTURE_ATLAS);
  }

  private void initCustomLoaders() {
    assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(new InternalFileHandleResolver()));
  }
}