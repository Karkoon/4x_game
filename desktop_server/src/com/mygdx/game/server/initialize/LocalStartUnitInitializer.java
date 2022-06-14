package com.mygdx.game.server.initialize;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.Slot;
import com.mygdx.game.core.initialize.StartUnitInitializer;
import com.mygdx.game.core.model.Coordinates;
import com.mygdx.game.server.ecs.entityfactory.UnitFactory;
import lombok.NonNull;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public final class LocalStartUnitInitializer implements StartUnitInitializer {

  private final UnitFactory unitFactory;
  private final GameScreenAssets assets;
  private final ComponentMapper<Slot> slotMapper;

  @Inject
  public LocalStartUnitInitializer(
      @NonNull UnitFactory unitFactory,
      @NonNull GameScreenAssets assets,
      @NonNull World world) {
    this.unitFactory = unitFactory;
    this.assets = assets;
    this.slotMapper = world.getMapper(Slot.class);
  }

  public Map<Coordinates, Integer> initializeTestUnit(int field) {
    var fields = new HashMap<Coordinates, Integer>();
    var initialCoordinates = new Coordinates(0, 0);
    var anyConfig = assets.getGameConfigs().getAny(UnitConfig.class);
    var id = unitFactory.createEntity(anyConfig, initialCoordinates);
    var slot = slotMapper.get(field);
    slot.getEntities().add(id);
    fields.put(initialCoordinates, id);
    return fields;
  }
}
