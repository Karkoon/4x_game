package com.mygdx.game.server.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.utils.Bits;
import com.mygdx.game.core.ecs.component.Name;
import com.mygdx.game.server.ecs.component.ChangeSubscribers;
import com.mygdx.game.server.ecs.component.DirtyComponents;
import com.mygdx.game.server.ecs.component.FriendlyOrFoe;
import com.mygdx.game.server.ecs.component.SharedComponents;
import com.mygdx.game.server.model.GameRoom;
import com.mygdx.game.server.network.RemoveEntityService;
import com.mygdx.game.server.network.StateSyncer;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@All({ChangeSubscribers.class, SharedComponents.class, FriendlyOrFoe.class})
public class ComponentSyncSystem extends IteratingSystem {

  private final StateSyncer stateSyncer;
  private final RemoveEntityService removeEntityService;
  private final GameRoom gameRoom;
  private ComponentMapper<ChangeSubscribers> clientsToUpdateMapper;
  private ComponentMapper<SharedComponents> sharedComponentsMapper;
  private ComponentMapper<FriendlyOrFoe> friendlyOrFoeMapper;
  private ComponentMapper<DirtyComponents> dirtyComponentsMapper;
  private ComponentMapper<Name> nameComponentMapper;


  @Inject
  public ComponentSyncSystem(
      StateSyncer stateSyncer,
      RemoveEntityService removeEntityService,
      GameRoom gameRoom
  ) {
    this.stateSyncer = stateSyncer;
    this.removeEntityService = removeEntityService;
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
    handleFoes(entityId);
    handleFriendlies(entityId);
    dirtyFlags.getDirtyComponents().clear();
  }

  @Override
  protected void end() {
    stateSyncer.flush();
    super.end();
  }

  private void handleFoes(int entityId) {
    var clientsToUpdate = clientsToUpdateMapper.get(entityId);
    var clients = clientsToUpdate.getClients();
    var changedSubscriptionState = new Bits(clientsToUpdate.getChangedSubscriptionState());
    var foes = new Bits(clients);
    foes.andNot(friendlyOrFoeMapper.get(entityId).getFriendlies());
    changedSubscriptionState.andNot(friendlyOrFoeMapper.get(entityId).getFriendlies());
    var foeComponents = sharedComponentsMapper.get(entityId).getFoes();
    sendComponentsToClients(entityId, foes, foeComponents, changedSubscriptionState);
  }

  private void handleFriendlies(int entityId) {
    var clientsToUpdate = clientsToUpdateMapper.get(entityId);
    var clients = clientsToUpdate.getClients();
    var changedSubscriptionState = new Bits(clientsToUpdate.getChangedSubscriptionState());
    var friendlies = new Bits(clients);
    friendlies.and(friendlyOrFoeMapper.get(entityId).getFriendlies());
    changedSubscriptionState.and(friendlyOrFoeMapper.get(entityId).getFriendlies());
    var friendComponents = sharedComponentsMapper.get(entityId).getFriendlies();
    sendComponentsToClients(entityId, friendlies, friendComponents, changedSubscriptionState);
  }

  private void sendComponentsToClients(int entityId, Bits clients, Bits components, Bits changedSubscriptionState) {
    var dirtyFlags = dirtyComponentsMapper.get(entityId);
    for (int clientIndex = 0; clientIndex < clients.length(); clientIndex++) {
      var client = gameRoom.getClients().get(clientIndex);
      // check if client stopped seeing entity and then send remove signal if so
      if (changedSubscriptionState.get(clientIndex) && !clients.get(clientIndex)) {
        log.info("removed entity " + entityId);
        removeEntityService.removeEntity(entityId, client);
        continue;
      }

      stateSyncer.beginTransaction(client); //problematic
      for (
          var mapperIndex = components.nextSetBit(0);
          mapperIndex != -1;
          mapperIndex = components.nextSetBit(mapperIndex + 1)
      ) {
        log.info("name: " + nameComponentMapper.get(entityId));
        if (!dirtyFlags.getDirtyComponents().get(mapperIndex) && !changedSubscriptionState.get(clientIndex)) {
          log.info("skipped because no changes");
          continue; // skip if component wasn't changed and the client does not need all the data
        }
        var mapper = world.getMapper(mapperIndex);
        var componentToSend = mapper.get(entityId);
        stateSyncer.sendComponentTo(componentToSend, entityId, client);
      }
    }
  }
}
