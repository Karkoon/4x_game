package com.mygdx.game.server.network;

import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.server.ecs.component.ChangeSubscribers;
import com.mygdx.game.server.model.Client;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class ShowSubfieldService extends WorldService {

  @Inject
  ShowSubfieldService() {
    super();
  }

  public void flipSubscriptionState(int parentId, Client client) {
    var world = client.getGameRoom().getGameInstance().getWorld();
    var clientIndex = client.getGameRoom().getClients().indexOf(client);
    var fieldMapper = world.getMapper(Field.class);
    var changeSubscribersMapper = world.getMapper(ChangeSubscribers.class);
    var subfields = fieldMapper.get(parentId).getSubFields();
    for (int i = 0; i < subfields.size; i++) {
      var entityId = subfields.get(i);
      var changeSubscribersComp = changeSubscribersMapper.get(entityId);
      changeSubscribersComp.getChangedSubscriptionState().set(clientIndex);
      changeSubscribersComp.getClients().flip(clientIndex);
    }
    log.info("Shown subfield");
    world.process();
  }
}
