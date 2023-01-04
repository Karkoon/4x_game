package com.mygdx.game.server.initialize;

import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntSet;
import com.mygdx.game.config.CivilizationConfig;
import com.mygdx.game.config.GameConfigs;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.GameRoom;
import com.mygdx.game.server.network.gameinstance.services.CreateUnitService;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class StartUnitInitializer {

  private final CreateUnitService createUnitService;
  private final GameConfigs gameConfigs;
  private final GameRoom gameRoom;
  private final IntSet usedMapPositions = new IntSet();

  @Inject
  public StartUnitInitializer(
      CreateUnitService createUnitService,
      GameRoom gameRoom,
      GameConfigs gameConfigs
  ) {
    this.createUnitService = createUnitService;
    this.gameConfigs = gameConfigs;
    this.gameRoom = gameRoom;
  }

  public void initializeStartingUnits(IntArray map) {
    for (int i = 0; i < gameRoom.getNumberOfClients(); i++) {
      var client = gameRoom.getClients().get(i);
      log.info("client id " + client.getPlayerToken());
      var config = gameConfigs.get(CivilizationConfig.class, client.getCivId()).getStartingUnit();
      var fieldId = getUnusedField(map);
      createUnitService.createUnit(config, fieldId, client, true);
    }
  }

  private int getUnusedField(IntArray map) {
    var fieldId = map.random();
    while (usedMapPositions.contains(fieldId)) {
      fieldId = map.random();
      if (usedMapPositions.size == map.size) {
        throw new RuntimeException("all positions have been used");
      }
    }
    usedMapPositions.add(fieldId);
    return fieldId;
  }
}
