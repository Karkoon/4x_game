package com.mygdx.game.client;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Log
public class TextureRenderer implements Disposable {

  private final SpriteBatch spriteBatch;
  private final SpriteCache cache;
  private final Array<Texture> textures;

  private final Camera camera;

  @Inject
  public TextureRenderer(@NonNull @Named("orthographic") Viewport viewport) {
    this.camera = viewport.getCamera();
    this.spriteBatch = new SpriteBatch();
    this.cache = new SpriteCache();
    this.textures = new Array<>();
  }

  public void render() {
    camera.update();
    spriteBatch.setProjectionMatrix(camera.combined);

    spriteBatch.begin();
    performFrustumCullingToTextureCache();
    textures.clear();
    spriteBatch.end();
  }

  private void performFrustumCullingToTextureCache() {
    for (Texture texture : textures) {
      spriteBatch.draw(texture, 0, 0);
    }
  }

  @Override
  public void dispose() {
    cache.dispose();
  }
}
