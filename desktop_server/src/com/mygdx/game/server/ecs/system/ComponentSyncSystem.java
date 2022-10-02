package com.mygdx.game.server.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.utils.Bits;
import com.mygdx.game.core.ecs.component.Name;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.ecs.component.ChangeSubscribers;
import com.mygdx.game.server.ecs.component.DirtyComponents;
import com.mygdx.game.server.ecs.component.FriendlyOrFoe;
import com.mygdx.game.server.ecs.component.SharedComponents;
import com.mygdx.game.server.model.GameRoom;
import com.mygdx.game.server.network.gameinstance.StateSyncer;
import com.mygdx.game.server.network.gameinstance.services.RemoveClientEntityService;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@All({ChangeSubscribers.class, SharedComponents.class, FriendlyOrFoe.class})
@GameInstanceScope
public class ComponentSyncSystem extends IteratingSystem {

  private final StateSyncer stateSyncer;
  private final Lazy<RemoveClientEntityService> removeClientEntityService;
  private final GameRoom gameRoom;

  private ComponentMapper<ChangeSubscribers> changeSubscribersMapper;
  private ComponentMapper<DirtyComponents> dirtyComponentsMapper;
  private ComponentMapper<FriendlyOrFoe> friendlyOrFoeMapper;
  private ComponentMapper<Name> nameComponentMapper;
  private ComponentMapper<SharedComponents> sharedComponentsMapper;

  @Inject
  public ComponentSyncSystem(
      StateSyncer stateSyncer,
      Lazy<RemoveClientEntityService> removeClientEntityService,
      GameRoom gameRoom
  ) {
    this.stateSyncer = stateSyncer;
    this.removeClientEntityService = removeClientEntityService;
    this.gameRoom = gameRoom;
  }

  @Override
  protected void begin() {
    super.begin();
    log.info("process ComponentSyncSystem");
  }

  @Override
  protected void process(int entityId) {
    var dirtyFlags = dirtyComponentsMapper.create(entityId);
    handleUnsubscribedClients(entityId);
    handleFoes(entityId);
    handleFriendlies(entityId);
    dirtyFlags.getDirtyComponents().clear();
  }

  @Override
  protected void end() {
    stateSyncer.flush();
    super.end();
  }

  /*
  1. entity created -> all data sent
  2. entity comp updated -> only that data sent
  3. entity comp updated and then removed from world and then deleted from client
   - problem -> componentsyncsystem is not the only place which updates the state .......... it can be thought that
     updating the whole entity is not a "component sync" problem -> this means I can "handle removing stuff in another system"
   */

  private void handleUnsubscribedClients(int entityId) {
    var clientsToUpdate = changeSubscribersMapper.get(entityId);
    var subscribers = clientsToUpdate.getClients();
    var changedSubscriptionState = new Bits(clientsToUpdate.getChangedSubscriptionState());
    for (int i = 0; i < gameRoom.getNumberOfClients(); i++) {
      if (!subscribers.get(i) && changedSubscriptionState.getAndClear(i)) {
        log.info("removed entity " + entityId);
        var client = gameRoom.getClients().get(i);
        removeClientEntityService.get().removeEntity(entityId, client);
      }
    }
  }

  private void handleFoes(int entityId) {
    var changeSubscribers = changeSubscribersMapper.get(entityId);
    var clients = changeSubscribers.getClients();
    var changedSubscriptionState = new Bits(changeSubscribers.getChangedSubscriptionState());
    var foes = new Bits(clients);
    foes.andNot(friendlyOrFoeMapper.get(entityId).getFriendlies());
    changedSubscriptionState.andNot(friendlyOrFoeMapper.get(entityId).getFriendlies());
    var foeComponents = sharedComponentsMapper.get(entityId).getFoes();
    sendComponentsToClients(entityId, foes, foeComponents, changedSubscriptionState);
  }

  private void handleFriendlies(int entityId) {
    var changeSubscribers = changeSubscribersMapper.get(entityId);
    var clients = changeSubscribers.getClients();
    var changedSubscriptionState = new Bits(changeSubscribers.getChangedSubscriptionState());
    var friendlies = new Bits(clients);
    friendlies.and(friendlyOrFoeMapper.get(entityId).getFriendlies());
    changedSubscriptionState.and(friendlyOrFoeMapper.get(entityId).getFriendlies());
    log.info("id: " + entityId + " friendlyorfoe bits: " + friendlyOrFoeMapper.get(entityId).getFriendlies()
        + " change subscribers:" + clients + " changed subscription state: " + changedSubscriptionState
    + " friendlies:" + friendlies + " changed subscription state " + changedSubscriptionState );
    var friendComponents = sharedComponentsMapper.get(entityId).getFriendlies();
    sendComponentsToClients(entityId, friendlies, friendComponents, changedSubscriptionState);
  }

  private void sendComponentsToClients(int entityId, Bits clients, Bits components, Bits changedSubscriptionState) {
    var dirtyFlags = dirtyComponentsMapper.get(entityId);
    for (
        var clientIndex = clients.nextSetBit(0);
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
        if (!dirtyFlags.getDirtyComponents().get(mapperIndex) && !changedSubscriptionState.get(clientIndex)) {
          log.info("name:" +  nameComponentMapper.get(entityId) + " skipped because no changes");
          continue; // skip if component wasn't changed and the client does not need all the data
        }
        var mapper = world.getMapper(mapperIndex);
        if (!mapper.has(entityId)) {
          log.info("skipped because entity does not have that component " + mapper.type.getType().getName());
          continue;
        }
        var componentToSend = mapper.get(entityId);
        log.info("sending name:" +  nameComponentMapper.get(entityId) + " to " + client.getPlayerToken());
        stateSyncer.sendComponentTo(componentToSend, entityId, client);
      }
    }
  }
}
