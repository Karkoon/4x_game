package com.mygdx.game.client.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.annotations.All;
import com.artemis.annotations.AspectDescriptor;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.utils.IntSet;
import com.mygdx.game.client.ecs.component.Visible;
import com.mygdx.game.client.model.InField;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.core.ecs.component.Building;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.core.ecs.component.SubField;
import com.mygdx.game.core.ecs.component.UnderConstruction;
import com.mygdx.game.core.util.DistanceUtil;
import lombok.extern.java.Log;

import javax.inject.Inject;

@All({Coordinates.class, Stats.class, Owner.class})
@GameInstanceScope
@Log
public class VisibilitySystem extends IteratingSystem {

  private final InField inField;
  private final IntSet visible = new IntSet();

  @AspectDescriptor(all = { Coordinates.class }, exclude = { SubField.class, Building.class, UnderConstruction.class})
  private EntitySubscription allThatCanBePerceived;

  private ComponentMapper<Coordinates> coordinatesMapper;
  private ComponentMapper<Owner> ownerMapper;
  private ComponentMapper<Stats> statsMapper;
  private ComponentMapper<Visible> visibleMapper;

  @Inject
  public VisibilitySystem(
      InField inField
  ) {
    super();
    this.inField = inField;
  }

  @Override
  protected void process(int perceiver) {
    var coordinates = coordinatesMapper.get(perceiver);
    var sightlineRadius = statsMapper.get(perceiver).getSightRadius();
    for (int i = 0; i < allThatCanBePerceived.getEntities().size(); i++) {
      var perceivableEntity = allThatCanBePerceived.getEntities().get(i);
      var perceivableCoords = coordinatesMapper.get(perceivableEntity);
      visibleMapper.set(perceivableEntity, false);
      if (DistanceUtil.distance(coordinates, perceivableCoords) <= sightlineRadius) {
        visible.add(perceivableEntity);
      }
    }
  }

  @Override
  protected void end() {
    if (inField.isInField()) { // todo do it differently
      visible.clear();
      return;
    }
    var iterator = visible.iterator();
    while (iterator.hasNext) {
      var visible = iterator.next();
      visibleMapper.set(visible, true);
    }
    iterator.reset();
    visible.clear();
  }
}
