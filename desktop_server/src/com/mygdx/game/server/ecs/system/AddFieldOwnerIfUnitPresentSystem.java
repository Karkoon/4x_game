package com.mygdx.game.server.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.All;
import com.artemis.annotations.AspectDescriptor;
import com.artemis.systems.IteratingSystem;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.Unit;
import com.mygdx.game.server.ecs.component.ChangeSubscribers;
import com.mygdx.game.server.ecs.component.DirtyComponents;
import com.mygdx.game.server.ecs.component.FriendlyOrFoe;
import com.mygdx.game.server.ecs.component.SightlineSubscribers;
import com.mygdx.game.server.model.GameRoom;
import com.mygdx.game.server.network.gameinstance.services.RemoveClientEntityService;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@All({Field.class, Coordinates.class})
@Log
public class AddFieldOwnerIfUnitPresentSystem extends IteratingSystem {

  private final GameRoom room;
  private final Lazy<RemoveClientEntityService> service;
  @AspectDescriptor(all = {Unit.class, Coordinates.class})
  private EntitySubscription units;

  private ComponentMapper<Coordinates> coordinatesMapper;
  private ComponentMapper<Owner> ownerMapper;
  private ComponentMapper<DirtyComponents> dirtyMapper;
  private ComponentMapper<ChangeSubscribers> changeSubscribersComponentMapper;
  private ComponentMapper<SightlineSubscribers> sightlineSubscribersComponentMapper;
  private ComponentMapper<FriendlyOrFoe> friendlyOrFoeComponentMapper;

  @Inject
  public AddFieldOwnerIfUnitPresentSystem(
      GameRoom room,
      Lazy<RemoveClientEntityService> service
  ) {
    this.room = room;
    this.service = service;
  }

  @Override
  protected void process(int fieldId) {
    var unitEntities = units.getEntities();
    for (int i = 0; i < unitEntities.size(); i++) {
      var unitId = unitEntities.get(i);
      if (coordinatesMapper.get(unitId).equals(coordinatesMapper.get(fieldId))) {
        var fieldOwner = ownerMapper.create(fieldId);
        var unitOwner = ownerMapper.get(unitId);
        if (!unitOwner.getToken().equals(fieldOwner.getToken())) {
          removePreviousOwnerFromField(fieldId, fieldOwner.getToken());
          addNewOwner(fieldId, unitOwner.getToken());
          fieldOwner.setToken(unitOwner.getToken());
          setDirty(fieldId, Owner.class, world);
          log.info("added owner to field");
        }
      }
    }
  }

  private void removePreviousOwnerFromField(int fieldId, String previousOwner) {
    if (previousOwner == null) return;
    var previousOwnerClient = room.getClientByToken(previousOwner);
    var previousOwnerIndex = room.getClients().indexOf(previousOwnerClient);
    friendlyOrFoeComponentMapper.get(fieldId).getFriendlies().clear(previousOwnerIndex);
    sightlineSubscribersComponentMapper.get(fieldId).getClients().clear(previousOwnerIndex);
    changeSubscribersComponentMapper.get(fieldId).getClients().clear(previousOwnerIndex);
    service.get().removeEntity(fieldId, previousOwnerClient);
  }

  private void addNewOwner(int fieldId, String newOwner) {
    var newOwnerIndex = room.getClients().indexOf(room.getClientByToken(newOwner));
    friendlyOrFoeComponentMapper.get(fieldId).getFriendlies().set(newOwnerIndex);
    sightlineSubscribersComponentMapper.get(fieldId).getClients().set(newOwnerIndex);
    changeSubscribersComponentMapper.get(fieldId).getClients().set(newOwnerIndex);
  }

  protected void setDirty(int entityId, Class component, World world) {
    var componentIndex = world.getComponentManager().getTypeFactory().getIndexFor(component);
    dirtyMapper.create(entityId).getDirtyComponents().set(componentIndex);
  }
}
