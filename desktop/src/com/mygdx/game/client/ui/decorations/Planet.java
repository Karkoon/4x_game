package com.mygdx.game.client.ui.decorations;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.assets.MenuScreenAssetPaths;
import com.mygdx.game.assets.MenuScreenAssets;
import com.mygdx.game.core.util.Updatable;
import lombok.NonNull;

public class Planet implements Updatable {
  private final Sprite planetSprite;
  private final MenuScreenAssets assets;
  private final ShaderProgram shader;

  public Planet(@NonNull MenuScreenAssets assets, Vector2 size, Vector2 position) {
    this.assets = assets;
    this.planetSprite = createPlanetSprite(size, position);
    this.shader = createPlanetShader();
  }

  private Sprite createPlanetSprite(Vector2 size, Vector2 position) {
    var sprite = new Sprite(assets.getTexture(MenuScreenAssetPaths.PLANET));
    sprite.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    sprite.setSize(size.x, size.y);
    sprite.setPosition(position.x - size.x / 2, position.y - size.y / 2);
    sprite.flip(true, true);
    sprite.setOriginCenter();
    return sprite;
  }

  private ShaderProgram createPlanetShader() {
    var shaderProgram = assets.getShaderProgram(MenuScreenAssetPaths.PLANET_SHADER);
    shaderProgram.bind();
    return shaderProgram;
  }

  public void resize(int width, int height) {
    shader.bind();
    shader.setUniformf("u_resolution", width, height);
  }

  public void draw(Batch batch) {
    batch.setShader(shader);
    planetSprite.draw(batch);
  }

  @Override
  public void update(float delta) {
    planetSprite.rotate(delta);
  }
}
