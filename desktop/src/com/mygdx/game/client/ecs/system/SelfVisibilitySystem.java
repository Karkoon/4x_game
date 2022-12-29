package com.mygdx.game.client.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.mygdx.game.client.ecs.component.Visible;
import com.mygdx.game.client.model.InField;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.Owner;
import lombok.extern.java.Log;

import javax.inject.Inject;

@All({Coordinates.class, Owner.class, Field.class})
@GameInstanceScope
@Log
public class SelfVisibilitySystem extends IteratingSystem {

  private final InField inField;

  private ComponentMapper<Visible> visibleMapper;
  private ComponentMapper<Owner> ownerMapper;

  @Inject
  public SelfVisibilitySystem(
      InField inField
  ) {
    super();
    this.inField = inField;
  }

  @Override
  protected void process(int perceivable) {
    if (inField.isInField()) return;
    visibleMapper.set(perceivable, true);
  }
}
