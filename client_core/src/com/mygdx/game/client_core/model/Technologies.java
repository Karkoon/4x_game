package com.mygdx.game.client_core.model;

import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.artemis.utils.IntBag;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class Technologies {

  @AspectDescriptor
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
