package com.mygdx.game.server.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.annotations.All;
import com.artemis.annotations.AspectDescriptor;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.utils.Bits;
import com.badlogic.gdx.utils.IntMap;
import com.mygdx.game.core.ecs.component.Building;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Name;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.SubField;
import com.mygdx.game.core.ecs.component.UnderConstruction;
import com.mygdx.game.core.util.DistanceUtil;
import com.mygdx.game.server.ecs.component.ChangeSubscribers;
import com.mygdx.game.server.ecs.component.SightlineSubscribers;
import lombok.extern.java.Log;

import javax.inject.Inject;

@All({Coordinates.class, SightlineSubscribers.class, Owner.class})
@Log
public class VisibilitySystem extends IteratingSystem {

  private final IntMap<Bits> entityToNewChangeSubscribers = new IntMap<>();

  @AspectDescriptor(all = {Coordinates.class, ChangeSubscribers.class}, exclude = { SubField.class, Building.class, UnderConstruction.class})
  private EntitySubscription allThatCanBePerceived;

  private ComponentMapper<ChangeSubscribers> changeSubscribersMapper;
  private ComponentMapper<Coordinates> coordinatesMapper;
  private ComponentMapper<Name> nameMapper;
  private ComponentMapper<SightlineSubscribers> sightlineSubscribersMapper;

  @Inject
  public VisibilitySystem() {
    super();
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
    log.info("processing perceiver: " + nameMapper.get(perceiver));
    for (int i = 0; i < allThatCanBePerceived.getEntities().size(); i++) {
      var perceivable = allThatCanBePerceived.getEntities().get(i);
      var perceivableCoords = coordinatesMapper.get(perceivable);
      if (DistanceUtil.distance(coordinates, perceivableCoords) <= sightlineRadius) {
        var changeSubscribers = obtainNewChangeSubscribers(allThatCanBePerceived.getEntities().get(i));
        changeSubscribers.or(sightlineSubscribers);
      }
    }
  }

  @Override
  protected void end() {
    log.info("visibility end");
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

  private Bits obtainNewChangeSubscribers(int entityId) {
    var subscribers = entityToNewChangeSubscribers.get(entityId);
    if (subscribers == null) {
      subscribers = new Bits();
      entityToNewChangeSubscribers.put(entityId, subscribers);
    }
    return subscribers;
  }

}
