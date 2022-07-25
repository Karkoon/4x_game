package com.mygdx.game.client;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.client.model.TextureDraw;
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
  private final Array<TextureDraw> textures;

  private final Camera camera;

  @Inject
  public TextureRenderer(@NonNull @Named("orthographic") Viewport viewport) {
    this.camera = viewport.getCamera();
    this.spriteBatch = new SpriteBatch();
    this.cache = new SpriteCache();
    this.textures = new Array<>();
  }

  public void technologyRender() {
    camera.update();
    spriteBatch.setProjectionMatrix(camera.combined);

    spriteBatch.begin();
    performFrustumCullingToTextureCache();
    textures.clear();
    spriteBatch.end();
  }

  private void performFrustumCullingToTextureCache() {
    for (TextureDraw textureDraw : textures) {
      Vector3 position = textureDraw.getPosition();
      log.info("Position: " + textureDraw.getPosition().toString());
      spriteBatch.draw(textureDraw.getTexture(), position.x, position.y);
    }
  }

  public void addTextureToCache(TextureDraw textureDraw) {
    textures.add(textureDraw);
  }

  @Override
  public void dispose() {
    cache.dispose();
  }
}
