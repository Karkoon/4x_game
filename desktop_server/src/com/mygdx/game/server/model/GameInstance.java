package com.mygdx.game.server.model;

import com.artemis.World;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.initialize.MapInitializer;
import com.mygdx.game.server.initialize.MaterialInitializer;
import com.mygdx.game.server.initialize.StartUnitInitializer;
import com.mygdx.game.server.initialize.TechnologyInitializer;
import com.mygdx.game.server.network.gameinstance.GameInstanceServer;
import com.mygdx.game.server.network.gameinstance.services.RoundEndService;
import dagger.Lazy;

import javax.inject.Inject;
import java.util.ArrayDeque;
import java.util.Queue;

@GameInstanceScope
public class GameInstance {

  private final Lazy<TechnologyInitializer> technologyInitializer;
  private final Lazy<MaterialInitializer> materialInitializer;
  private final Lazy<MapInitializer> mapInitializer;
  private final Lazy<StartUnitInitializer> unitInitializer;

  private final World world;
  private final GameRoom room;
  private final Lazy<GameInstanceServer> gameInstanceServer;
  private final RoundEndService roundEndService;
  private Queue<Client> playerOrder;
  private Client activePlayer;

  @Inject
  public GameInstance(
      Lazy<TechnologyInitializer> technologyInitializer,
      Lazy<MaterialInitializer> materialInitializer,
      Lazy<MapInitializer> mapInitializer,
      Lazy<StartUnitInitializer> unitInitializer,
      World world,
      GameRoom room,
      Lazy<GameInstanceServer> gameInstanceServer,
      RoundEndService roundEndService
  ) {
    this.technologyInitializer = technologyInitializer;
    this.materialInitializer = materialInitializer;
    this.mapInitializer = mapInitializer;
    this.unitInitializer = unitInitializer;
    this.world = world;
    this.room = room;
    this.gameInstanceServer = gameInstanceServer;
    this.roundEndService = roundEndService;
  }

  public void startGame(int width, int height, long mapType) {
    var map = mapInitializer.get().initializeMap(width, height, mapType);
    technologyInitializer.get().initializeTechnologies();
    materialInitializer.get().initializeMaterials();
    unitInitializer.get().initializeStartingUnits(map);
    playerOrder = new ArrayDeque<>();
    playerOrder.addAll(room.getClients());
    changeToNextPlayer();
    world.process();
  }

  public Client changeToNextPlayer() {
    if (playerOrder.isEmpty()) {
      roundEndService.makeEndRoundSteps();
      playerOrder.addAll(room.getClients());
    }
    activePlayer = playerOrder.remove();
    return activePlayer;
  }

  public Client getActivePlayer() {
    return activePlayer;
  }

  public World getWorld() {
    return world;
  }

  public GameInstanceServer getServer() {
    return gameInstanceServer.get();
  }
}
