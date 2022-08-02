package com.mygdx.game.client.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.TextureComp;
import com.mygdx.game.client_core.ecs.entityfactory.Setter;
import com.mygdx.game.config.EntityConfig;
import com.mygdx.game.config.TextureConfig;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class TextureCompSetter implements Setter {

  private final ComponentMapper<TextureComp> textureMapper;
  private final GameScreenAssets assets;

  @Inject
  public TextureCompSetter(@NonNull World world,
                           @NonNull GameScreenAssets assets) {
    this.textureMapper = world.getMapper(TextureComp.class);
    this.assets = assets;
  }

  @Override
  public Result set(EntityConfig config, int entityId) {
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
