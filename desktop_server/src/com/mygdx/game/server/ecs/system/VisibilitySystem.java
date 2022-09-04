package com.mygdx.game.server.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.utils.Bits;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntMap;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.core.util.QuadTreeInt;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.ecs.component.ChangeSubscribers;
import com.mygdx.game.server.ecs.component.SightlineSubscribers;

import javax.inject.Inject;

@GameInstanceScope
@All({SightlineSubscribers.class, Coordinates.class, Stats.class})
public class VisibilitySystem extends IteratingSystem {

  @Inject
  public VisibilitySystem() {
    super();
  }

  private final QuadTreeInt quadTree = new QuadTreeInt(300, 8); // todo zrobić testy czy faktycznie
  // opłaca się użyć quadtree i porównać do generowania ręcznego w jakimś radiusie koordynatów i sprawdzanie czy coś tam jest
  // + 3 strony do inżynierki czuję
  private final IntArray otherEntities = new IntArray(false, 16);
  private final IntMap<Bits> entityToNewChangeSubscribers = new IntMap<>();
  private ComponentMapper<SightlineSubscribers> sightlineSubscribersMapper;
  private ComponentMapper<ChangeSubscribers> changeSubscribersMapper;
  private ComponentMapper<Coordinates> coordinatesMapper;
  private ComponentMapper<Stats> statsMapper;

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
    quadTree.reset();
    entityToNewChangeSubscribers.clear();
    for (int i = 0; i < getEntityIds().size(); i++) { // preprocessing
      var entityId = getEntityIds().get(i);
      var coordinates = coordinatesMapper.get(i);
      quadTree.add(entityId, coordinates.getX(), coordinates.getY());
    }
  }

  @Override
  protected void process(int entityId) {
    var coordinates = coordinatesMapper.get(entityId);
    var stats = statsMapper.get(entityId);
    var sightlineSubscribers = sightlineSubscribersMapper.get(entityId).getClients();

    quadTree.query(coordinates.getX(), coordinates.getY(), stats.getSightRadius(), otherEntities); // result through otherEntities

    for (int i = 0; i < otherEntities.size; i++) { // filter jednostki tego samego gracza czy cos
      var otherEntity = otherEntities.get(i);
      var changeSubscribers = obtainNewChangeSubscribers(otherEntity);
      changeSubscribers.or(sightlineSubscribers);
    }
  }

  @Override
  protected void end() {
    for (int i = 0; i < getEntityIds().size(); i++) { // postprocessing
      var entityId = getEntityIds().get(i);
      var changeSubscribersComp = changeSubscribersMapper.get(entityId);

      var newChangeSubscribers = obtainNewChangeSubscribers(entityId);

      var changedSubscriptionState = new Bits(changeSubscribersComp.getClients());
      changedSubscriptionState.xor(newChangeSubscribers);

      changeSubscribersComp.setChangedSubscriptionState(changedSubscriptionState);
      changeSubscribersComp.setClients(newChangeSubscribers);
    }
  }

}
