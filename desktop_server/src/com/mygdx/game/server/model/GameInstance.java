package com.mygdx.game.server.model;

import com.artemis.World;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.initialize.MapInitializer;
import com.mygdx.game.server.initialize.StartUnitInitializer;
import com.mygdx.game.server.initialize.TechnologyInitializer;
import dagger.Lazy;

import javax.inject.Inject;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

@GameInstanceScope
public class GameInstance {

  private final Lazy<TechnologyInitializer> technologyInitializer;
  private final Lazy<MapInitializer> mapInitializer;
  private final Lazy<StartUnitInitializer> unitInitializer;
  private World world;
  private Queue<Client> playerOrder;
  private Client activePlayer;

  @Inject
  public GameInstance(
      Lazy<TechnologyInitializer> technologyInitializer,
      Lazy<MapInitializer> mapInitializer,
      Lazy<StartUnitInitializer> unitInitializer,
      World world
  ) {
    this.technologyInitializer = technologyInitializer;
    this.mapInitializer = mapInitializer;
    this.unitInitializer = unitInitializer;
    this.world = world;
  }

  public void startGame(int width, int height, long mapType, List<Client> players) {
    technologyInitializer.get().initializeTechnologies();
    mapInitializer.get().initializeMap(width, height, mapType);
    unitInitializer.get().initializeTestUnit();
    playerOrder = new ArrayDeque<>(players);
  }

  public Client changeToNextPlayer() {
    activePlayer = playerOrder.remove();
    playerOrder.add(activePlayer);
    return activePlayer;
  }

  public Client getActivePlayer() {
    return activePlayer;
  }

  public World getWorld() {
    return world;
  }

  public void setWorld(World world) {
    this.world = world;
  }
}
