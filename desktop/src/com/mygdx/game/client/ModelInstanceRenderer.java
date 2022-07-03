package com.mygdx.game.client;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelCache;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ModelInstanceRenderer implements Disposable {

  private final ModelBatch modelBatch;
  private final ModelCache cache;
  private final Array<ModelInstance> modelInstances;
  private final Array<ModelInstance> subModelInstances;

  private final Camera camera;

  @Inject
  public ModelInstanceRenderer(@NonNull Viewport viewport) {
    this.camera = viewport.getCamera();
    this.cache = new ModelCache();
    this.modelBatch = new ModelBatch();
    this.modelInstances = new Array<>();
    this.subModelInstances = new Array<>();
  }

  public void render() {
    performFrustumCullingToModelCache(modelInstances);
    modelInstances.clear();
    modelBatch.begin(camera);
    modelBatch.render(cache);
    modelBatch.end();
  }

  public void subRender() {
    performFrustumCullingToModelCache(subModelInstances);
    subModelInstances.clear();
    modelBatch.begin(camera);
    modelBatch.render(cache);
    modelBatch.end();
  }

  private void performFrustumCullingToModelCache(Array<ModelInstance> modelInstances) {
    cache.begin(camera);
    for (var i = 0; i < modelInstances.size; i++) {
      var modelInstance = modelInstances.get(i);
      if (isVisible(camera, modelInstance)) {
        cache.add(modelInstance);
      }
    }
    cache.end();
  }

  private boolean isVisible(Camera cam, ModelInstance instance) {
    float[] val = instance.transform.val;
    return cam.frustum.boundsInFrustum(
        val[Matrix4.M03], val[Matrix4.M13], val[Matrix4.M23],
        500f, 500f, 0f)
        && cam.position.dst(val[Matrix4.M03], val[Matrix4.M13], val[Matrix4.M23]) < 5500f;
  }

  public void addModelToCache(ModelInstance modelInstance) {
    modelInstances.add(modelInstance);
  }

  public void addSubModelToCache(ModelInstance modelInstance) {
    subModelInstances.add(modelInstance);
  }

  public void addToCache(Array<ModelInstance> modelInstances) {
    this.modelInstances.addAll(modelInstances);
  }

  @Override
  public void dispose() {
    cache.dispose();
  }
}
