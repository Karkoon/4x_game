package com.mygdx.game.client.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.mygdx.game.client.TextureRenderer;
import com.mygdx.game.client.ecs.component.TextureComp;
import com.mygdx.game.client.model.TextureDraw;
import com.mygdx.game.client_core.ecs.component.Position;
import com.mygdx.game.core.ecs.component.Technology;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@All({Technology.class, Position.class})
@Log
public class TechnologyRenderSystem extends IteratingSystem {

  protected ComponentMapper<Technology> technologyMapper;
  protected ComponentMapper<Position> positionMapper;
  protected ComponentMapper<TextureComp> textureMapper;

  private final TextureRenderer renderer;

  @Inject
  public TechnologyRenderSystem(@NonNull TextureRenderer renderer) {
    this.renderer = renderer;
  }

  @Override
  protected void process(int entityId) {
    var texture = textureMapper.get(entityId).getTexture();
    var position = positionMapper.get(entityId).getPosition();
    TextureDraw textureDraw = new TextureDraw(entityId, texture, position);
    if (technologyMapper.has(entityId)) {
      renderer.addTextureToCache(textureDraw);
    }
  }
}
