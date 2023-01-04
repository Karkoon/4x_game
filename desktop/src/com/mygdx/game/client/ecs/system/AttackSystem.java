package com.mygdx.game.client.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.mygdx.game.client.ecs.component.Highlighted;
import com.mygdx.game.client.model.ChosenEntity;
import com.mygdx.game.client.ui.WarningDialogFactory;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.client_core.network.service.AttackEntityService;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.core.util.DistanceUtil;
import lombok.extern.java.Log;

import javax.inject.Inject;

@All({Highlighted.class, Stats.class})
@Log
@GameInstanceScope
public class AttackSystem extends IteratingSystem {

  private final AttackEntityService attackEntityService;
  private final ChosenEntity chosenEntity;
  private final PlayerInfo playerInfo;
  private final WarningDialogFactory warningDialog;

  private ComponentMapper<Coordinates> coordinatesMapper;
  private ComponentMapper<Highlighted> highlightedMapper;
  private ComponentMapper<Owner> ownerComponentMapper;
  private ComponentMapper<Stats> statsComponentMapper;

  @Inject
  public AttackSystem(
      AttackEntityService attackEntityService,
      ChosenEntity chosenEntity,
      PlayerInfo playerInfo,
      WarningDialogFactory warningDialog
  ) {
    this.attackEntityService = attackEntityService;
    this.chosenEntity = chosenEntity;
    this.playerInfo = playerInfo;
    this.warningDialog = warningDialog;
  }

  @Override
  public void process(int attacker) {
    if (chosenEntity.isAnyChosen() && statsComponentMapper.has(chosenEntity.peek())) {
      log.info("attack system clicked");
      var attacked = chosenEntity.pop();
      if (!playerInfo.getToken().equals(ownerComponentMapper.get(attacker).getToken())){
        warningDialog.createAndShow("Attack restricted!",
                "You cannot use attack with this unit! You are not an owner!");
        highlightedMapper.remove(attacker);
        return;
      }

      if (haveDifferentOwner(attacker, attacked)) {
        Coordinates attackerCoordinates = coordinatesMapper.get(attacker);
        Coordinates attackedCoordinates = coordinatesMapper.get(attacked);

        Stats attackerStats = statsComponentMapper.get(attacker);
        int attackerAttackRange = attackerStats.getAttackRange();
        if (DistanceUtil.distance(attackerCoordinates, attackedCoordinates) > attackerAttackRange){
          warningDialog.createAndShow("Atack range","You can't attack this unit! Enemy is to far.");
        }
        else {
          attackEntityService.attack(attacker, attacked);
        }
      }
      else {
        warningDialog.createAndShow("Attack","You can't attack yourself!!!");
      }
      highlightedMapper.remove(attacker);
    }
  }

  private boolean haveDifferentOwner(int attacker, int attacked) {
    return !ownerComponentMapper.get(attacker).getToken().equals(ownerComponentMapper.get(attacked).getToken());
  }
}
