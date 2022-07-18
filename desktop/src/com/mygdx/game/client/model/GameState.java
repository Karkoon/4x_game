package com.mygdx.game.client.model;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.utils.IntArray;
import com.mygdx.game.client.ecs.component.Movable;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.SubField;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
@Log
public class GameState {

  private final ComponentMapper<Coordinates> coordinatesMapper;
  private final ComponentMapper<SubField> subFieldMapper;
  private final Map<Coordinates, IntArray> entitiesAtCoordinateGame;

  @Inject
  public GameState(World world) {
    this.coordinatesMapper = world.getMapper(Coordinates.class);
    this.subFieldMapper = world.getMapper(SubField.class);
    entitiesAtCoordinateGame = new HashMap<>();
  }

  public IntArray getEntitiesAtCoordinate(Coordinates coordinates) {
    return entitiesAtCoordinateGame.get(coordinates);
  }

  public Set<Coordinates> getSavedCoordinates() {
    return entitiesAtCoordinateGame.keySet();
  }

  /**
   * @param entity with an associated coord through world
   */
  public void saveEntity(int entity) {
    log.info("Save entity with id: " + entity);
    if (!subFieldMapper.has(entity))
      saveGameEntity(entity);
  }

  private void saveGameEntity(int entity) {
    var coordinate = coordinatesMapper.get(entity);
    var entities = entitiesAtCoordinateGame.computeIfAbsent(coordinate, (_coords) -> new IntArray());
    entities.add(entity);
  }

  public void removeEntity(int entity) {
    var coordinate = coordinatesMapper.get(entity);
    entitiesAtCoordinateGame.getOrDefault(coordinate, new IntArray()).removeValue(entity);
  }
}
