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
import com.mygdx.game.server.network.StateSyncer;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@All({ChangeSubscribers.class, SharedComponents.class, FriendlyOrFoe.class})
public class ComponentSyncSystem extends IteratingSystem {

  private final StateSyncer stateSyncer;
  private final GameRoom gameRoom;
  private ComponentMapper<ChangeSubscribers> clientsToUpdateMapper;
  private ComponentMapper<SharedComponents> sharedComponentsMapper;
  private ComponentMapper<FriendlyOrFoe> friendlyOrFoeMapper;
  private ComponentMapper<DirtyComponents> dirtyComponentsMapper;


  @Inject
  public ComponentSyncSystem(
      StateSyncer stateSyncer,
      GameRoom gameRoom
  ) {
    this.stateSyncer = stateSyncer;
    this.gameRoom = gameRoom;
  }

  @Override
  protected void begin() {
    super.begin();
    log.info("process ComponentSyncSystem");
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
    var dirtyFlags = dirtyComponentsMapper.create(entityId);
    for (
        int clientIndex = clients.nextSetBit(0);
        clientIndex != -1;
        clientIndex = clients.nextSetBit(clientIndex + 1)
    ) {
      var client = gameRoom.getClients().get(clientIndex);
      stateSyncer.beginTransaction(client); //problematic
      for (
          var mapperIndex = components.nextSetBit(0);
          mapperIndex != -1;
          mapperIndex = components.nextSetBit(mapperIndex + 1)
      ) {
        if (dirtyFlags.getDirtyComponents().get(mapperIndex) && !needAllData.get(clientIndex)) {
          continue; // skip if component wasn't changed and the client does not need all the data
        }
        var mapper = world.getMapper(mapperIndex);
        var componentToSend = mapper.get(entityId);

        stateSyncer.sendComponentTo(componentToSend, entityId, client);
      }
      stateSyncer.endTransaction(client);
    }
    dirtyFlags.getDirtyComponents().clear();
  }
}
