package com.mygdx.game.core.di;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.core.ecs.component.Name;
import com.mygdx.game.core.ecs.component.Position;
import com.mygdx.game.core.ecs.component.Slot;
import com.mygdx.game.core.ecs.component.Stats;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import lombok.extern.java.Log;

@Module
@Log
public class CoreComponentMapperModule {
  @Provides
  @Reusable
  public ComponentMapper<Name> nameComponentMapper(World world) {
    return new ComponentMapper<>(Name.class, world);
  }

  @Provides
  @Reusable
  public ComponentMapper<Position> positionComponentMapper(World world) {
    return new ComponentMapper<>(Position.class, world);
  }

  @Provides
  @Reusable
  public ComponentMapper<Slot> slotComponentMapper(World world) {
    return new ComponentMapper<>(Slot.class, world);
  }

  @Provides
  @Reusable
  public ComponentMapper<Stats> statsComponentMapper(World world) {
    return new ComponentMapper<>(Stats.class, world);
  }
}
