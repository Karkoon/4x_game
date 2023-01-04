package com.mygdx.game.server.network.gameinstance.services;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.core.network.messages.RemoveEntityMessage;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.ecs.component.ChangeSubscribers;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameRoom;
import com.mygdx.game.server.network.MessageSender;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class RemoveClientEntityService {

  private final GameRoom gameRoom;
  private final MessageSender messageSender;

  private ComponentMapper<ChangeSubscribers> subscribersComponentMapper;


  @Inject
  RemoveClientEntityService(
      GameRoom gameRoom,
      MessageSender messageSender,
      World world
  ) {
    super();
    this.gameRoom = gameRoom;
    this.messageSender = messageSender;
    world.inject(this);
  }

  public void removeEntity(int entityId, @NonNull Client client) {
    var msg = new RemoveEntityMessage(entityId);
    messageSender.send(msg, client);
  }

  public void removeEntityForAll(int entityId) {
    var clients = subscribersComponentMapper.get(entityId).getClients();
    var msg = new RemoveEntityMessage(entityId);
    for (
        var clientIndex = clients.nextSetBit(0);
        clientIndex != -1;
        clientIndex = clients.nextSetBit(clientIndex + 1)
    ) {
      var client = gameRoom.getClients().get(clientIndex);
      messageSender.send(msg, client);
    }
  }
}
