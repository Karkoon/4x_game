package com.mygdx.game.server.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.utils.Bits;
import com.mygdx.game.server.ecs.component.ChangeSubscribers;
import com.mygdx.game.server.ecs.component.DirtyComponents;
import com.mygdx.game.server.ecs.component.FriendlyOrFoe;
import com.mygdx.game.server.ecs.component.SharedComponents;
import com.mygdx.game.server.model.GameRoom;
import com.mygdx.game.server.network.GameRoomSyncer;

import javax.inject.Inject;

@All({ChangeSubscribers.class, SharedComponents.class, FriendlyOrFoe.class})
public class ComponentSyncSystem extends IteratingSystem {

  private final GameRoomSyncer gameRoomSyncer;
  private final GameRoom gameRoom;
  private ComponentMapper<ChangeSubscribers> clientsToUpdateMapper;
  private ComponentMapper<SharedComponents> sharedComponentsMapper;
  private ComponentMapper<FriendlyOrFoe> friendlyOrFoeMapper;
  private ComponentMapper<DirtyComponents> dirtyComponentsMapper;


  @Inject
  public ComponentSyncSystem(
      GameRoomSyncer gameRoomSyncer,
      GameRoom gameRoom
  ) {
    this.gameRoomSyncer = gameRoomSyncer;
    this.gameRoom = gameRoom;
  }

  @Override
  protected void process(int entityId) {
    handleFoes(entityId);
    handleFriendlies(entityId);
  }

  private void handleFoes(int entityId) {
    var clientsToUpdate = clientsToUpdateMapper.get(entityId);
    var clients = clientsToUpdate.getClients();
    var needAllData = clientsToUpdate.getChangedSubscriptionState();
    var foes = new Bits(clients);
    foes.andNot(friendlyOrFoeMapper.get(entityId).getFriendlies());
    var foeComponents = sharedComponentsMapper.get(entityId).getFoes();
    sendComponentsToClients(entityId, foes, foeComponents, needAllData);
  }

  private void handleFriendlies(int entityId) {
    var clientsToUpdate = clientsToUpdateMapper.get(entityId);
    var clients = clientsToUpdate.getClients();
    var needAllData = clientsToUpdate.getChangedSubscriptionState();
    var friendlies = new Bits(clients);
    friendlies.and(friendlyOrFoeMapper.get(entityId).getFriendlies());
    var friendComponents = sharedComponentsMapper.get(entityId).getFriendlies();
    sendComponentsToClients(entityId, friendlies, friendComponents, needAllData);
  }

  private void sendComponentsToClients(int entityId, Bits clients, Bits components, Bits needAllData) {
    var dirtyFlags = dirtyComponentsMapper.get(entityId);
    for (
        int clientIndex = clients.nextSetBit(0);
        clientIndex != -1;
        clientIndex = clients.nextSetBit(clientIndex)
    ) {
      var client = gameRoom.getClients().get(clientIndex);
      for (
          var mapperIndex = components.nextSetBit(0);
          mapperIndex != -1;
          mapperIndex = components.nextSetBit(mapperIndex)
      ) {
        if (dirtyFlags.getDirtyComponents().get(mapperIndex) && !needAllData.get(clientIndex)) {
          continue; // skip if component wasn't changed and the client does not need all the data
        }
        var mapper = world.getMapper(mapperIndex);
        var componentToSend = mapper.get(entityId);

        gameRoomSyncer.sendComponentTo(componentToSend, entityId, client);
      }
    }
    dirtyFlags.getDirtyComponents().clear();
  }
}
