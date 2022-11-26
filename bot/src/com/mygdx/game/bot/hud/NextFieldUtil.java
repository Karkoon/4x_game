package com.mygdx.game.bot.hud;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
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
public class NextFieldUtil {

  private ComponentMapper<Owner> ownerComponentMapper;
  private ComponentMapper<Coordinates> coordinatesComponentMapper;
  private ComponentMapper<Stats> statsComponentMapper;

  @AspectDescriptor(all = {Field.class})
  private EntitySubscription fields;

  @AspectDescriptor(all = {Movable.class, Owner.class, Coordinates.class})
  private EntitySubscription units;

  @Inject
  public NextFieldUtil(
      World world
  ) {
    world.inject(this);
  }

  public int selectFieldInRangeOfUnit(int unit) {
    var fieldsInRange = new IntSet();
    for (int i = 0; i < fields.getEntities().size(); i++) {
      int field = fields.getEntities().get(i);
      if (inRange(unit, field) && noEnemyUnits(unit, field)) {
        fieldsInRange.add(field);
      }
    }
    var iter = fieldsInRange.iterator();
    while (iter.hasNext) {
      int field = iter.next();
      if (haveDifferentOwner(unit, field)) {
        return field;
      }
    }
    return 0xC0FFEE;
  }

  private boolean haveDifferentOwner(int unit, int field) {
    var unitOwner = ownerComponentMapper.get(unit);
    var fieldOwner = ownerComponentMapper.get(field);
    return fieldOwner == null || !unitOwner.getToken().equals(fieldOwner.getToken());
  }

  // Chyba jeszcze nie działa
  private boolean noEnemyUnits(int unit, int field) {
    var unitOwner = ownerComponentMapper.get(unit);
    var fieldCoordinates = coordinatesComponentMapper.get(field);

    for (int i = 0; i < units.getEntities().size(); i++) {
      var otherUnitOwner = ownerComponentMapper.get(units.getEntities().get(i));
      var unitCoordinates = coordinatesComponentMapper.get(units.getEntities().get(i));
      if (fieldCoordinates.equals(unitCoordinates) && !unitOwner.getToken().equals(otherUnitOwner.getToken()))
        return false;
    }
    return true;
  }

  private boolean inRange(int unit, int field) {
    var currentCoordinate = coordinatesComponentMapper.get(unit);
    var coordinates = coordinatesComponentMapper.get(field);
    var distance = DistanceUtil.distance(currentCoordinate, coordinates);
    var range = statsComponentMapper.get(unit).getMoveRange();
    return range >= distance;
  }
}
