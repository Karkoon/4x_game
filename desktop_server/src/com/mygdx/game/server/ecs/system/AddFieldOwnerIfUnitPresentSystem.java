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
import com.mygdx.game.server.ecs.component.DirtyComponents;
import lombok.extern.java.Log;

import javax.inject.Inject;

@All({Field.class, Coordinates.class})
@Log
public class AddFieldOwnerIfUnitPresentSystem extends IteratingSystem {

  @AspectDescriptor(all = {Unit.class, Coordinates.class})
  private EntitySubscription units;

  private ComponentMapper<Coordinates> coordinatesMapper;
  private ComponentMapper<Owner> ownerMapper;
  private ComponentMapper<DirtyComponents> dirtyMapper;

  @Inject
  public AddFieldOwnerIfUnitPresentSystem() {
    super();
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
          fieldOwner.setToken(unitOwner.getToken());
          setDirty(fieldId, Owner.class, world);
          log.info("added owner to field");
        }
      }
    }
  }

  protected void setDirty(int entityId, Class component, World world) {
    var componentIndex = world.getComponentManager().getTypeFactory().getIndexFor(component);
    dirtyMapper.create(entityId).getDirtyComponents().set(componentIndex);
  }
}
