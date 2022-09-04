package com.mygdx.game.server.model;

import com.artemis.World;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.initialize.MapInitializer;
import com.mygdx.game.server.initialize.StartUnitInitializer;
import com.mygdx.game.server.initialize.TechnologyInitializer;
import dagger.Lazy;

import javax.inject.Inject;
import java.util.ArrayDeque;
import java.util.Queue;

@GameInstanceScope
public class GameInstance {

  private final Lazy<TechnologyInitializer> technologyInitializer;
  private final Lazy<MapInitializer> mapInitializer;
  private final Lazy<StartUnitInitializer> unitInitializer;
  private final World world;
  private final GameRoom room;
  private Queue<Client> playerOrder;
  private Client activePlayer;

  @Inject
  public GameInstance(
      Lazy<TechnologyInitializer> technologyInitializer,
      Lazy<MapInitializer> mapInitializer,
      Lazy<StartUnitInitializer> unitInitializer,
      World world,
      GameRoom room
  ) {
    this.technologyInitializer = technologyInitializer;
    this.mapInitializer = mapInitializer;
    this.unitInitializer = unitInitializer;
    this.world = world;
    this.room = room;
  }

  public void startGame(int width, int height, long mapType) {
    mapInitializer.get().initializeMap(width, height, mapType);
    technologyInitializer.get().initializeTechnologies();
    unitInitializer.get().initializeStartingUnits();
    playerOrder = new ArrayDeque<>(room.getClients());
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
}
