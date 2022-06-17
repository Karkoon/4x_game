package com.mygdx.game.client.model;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.utils.IntArray;
import com.mygdx.game.core.ecs.component.Coordinates;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
public class GameState {

  private final ComponentMapper<Coordinates> coordinatesMapper;
  private final Map<Coordinates, IntArray> entitiesAtCoordinate;

  @Inject
  public GameState(World world) {
    this.coordinatesMapper = world.getMapper(Coordinates.class);
    entitiesAtCoordinate = new HashMap<>();
  }

  public IntArray getEntitiesAtCoordinate(Coordinates coordinates) {
    return entitiesAtCoordinate.get(coordinates);
  }

  public Set<Coordinates> getSavedCoordinates() {
    return entitiesAtCoordinate.keySet();
  }

  /**
   * @param entity with an associated coord through world
   */
  public void saveEntity(int entity) {
    var coordinate = coordinatesMapper.get(entity);
    var entities = entitiesAtCoordinate.computeIfAbsent(coordinate, (_coords) -> new IntArray());
    entities.add(entity);
  }

  public void removeEntity(int entity) {
    var coordinate = coordinatesMapper.get(entity);
    entitiesAtCoordinate.getOrDefault(coordinate, new IntArray()).removeValue(entity);
  }
}
