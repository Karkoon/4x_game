package com.mygdx.game.client.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.ModelInstanceComp;
import com.mygdx.game.client.ecs.component.TextureComp;
import com.mygdx.game.client.util.ModelInstanceUtil;
import com.mygdx.game.config.ModelConfig;
import com.mygdx.game.config.TextureConfig;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class TextureCompSetter {

  private final ComponentMapper<TextureComp> textureMapper;
  private final GameScreenAssets assets;

  @Inject
  public TextureCompSetter(@NonNull World world,
                           @NonNull GameScreenAssets assets) {
    this.textureMapper = world.getMapper(TextureComp.class);
    this.assets = assets;
  }

  public void set(@NonNull TextureConfig config, int entity) {
    setUpTextureComp(config, entity);
  }

  private void setUpTextureComp(@NonNull TextureConfig config, int entityId) {
    var texture = textureMapper.create(entityId);
    texture.setTexture(assets.getTexture(config.getTextureName()));
  }
}
