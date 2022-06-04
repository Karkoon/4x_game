package com.mygdx.game.server.initialize;

import com.artemis.ComponentMapper;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.Slot;
import com.mygdx.game.core.initialize.StartUnitInitializer;
import com.mygdx.game.core.model.Coordinates;
import com.mygdx.game.server.ecs.entityfactory.UnitFactory;
import lombok.NonNull;

import javax.inject.Inject;

public final class LocalStartUnitInitializer implements StartUnitInitializer {

  private final UnitFactory unitFactory;
  private final GameScreenAssets assets;
  private final ComponentMapper<Slot> slotMapper;

  @Inject
  public LocalStartUnitInitializer(@NonNull UnitFactory unitFactory,
                                   @NonNull GameScreenAssets assets,
                                   @NonNull ComponentMapper<Slot> slotMapper) {
    this.unitFactory = unitFactory;
    this.assets = assets;
    this.slotMapper = slotMapper;
  }

  public void initializeTestUnit(int field) {
    var initialCoordinates = new Coordinates(0, 0);
    var anyConfig = assets.getGameConfigs().getAny(UnitConfig.class);
    var id = unitFactory.createEntity(anyConfig, initialCoordinates);
    var slot = slotMapper.get(field);
    slot.getEntities().add(id);
  }
}
