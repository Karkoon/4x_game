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

@Log
@GameInstanceScope
public class RemoveClientEntityService {

  private final MessageSender sender;
  private final GameRoom room;
  private ComponentMapper<ChangeSubscribers> subscribersComponentMapper;


  @Inject
  RemoveClientEntityService(
      MessageSender sender,
      World world,
      GameRoom room
  ) {
    super();
    this.sender = sender;
    this.room = room;
    world.inject(this);
  }

  public void removeEntity(int entityId, @NonNull Client client) {
    var msg = new RemoveEntityMessage(entityId);
    sender.send(msg, client);
  }

  public void removeEntityForAll(int entityId) {
    var clients = subscribersComponentMapper.get(entityId).getClients();
    var msg = new RemoveEntityMessage(entityId);
    for (
        var clientIndex = clients.nextSetBit(0);
        clientIndex != -1;
        clientIndex = clients.nextSetBit(clientIndex + 1)
    ) {
      var client = room.getClients().get(clientIndex);
      sender.send(msg, client);
    }
  }
}
