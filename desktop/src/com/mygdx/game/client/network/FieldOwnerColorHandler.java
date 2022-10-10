package com.mygdx.game.client.network;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.assets.GameScreenAssetPaths;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.ModelInstanceComp;
import com.mygdx.game.client.model.OwnerToColorMap;
import com.mygdx.game.client.util.ModelInstanceUtil;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.Owner;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
public class FieldOwnerColorHandler implements ComponentMessageListener.Handler<Owner> {

  private final OwnerToColorMap colorMap;
  private final GameScreenAssets assets;
  private ComponentMapper<ModelInstanceComp> modelInstanceMapper;
  private ComponentMapper<Field> fieldMapper;
  private ComponentMapper<Owner> ownerMapper;
  @Inject
  public FieldOwnerColorHandler(
      World world,
      OwnerToColorMap colorMap,
      GameScreenAssets assets
  ) {
    this.colorMap = colorMap;
    this.assets = assets;
    world.inject(this);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, Owner component) {
    if (fieldMapper.has(worldEntity)) {
      var modelInstances = modelInstanceMapper.create(worldEntity);
      modelInstances.setHighlight(createHighlightModelInstance(worldEntity));
    }
    return FULLY_HANDLED;
  }

  private ModelInstance createHighlightModelInstance(int entityId) {
    var owner = ownerMapper.create(entityId);
    var color = colorMap.get(owner);
    log.info("added color" + color.toIntBits() + " owner: " + owner.getToken());
    var modelInstance = new ModelInstance(assets.getModel(GameScreenAssetPaths.HIGHLIGHT_MODEL));
    var texture = assets.getTexture(GameScreenAssetPaths.HIGHLIGHT_TEXTURE);
    ModelInstanceUtil.setTexture(modelInstance, texture);
    ModelInstanceUtil.tintColor(modelInstance, color);
    return modelInstance;
  }
}
