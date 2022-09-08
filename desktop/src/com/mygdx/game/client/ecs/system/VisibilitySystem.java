package com.mygdx.game.client.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.annotations.All;
import com.artemis.annotations.AspectDescriptor;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.utils.IntSet;
import com.mygdx.game.client.ecs.component.Visible;
import com.mygdx.game.client.model.InField;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.core.ecs.component.SubField;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@All({Coordinates.class, Stats.class})
@Log
@Singleton
public class VisibilitySystem extends IteratingSystem {

  private final InField inField;
  @AspectDescriptor(all = { Coordinates.class }, exclude = { SubField.class })
  private EntitySubscription allThatCanBePerceived;
  private ComponentMapper<Coordinates> coordinatesMapper;
  private ComponentMapper<Visible> visibleMapper;
  private ComponentMapper<Stats> statsMapper;
  private IntSet visibles = new IntSet();

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
      var dst2 = Math.pow(coordinates.getX() - perceivableCoords.getX(), 2) + Math.pow(coordinates.getY() - perceivableCoords.getY(), 2);
      if (dst2 <= sightlineRadius * sightlineRadius) {
        visibles.add(perceivableEntity);
      }
    }
  }

  @Override
  protected void end() {
    if (inField.isInField()) { // todo do it differently
      visibles.clear();
      return;
    }
    log.info("perceived " + visibles.size + " potentailly visibile " + allThatCanBePerceived.getEntities().size());
    var iterator = visibles.iterator();
    while (iterator.hasNext) {
      var visible = iterator.next();
      visibleMapper.set(visible, true);
    }
    iterator.reset();
    visibles.clear();
  }
}
