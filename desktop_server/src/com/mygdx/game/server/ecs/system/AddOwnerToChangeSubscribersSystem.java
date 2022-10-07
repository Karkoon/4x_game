package com.mygdx.game.server.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.mygdx.game.core.ecs.component.Name;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.server.ecs.component.ChangeSubscribers;
import com.mygdx.game.server.model.GameRoom;
import lombok.extern.java.Log;

import javax.inject.Inject;

@All({ChangeSubscribers.class, Owner.class})
@Log
public class AddOwnerToChangeSubscribersSystem extends IteratingSystem {

  private final GameRoom gameRoom;
  private ComponentMapper<ChangeSubscribers> changeSubscribersComponentMapper;
  private ComponentMapper<Name> nameComponentMapper;
  private ComponentMapper<Owner> ownerComponentMapper;

  @Inject
  public AddOwnerToChangeSubscribersSystem(
      GameRoom gameRoom
  ) {
    this.gameRoom = gameRoom;
  }

  @Override
  protected void process(int entityId) {
    var owner = ownerComponentMapper.get(entityId).getToken();
    var name = nameComponentMapper.create(entityId);
    var clientIndex = gameRoom.getOrderNumber(gameRoom.getClientByToken(owner));
    var subscribers = changeSubscribersComponentMapper.get(entityId);
    if (!subscribers.getClients().get(clientIndex)) {
      log.info("added owner to " + name);
      subscribers.getClients().set(clientIndex);
    }
  }
}
