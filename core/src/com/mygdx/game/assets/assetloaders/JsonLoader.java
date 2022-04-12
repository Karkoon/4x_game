package com.mygdx.game.assets.assetloaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.mygdx.game.assets.assetloaders.JsonLoader.JsonLoaderParameters;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

public class JsonLoader<T> extends AsynchronousAssetLoader<T, JsonLoaderParameters<T>> {

  private final Json json = new Json();
  private T objectToLoad;

  public JsonLoader(FileHandleResolver resolver) {
    super(resolver);
  }

  @Override
  public void loadAsync(AssetManager manager, String fileName, FileHandle file, JsonLoaderParameters<T> parameter) {
    objectToLoad = json.fromJson(parameter.getLoadedObjectClass(), file);
  }

  @Override
  public T loadSync(AssetManager manager, String fileName, FileHandle file, JsonLoaderParameters<T> parameter) {
    return objectToLoad;
  }

  @Override
  @SuppressWarnings("rawtypes")
  public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, JsonLoaderParameters<T> parameter) {
    return null;
  }

  @AllArgsConstructor
  @Getter
  public static class JsonLoaderParameters<T> extends AssetLoaderParameters<T> {
    @NonNull
    private final Class<T> loadedObjectClass;
  }
}
