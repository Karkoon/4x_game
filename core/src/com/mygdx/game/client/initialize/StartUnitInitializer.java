package com.mygdx.game.client.initialize;

import com.badlogic.ashley.core.Entity;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.Slot;
import com.mygdx.game.client.ecs.entityfactory.UnitFactory;
import com.mygdx.game.client.model.Coordinates;
import com.mygdx.game.config.UnitConfig;
import lombok.NonNull;

import javax.inject.Inject;
import java.util.Map;

public final class StartUnitInitializer {

  private final UnitFactory unitFactory;
  private final GameScreenAssets assets;

  @Inject
  public StartUnitInitializer(@NonNull UnitFactory unitFactory,
                              @NonNull GameScreenAssets assets) {
    this.unitFactory = unitFactory;
    this.assets = assets;
  }

  public void initializeTestUnit(Map<Coordinates, Entity> fieldList) {
    var initialCoordinates = new Coordinates(0, 0);
    var anyConfig = assets.getGameConfigs().getAny(UnitConfig.class);
    var unitEntity = unitFactory.createEntity(anyConfig, initialCoordinates);

    var slot = fieldList.get(initialCoordinates).getComponent(Slot.class);
    slot.getEntities().add(unitEntity);
  }
}
