package com.mygdx.game.server.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.mygdx.game.core.ecs.component.Name;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.server.ecs.component.SightlineSubscribers;
import com.mygdx.game.server.model.GameRoom;
import lombok.extern.java.Log;

import javax.inject.Inject;

@All({Owner.class, SightlineSubscribers.class})
@Log
public class AddOwnerToSightlineSubscribersSystem extends IteratingSystem {

  private final GameRoom gameRoom;

  private ComponentMapper<Owner> ownerMapper;
  private ComponentMapper<Name> nameComponentMapper;
  private ComponentMapper<SightlineSubscribers> sightlineSubscribersMapper;

  @Inject
  public AddOwnerToSightlineSubscribersSystem(
      GameRoom gameRoom
  ) {
    this.gameRoom = gameRoom;
  }

  @Override
  protected void process(int entityId) {
    var owner = ownerMapper.get(entityId).getToken();
    var name = nameComponentMapper.create(entityId);
    var clientIndex = gameRoom.getOrderNumber(gameRoom.getClientByToken(owner));
    var sightlineSubscribers = sightlineSubscribersMapper.get(entityId);
    log.info("added sightline subscriber " + owner + " to" + name);
    sightlineSubscribers.setClient(clientIndex);
  }
}
