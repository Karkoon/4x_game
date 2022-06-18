package com.mygdx.game.client.model;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.utils.IntArray;
import com.mygdx.game.client.ecs.component.Score;
import com.mygdx.game.core.ecs.component.Coordinates;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
public class GameState {

  private final ComponentMapper<Coordinates> coordinatesMapper;
  private final ComponentMapper<Score> scoreMapper;
  private final Map<Coordinates, IntArray> entitiesAtCoordinate;

  @Inject
  public GameState(World world) {
    this.coordinatesMapper = world.getMapper(Coordinates.class);
    this.scoreMapper = world.getMapper(Score.class);
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

  public Map<Coordinates, Integer> getScoreMap() {
    var scoreMap = new HashMap<Coordinates, Integer>();
    for (Map.Entry<Coordinates, IntArray> coordinateEntities : entitiesAtCoordinate.entrySet()) {
      for (int i = 0; i < coordinateEntities.getValue().size; i++) {
        if (scoreMapper.has(coordinateEntities.getValue().get(i))) {
          scoreMap.put(coordinateEntities.getKey(), scoreMapper.get(coordinateEntities.getValue().get(i)).getScoreValue());
        }
      }
    }
    return scoreMap;
  }
}
