package com.mygdx.game.client.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.TextureComp;
import com.mygdx.game.client_core.ecs.entityfactory.Setter;
import com.mygdx.game.config.Config;
import com.mygdx.game.config.TextureConfig;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
public class TextureCompSetter implements Setter {

  private ComponentMapper<TextureComp> textureMapper;
  private final GameScreenAssets assets;

  @Inject
  public TextureCompSetter(
      World world,
      GameScreenAssets assets
  ) {
    world.inject(this);
    this.assets = assets;
  }

  @Override
  public Result set(Config config, int entityId) {
    if (config instanceof TextureConfig textureConfig) {
      setUpTextureComp(textureConfig, entityId);
      return Result.HANDLED;
    } else {
      return Result.REJECTED;
    }
  }

  private void setUpTextureComp(@NonNull TextureConfig config, int entityId) {
    var texture = textureMapper.create(entityId);
    texture.setTexture(assets.getTexture(config.getTextureName()));
  }


}
