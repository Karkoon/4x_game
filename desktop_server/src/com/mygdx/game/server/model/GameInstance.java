package com.mygdx.game.server.model;

import com.artemis.World;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.initialize.MapInitializer;
import com.mygdx.game.server.initialize.MaterialInitializer;
import com.mygdx.game.server.initialize.StartUnitInitializer;
import com.mygdx.game.server.initialize.TechnologyInitializer;
import com.mygdx.game.server.network.gameinstance.GameInstanceServer;
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
  private Queue<Client> playerOrder;
  private Client activePlayer;
  private int currentTurn;

  @Inject
  public GameInstance(
      Lazy<TechnologyInitializer> technologyInitializer,
      Lazy<MaterialInitializer> materialInitializer,
      Lazy<MapInitializer> mapInitializer,
      Lazy<StartUnitInitializer> unitInitializer,
      World world,
      GameRoom room,
      Lazy<GameInstanceServer> gameInstanceServer
  ) {
    this.technologyInitializer = technologyInitializer;
    this.materialInitializer = materialInitializer;
    this.mapInitializer = mapInitializer;
    this.unitInitializer = unitInitializer;
    this.world = world;
    this.room = room;
    this.gameInstanceServer = gameInstanceServer;
    this.currentTurn = 1;
  }

  public void startGame(int width, int height, long mapType) {
    mapInitializer.get().initializeMap(width, height, mapType);
    technologyInitializer.get().initializeTechnologies();
    materialInitializer.get().initializeMaterials();
    unitInitializer.get().initializeStartingUnits();
    playerOrder = new ArrayDeque<>();
    for (Client client : room.getClients()) {
      client.setTurnNumber(0);
      playerOrder.add(client);
    }
    changeToNextPlayer();
    world.process();
  }

  public Client changeToNextPlayer() {
    activePlayer = playerOrder.remove();
    activePlayer.setTurnNumber(currentTurn);
    playerOrder.add(activePlayer);
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

  public boolean isLastPlayer() {
    if (activePlayer.getTurnNumber() == currentTurn) {
      currentTurn += 1;
      return true;
    }
    return false;
  }

}
