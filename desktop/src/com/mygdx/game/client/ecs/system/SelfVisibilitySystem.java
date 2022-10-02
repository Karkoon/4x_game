package com.mygdx.game.client.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.mygdx.game.client.ecs.component.Visible;
import com.mygdx.game.client.model.InField;
import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.Owner;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@All({Coordinates.class, Owner.class, Field.class})
@Log
@Singleton
public class SelfVisibilitySystem extends IteratingSystem {

  private final InField inField;
  private final PlayerInfo playerInfo;
  private ComponentMapper<Visible> visibleMapper;
  private ComponentMapper<Owner> ownerMapper;

  @Inject
  public SelfVisibilitySystem(
      InField inField,
      PlayerInfo playerInfo
  ) {
    super();
    this.inField = inField;
    this.playerInfo = playerInfo;
  }

  @Override
  protected void process(int perceivable) {
    if (inField.isInField()) return;
    visibleMapper.set(perceivable, true);
  }
}