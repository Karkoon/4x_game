package com.mygdx.game.server.network.gameinstance.services;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.SubField;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.ecs.component.ChangeSubscribers;
import com.mygdx.game.server.model.Client;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class ShowSubfieldService extends WorldService {


  @AspectDescriptor(all = {ChangeSubscribers.class}, exclude = {SubField.class})
  private ComponentMapper<Field> fieldMapper;
  private ComponentMapper<ChangeSubscribers> changeSubscribersMapper;
  private final World world;

  @Inject
  ShowSubfieldService(
      World world
  ) {
    world.inject(this);
    this.world = world;
  }

  public void flipSubscriptionState(int parentId, Client client) {
    var clientIndex = client.getGameRoom().getClients().indexOf(client);
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
