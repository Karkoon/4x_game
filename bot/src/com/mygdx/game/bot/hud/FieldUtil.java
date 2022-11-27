package com.mygdx.game.bot.hud;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntSet;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.ecs.component.Movable;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.core.util.DistanceUtil;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class FieldUtil {

  private ComponentMapper<Owner> ownerComponentMapper;
  private ComponentMapper<Coordinates> coordinatesComponentMapper;
  private ComponentMapper<Stats> statsComponentMapper;

  @Inject
  public FieldUtil(
      World world
  ) {
    world.inject(this);
  }

  @AspectDescriptor(one = {Field.class})
  private EntitySubscription fields;

  @AspectDescriptor(one = {Stats.class, Owner.class, Coordinates.class, Movable.class})
  private EntitySubscription units;

  public IntArray selectAvailableFields(int unit) {
    var fieldsInRange = new IntSet();
    for (int i = 0; i < fields.getEntities().size(); i++) {
      int field = fields.getEntities().get(i);
      if (inRange(unit, field) && !isOccupied(unit, field)) {
        fieldsInRange.add(field);
      }
    }

    return fieldsInRange.iterator().toArray();
  }

  private boolean inRange(int unit, int field) {
    if (!units.getEntities().contains(unit)){
      log.severe("Coś nie tak ze statami!!!");
      return false;
    }
    var currentCoordinate = coordinatesComponentMapper.get(unit);
    var coordinates = coordinatesComponentMapper.get(field);
    var distance = DistanceUtil.distance(currentCoordinate, coordinates);
    var range = statsComponentMapper.get(unit).getMoveRange();
    return range >= distance;
  }

  private boolean isOccupied(int unit, int field) {
    for (int i = 0; i < units.getEntities().size(); i++) {
      var otherUnit = units.getEntities().get(i);
      if (!coordinatesComponentMapper.get(otherUnit).equals(coordinatesComponentMapper.get(field))){
        continue;
      }
      if (!ownerComponentMapper.get(otherUnit).getToken()
              .equals(ownerComponentMapper.get(unit).getToken())) {
        return true;
      }
    }
    return false;
  }
}
