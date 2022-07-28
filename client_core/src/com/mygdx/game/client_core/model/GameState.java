package com.mygdx.game.client_core.model;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.utils.IntArray;
import com.mygdx.game.client_core.ecs.component.Movable;
import com.mygdx.game.client_core.ecs.component.Score;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.SubField;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Singleton
@Log
public class GameState {

  private final ComponentMapper<Coordinates> coordinatesMapper;
  private final ComponentMapper<SubField> subFieldMapper;
  private final ComponentMapper<Score> scoreMapper;
  private final ComponentMapper<Movable> movableMapper;
  private final Map<Coordinates, IntArray> entitiesAtCoordinateGame;

  @Inject
  public GameState(World world) {
    this.coordinatesMapper = world.getMapper(Coordinates.class);
    this.subFieldMapper = world.getMapper(SubField.class);
    this.scoreMapper = world.getMapper(Score.class);
    this.movableMapper = world.getMapper(Movable.class);
    this.entitiesAtCoordinateGame = new HashMap<>();
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
    if (movableMapper.has(entity) || scoreMapper.has(entity)) {
      saveGameEntity(entity);
    }
  }

  private void saveGameEntity(int entity) {
    var coordinate = coordinatesMapper.get(entity);
    var entities = entitiesAtCoordinateGame.computeIfAbsent(coordinate, _coords -> new IntArray());
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
