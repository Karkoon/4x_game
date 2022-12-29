package com.mygdx.game.client.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.TextureComp;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.ecs.entityfactory.Setter;
import com.mygdx.game.config.Config;
import com.mygdx.game.config.TextureConfig;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class TextureCompSetter implements Setter {

  private final GameScreenAssets gameScreenAssets;

  private ComponentMapper<TextureComp> textureMapper;

  @Inject
  public TextureCompSetter(
      GameScreenAssets gameScreenAssets,
      World world
  ) {
    world.inject(this);
    this.gameScreenAssets = gameScreenAssets;
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
    texture.setTexture(gameScreenAssets.getTexture(config.getTextureName()));
  }


}
