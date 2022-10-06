package com.mygdx.game.server.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.mygdx.game.core.ecs.component.Name;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.server.ecs.component.SightlineSubscribers;
import com.mygdx.game.server.model.GameRoom;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@All({Owner.class, SightlineSubscribers.class})
@Log
public class AddOwnerToSightlineSubscribersSystem extends IteratingSystem {

  private final GameRoom room;

  private ComponentMapper<Owner> ownerMapper;
  private ComponentMapper<Name> nameComponentMapper;
  private ComponentMapper<SightlineSubscribers> sightlineSubscribersMapper;

  @Inject
  public AddOwnerToSightlineSubscribersSystem(
      GameRoom room
  ) {
    super();
    this.room = room;
  }

  @Override
  protected void process(int entityId) {
    var owner = room.getClientByToken(ownerMapper.get(entityId).getToken());
    sightlineSubscribersMapper.get(entityId).setClient(room.getOrderNumber(owner));
    log.info("added sightline subscriber to" + nameComponentMapper.get(entityId));
  }
}
