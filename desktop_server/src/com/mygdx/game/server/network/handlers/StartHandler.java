package com.mygdx.game.server.network.handlers;

import com.mygdx.game.server.initialize.MapInitializer;
import com.mygdx.game.server.initialize.StartUnitInitializer;
import com.mygdx.game.server.initialize.TechnologyInitializer;
import com.mygdx.game.server.model.GameRoom;
import com.mygdx.game.server.network.EndTurnService;
import com.mygdx.game.server.network.GameRoomSyncer;
import io.vertx.core.buffer.Buffer;

import javax.inject.Inject;

import static com.badlogic.gdx.net.HttpRequestBuilder.json;

public class StartHandler {

  private final GameRoomSyncer syncer;
  private final TechnologyInitializer technologyInitializer;
  private final MapInitializer mapInitializer;
  private final StartUnitInitializer unitInitializer;
  private final GameRoom room;
  private final EndTurnService endTurnService;

  @Inject
  public StartHandler(
      GameRoomSyncer syncer,
      TechnologyInitializer technologyInitializer,
      MapInitializer mapInitializer,
      StartUnitInitializer unitInitializer,
      GameRoom room,
      EndTurnService endTurnService
  ) {

    this.syncer = syncer;
    this.technologyInitializer = technologyInitializer;
    this.mapInitializer = mapInitializer;
    this.unitInitializer = unitInitializer;
    this.room = room;
    this.endTurnService = endTurnService;
  }

  public void handle(String[] commands) {
    var width = Integer.parseInt(commands[1]);
    var height = Integer.parseInt(commands[2]);
    var mapType = Long.parseLong(commands[3]);
    syncer.beginTransaction();
    technologyInitializer.initializeTechnologies();
    mapInitializer.initializeMap(width, height, mapType);
    unitInitializer.initializeTestUnit();
    syncer.endTransaction();
    room.getClients().forEach(ws -> {
      var msg = new GameStartedMessage(room.getClient(0).getPlayerToken());
      var buffer = Buffer.buffer(json.toJson(msg, (Class<?>) null));
      ws.getSocket().write(buffer);
    });
    endTurnService.init();
  }
}
