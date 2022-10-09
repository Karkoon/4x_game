package com.mygdx.game.client.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.assets.GameScreenAssetPaths;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.Highlight;
import com.mygdx.game.client.ecs.component.ModelInstanceComp;
import com.mygdx.game.client.util.ModelInstanceUtil;
import com.mygdx.game.client_core.ecs.component.Position;
import com.mygdx.game.core.ecs.component.Coordinates;
import lombok.NonNull;

import javax.inject.Inject;

public class HighlightFactory {

  private ComponentMapper<Position> positionMapper;
  private ComponentMapper<ModelInstanceComp> modelMapper;
  private ComponentMapper<Coordinates> coordinatesComponentMapper;
  private ComponentMapper<Highlight> highlightMapper;
  private final GameScreenAssets assets;

  @Inject
  public HighlightFactory(
      @NonNull World world,
      @NonNull GameScreenAssets assets
  ) {
    world.inject(this);
    this.assets = assets;
  }

  public void createEntity(int entity, Coordinates coordinates, boolean inField) {
    positionMapper.create(entity).getValue().set(0, 15, 0);
    setUpModelInstanceComp(entity);
    setUpCoordinates(entity, coordinates);
    highlightMapper.create(entity).setInField(inField);
  }

  private void setUpCoordinates(int entity, Coordinates coordinates) {
    coordinatesComponentMapper.create(entity).setCoordinates(coordinates);
  }

  private void setUpModelInstanceComp(int entityId) {
    var modelInstance = prepareModelInstance();
    var modelInstanceComp = modelMapper.create(entityId);
    modelInstanceComp.setMainModel(modelInstance);
  }

  private ModelInstance prepareModelInstance() {
    var highlightPath = GameScreenAssetPaths.HIGHLIGHT_MODEL;
    var highlightTexturePath = GameScreenAssetPaths.HIGHLIGHT_TEXTURE;
    var modelInstance = new ModelInstance(assets.getModel(highlightPath), new Vector3());
    var texture = assets.getTexture(highlightTexturePath);
    ModelInstanceUtil.setTexture(modelInstance, texture);
    return modelInstance;
  }


}
