package com.mygdx.game.client.ui.decorations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.google.inject.Singleton;
import com.mygdx.game.assets.MenuScreenAssetPaths;
import com.mygdx.game.core.util.NoiseGenerator;

import javax.inject.Inject;

@Singleton
public class StarBackground {

  private final ShaderProgram nebulaShader;
  private final NoiseGenerator noiseGenerator = new NoiseGenerator();
  private float step = 0;
  private float time = 0;
  private final Texture nebula;
  private final Texture stars;
  private final AssetManager assets;
  private final ShaderProgram starShader;
  private final Texture noise;

  private float iPosX;
  private float iPosY;

  public @Inject StarBackground(
      AssetManager assets
  ) {
    this.assets = assets;
    nebula = assets.get(MenuScreenAssetPaths.NEBULA, Texture.class);
    stars = setupStarsTexture(assets.get(MenuScreenAssetPaths.STARS, Texture.class));
    noise = createBackgroundNoise();

    nebulaShader = createShader(MenuScreenAssetPaths.NEBULA_SHADER);
    starShader = createShader(MenuScreenAssetPaths.STARS_SHADER);
  }

  private Texture setupStarsTexture(Texture starsTexture) {
    starsTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
    return starsTexture;
  }

  private void updateBackgroundShaderTime(float delta) {
    if (step > 0.05f) {
      nebulaShader.bind();
      nebulaShader.setUniformf("u_time", time);
      starShader.bind();
      starShader.setUniformf("u_time", time);
      time += delta;
      step = 0f;
    } else {
      step += delta;
    }
  }

  public void update(float delta) {
    updateBackgroundShaderTime(delta);
  }

  public void draw(Batch batch, Camera camera) {
    float posX = camera.position.x - camera.viewportWidth / 2;
    float posY = camera.position.y - camera.viewportHeight / 2;

    batch.setShader(nebulaShader);
    noise.bind(2);
    nebulaShader.setUniformi("u_noise", 2);
    Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
    batch.draw(nebula, posX, posY, camera.viewportWidth, camera.viewportHeight);

    batch.setShader(starShader);
    iPosX = Interpolation.bounce.apply(iPosX, posX, 0.6f);
    iPosY = Interpolation.bounce.apply(iPosY, posY, 0.6f);
    batch.draw(stars, iPosX, iPosY, camera.viewportWidth, camera.viewportHeight, 0, 0, 4, 4);
    batch.setShader(null);
  }

  public void resize(int width, int height) {
    nebulaShader.bind();
    nebulaShader.setUniformf("u_resolution", width, height);
    starShader.bind();
    starShader.setUniformf("u_resolution", width, height);

  }

  private ShaderProgram createShader(String path) {
    var shader = assets.get(path, ShaderProgram.class);
    shader.bind();
    noise.bind(2);
    shader.setUniformi("u_noise", 2);
    Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
    return shader;
  }

  private Texture createBackgroundNoise() {
    var generatedNoise = noiseGenerator.noise(1, 1);
    generatedNoise.setWrap(TextureWrap.MirroredRepeat, TextureWrap.MirroredRepeat);
    return generatedNoise;
  }
}
