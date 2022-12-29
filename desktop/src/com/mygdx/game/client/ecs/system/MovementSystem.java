package com.mygdx.game.client.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.artemis.annotations.AspectDescriptor;
import com.artemis.EntitySubscription;
import com.mygdx.game.client.ecs.component.Highlighted;
import com.mygdx.game.client.model.ChosenEntity;
import com.mygdx.game.client.ui.WarningDialogFactory;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.ecs.component.Movable;
import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.client_core.network.service.MoveEntityService;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.core.util.DistanceUtil;
import lombok.extern.java.Log;

import javax.inject.Inject;

@All({Highlighted.class, Movable.class})
@GameInstanceScope
@Log
public class MovementSystem extends IteratingSystem {

  private final ChosenEntity chosenEntity;
  private final MoveEntityService moveEntityService;
  private final PlayerInfo playerInfo;
  private final WarningDialogFactory warningDialog;

  private ComponentMapper<Coordinates> coordinatesMapper;
  private ComponentMapper<Field> fieldMapper;
  private ComponentMapper<Highlighted> highlightedMapper;
  private ComponentMapper<Owner> ownerComponentMapper;
  private ComponentMapper<Stats> statsMapper;

  @AspectDescriptor(all = {Movable.class, Owner.class, Coordinates.class})
  private EntitySubscription units;

  @Inject
  public MovementSystem(
      ChosenEntity chosenEntity,
      MoveEntityService moveEntityService,
      PlayerInfo playerInfo,
      WarningDialogFactory warningDialog
  ) {
    this.chosenEntity = chosenEntity;
    this.moveEntityService = moveEntityService;
    this.playerInfo = playerInfo;
    this.warningDialog = warningDialog;
  }

  @Override
  protected void process(int entityId) {
    if (chosenEntity.isAnyChosen() && fieldMapper.has(chosenEntity.peek())) {
      var chosenEntityId = chosenEntity.pop();
      if (!playerInfo.getToken().equals(ownerComponentMapper.get(entityId).getToken())){
        warningDialog.createAndShow("Movement restricted!",
                "You cannot move this unit! You are not an owner!");
        highlightedMapper.remove(entityId);
        return;
      }

      boolean canMove = true;
      for (int i = 0; i < units.getEntities().size(); i++) {
        var entity = units.getEntities().get(i);
        if (!coordinatesMapper.get(entity).equals(coordinatesMapper.get(chosenEntityId))){
          continue;
        }
        if (!ownerComponentMapper.get(entity).getToken()
                .equals(ownerComponentMapper.get(entityId).getToken())) {
          canMove = false;
          log.info("Cannot move on enemy's field!");
          break;
        }
      }
      if (canMove) {
        log.info("some are chosen and there's a movable highlighted entity");

        var targetCoordinate = coordinatesMapper.get(chosenEntityId);
        var currentCoordinate = coordinatesMapper.get(entityId);

        var distance = DistanceUtil.distance(currentCoordinate, targetCoordinate);
        var range = statsMapper.get(entityId).getMoveRange();

        log.info("Chce przejsc " + distance + " ale zostało mi " + range);

        if (distance > range) {
          warningDialog.createAndShow("Movement out of range!",
                  "You have " + range + " move points left!" + "You can't move " + distance + " units!");
        } else {
          moveEntityService.moveEntity(entityId, targetCoordinate);
        }
      }
      else {
        warningDialog.createAndShow("Movement blocked by enemy unit!",
                "You cannot stand on this field! It is already occupied by enemy unit!");
      }

      highlightedMapper.remove(entityId);
    }
  }

}
