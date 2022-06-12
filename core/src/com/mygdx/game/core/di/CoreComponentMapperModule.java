package com.mygdx.game.core.di;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.core.ecs.component.Name;
import com.mygdx.game.core.ecs.component.Position;
import com.mygdx.game.core.ecs.component.Slot;
import com.mygdx.game.core.ecs.component.Stats;
import dagger.Module;
import dagger.Provides;
import lombok.extern.java.Log;

@Module
@Log
public class CoreComponentMapperModule {
  @Provides
  public ComponentMapper<Name> nameComponentMapper(World world) {
    return world.getMapper(Name.class);
  }

  @Provides
  public ComponentMapper<Position> positionComponentMapper(World world) {
    return world.getMapper(Position.class);
  }

  @Provides
  public ComponentMapper<Slot> slotComponentMapper(World world) {
    return world.getMapper(Slot.class);
  }

  @Provides
  public ComponentMapper<Stats> statsComponentMapper(World world) {
    return world.getMapper(Stats.class);
  }
}
