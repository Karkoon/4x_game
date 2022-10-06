package com.mygdx.game.server.initialize;

import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.ecs.entityfactory.UnitFactory;
import com.mygdx.game.server.model.GameRoom;

import javax.inject.Inject;

@GameInstanceScope
public class StartUnitInitializer {

  private final UnitFactory unitFactory;
  private final GameConfigAssets assets;
  private final GameRoom gameRoom;

  @Inject
  public StartUnitInitializer(
      UnitFactory unitFactory,
      GameConfigAssets assets,
      GameRoom gameRoom
  ) {
    this.unitFactory = unitFactory;
    this.assets = assets;
    this.gameRoom = gameRoom;
  }

  private int y = 0;
  public void initializeStartingUnits() {
    var anyConfig = assets.getGameConfigs().getAny(UnitConfig.class); // todo decide which unitconfig should be the starting unit
    for (int i = 0; i < gameRoom.getNumberOfClients(); i++) {
      unitFactory.createEntity(anyConfig, new Coordinates(0, y++), gameRoom.getClients().get(i));
    }
  }
}
