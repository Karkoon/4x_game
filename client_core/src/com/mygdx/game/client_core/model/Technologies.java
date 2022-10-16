package com.mygdx.game.client_core.model;

import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.artemis.utils.IntBag;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.core.ecs.component.Technology;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class Technologies {

  @AspectDescriptor(all = {Technology.class})
  private EntitySubscription allTechnologies;

  @Inject
  public Technologies(
      World world
  ) {
    world.inject(this);
  }

  public IntBag getAllTechnologies() {
    return allTechnologies.getEntities();
  }

}
