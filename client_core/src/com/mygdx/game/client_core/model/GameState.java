package com.mygdx.game.client_core.model;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.utils.IntArray;
import com.mygdx.game.client_core.ecs.component.Score;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.SubField;
import lombok.NonNull;
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
  private final ComponentMapper<Score> scoreMapper;
  private final Map<Coordinates, IntArray> entitiesAtCoordinateGame;

  @Inject
  public GameState(
     @NonNull World world
  ) {
    this.coordinatesMapper = world.getMapper(Coordinates.class);
    this.scoreMapper = world.getMapper(Score.class);
    this.entitiesAtCoordinateGame = new HashMap<>();
  }

  public IntArray getEntitiesAtCoordinate(Coordinates coordinates) {
    return entitiesAtCoordinateGame.get(coordinates);
  }

  public Set<Coordinates> getSavedCoordinates() {
    return entitiesAtCoordinateGame.keySet();
  }

  public IntArray getSpecifiedEntitiesAtCoordinate(Coordinates coordinates, ComponentMapper<?>[] mappers) {
    var newEntities = new IntArray();
    var entitiesAtCoords = entitiesAtCoordinateGame.get(coordinates).toArray();
    for (int entity : entitiesAtCoords) {
      for (var mapper : mappers) {
        if (mapper.has(entity)) {
          newEntities.add(entity);
          break;
        }
      }
    }
    return newEntities;
  }

  /**
   * @param entity with an associated coord through world
   */
  public void saveEntity(int entity) {
    log.info("Save entity with id: " + entity);
    saveGameEntity(entity);
  }

  private void saveGameEntity(int entity) {
    var coordinate = coordinatesMapper.get(entity);
    var entities = entitiesAtCoordinateGame.computeIfAbsent(coordinate, ignored -> new IntArray());
    entities.add(entity);
  }

  public void removeEntity(int entity) {
    var coordinate = coordinatesMapper.get(entity);
    entitiesAtCoordinateGame.getOrDefault(coordinate, new IntArray()).removeValue(entity);
  }

  public Map<Coordinates, Integer> getScoreMap() {
    var scoreMap = new HashMap<Coordinates, Integer>();
    for (Map.Entry<Coordinates, IntArray> coordinateEntities : entitiesAtCoordinateGame.entrySet()) {
      for (int i = 0; i < coordinateEntities.getValue().size; i++) {
        if (scoreMapper.has(coordinateEntities.getValue().get(i))) {
          scoreMap.put(coordinateEntities.getKey(), scoreMapper.get(coordinateEntities.getValue().get(i)).getScoreValue());
        }
      }
    }
    return scoreMap;
  }
}
