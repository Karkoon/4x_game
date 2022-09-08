package com.mygdx.game.server.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.annotations.All;
import com.artemis.annotations.AspectDescriptor;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.utils.Bits;
import com.badlogic.gdx.utils.IntMap;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.SubField;
import com.mygdx.game.server.ecs.component.ChangeSubscribers;
import com.mygdx.game.server.ecs.component.SightlineSubscribers;
import lombok.extern.java.Log;

import javax.inject.Inject;

@All({Coordinates.class, SightlineSubscribers.class, Owner.class})
@Log
public class VisibilitySystem extends IteratingSystem {

  @AspectDescriptor(all = {Coordinates.class, ChangeSubscribers.class}, exclude = { SubField.class })
  private EntitySubscription allThatCanBePerceived;
  private final IntMap<Bits> entityToNewChangeSubscribers = new IntMap<>();
  private ComponentMapper<SightlineSubscribers> sightlineSubscribersMapper;
  private ComponentMapper<ChangeSubscribers> changeSubscribersMapper;
  private ComponentMapper<Coordinates> coordinatesMapper;

  @Inject
  public VisibilitySystem() {
    super();
  }

  private Bits obtainNewChangeSubscribers(int entityId) {
    var subscribers = entityToNewChangeSubscribers.get(entityId);
    if (subscribers == null) {
      subscribers = new Bits();
      entityToNewChangeSubscribers.put(entityId, subscribers);
    }
    return subscribers;
  }

  @Override
  protected void begin() {
    log.info("process VisibilitySystem ");
    entityToNewChangeSubscribers.clear();
  }

  @Override
  protected void process(int perceiver) {
    var coordinates = coordinatesMapper.get(perceiver);
    var sightlineRadius = sightlineSubscribersMapper.get(perceiver).getSightlineRadius();
    var sightlineSubscribers = sightlineSubscribersMapper.get(perceiver).getClients();
    log.info(coordinates.toString());
    for (int i = 0; i < allThatCanBePerceived.getEntities().size(); i++) {
      var perceivableCoords = coordinatesMapper.get(allThatCanBePerceived.getEntities().get(i));
      var dst2 = Math.pow(coordinates.getX() - perceivableCoords.getX(), 2) + Math.pow(coordinates.getY() - perceivableCoords.getY(), 2);
      if (dst2 <= sightlineRadius * sightlineRadius) {
        var changeSubscribers = obtainNewChangeSubscribers(allThatCanBePerceived.getEntities().get(i));
        changeSubscribers.or(sightlineSubscribers);
      }
    }
  }

  @Override
  protected void end() {
    for (int i = 0; i < allThatCanBePerceived.getEntities().size(); i++) { // postprocessing
      var entityId = allThatCanBePerceived.getEntities().get(i);
      var changeSubscribersComp = changeSubscribersMapper.get(entityId);

      var newChangeSubscribers = obtainNewChangeSubscribers(entityId);

      var changedSubscriptionState = new Bits(changeSubscribersComp.getClients());
      changedSubscriptionState.xor(newChangeSubscribers);

      changeSubscribersComp.setChangedSubscriptionState(changedSubscriptionState);
      changeSubscribersComp.setClients(newChangeSubscribers);
    }
  }

}
