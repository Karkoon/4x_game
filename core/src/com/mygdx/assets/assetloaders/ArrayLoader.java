package com.mygdx.assets.assetloaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.assets.assetloaders.JsonLoader.JsonLoaderParameters;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import static com.mygdx.assets.assetloaders.ArrayLoader.ArrayLoaderParameter;

public class ArrayLoader<T> extends AsynchronousAssetLoader<Array<T>, ArrayLoaderParameter<T>> {

  private final Array<T> loadedEntities;

  public ArrayLoader(FileHandleResolver resolver) {
    super(resolver);
    loadedEntities = new Array<>();
  }

  @Override
  public void loadAsync(AssetManager manager, String directoryName, FileHandle directory, ArrayLoaderParameter<T> parameter) {
    manager.getAll(parameter.assetType, loadedEntities);
  }

  @Override
  public Array<T> loadSync(AssetManager manager, String fileName, FileHandle file, ArrayLoaderParameter<T> parameter) {
    return loadedEntities;
  }

  @Override
  @SuppressWarnings("rawtypes")
  public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, ArrayLoaderParameter<T> parameter) {
    FileHandle[] files = file.list(parameter.getFiletype());
    Array<AssetDescriptor> assetDescriptors = new Array<>(files.length);
    for (FileHandle fileHandle : files) {
      assetDescriptors.add(new AssetDescriptor<>(fileHandle,
          parameter.getAssetType(),
          new JsonLoaderParameters<>(parameter.getAssetType())));
    }
    return assetDescriptors;
  }

  @Getter
  @AllArgsConstructor
  public static class ArrayLoaderParameter<T> extends AssetLoaderParameters<Array<T>> {
    @NonNull
    private final Class<T> assetType;
    @NonNull
    private final String filetype;
  }
}
