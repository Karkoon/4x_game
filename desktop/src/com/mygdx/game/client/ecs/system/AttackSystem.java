package com.mygdx.game.client.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.mygdx.game.client.ecs.component.Highlighted;
import com.mygdx.game.client.model.ChosenEntity;
import com.mygdx.game.client_core.network.AttackEntityService;
import com.mygdx.game.core.ecs.component.CanAttack;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.Stats;

import javax.inject.Inject;

@All({Highlighted.class, Stats.class})
public class AttackSystem extends IteratingSystem {

  private final ChosenEntity chosenEntity;
  private final AttackEntityService attackEntityService;
  private ComponentMapper<Stats> statsComponentMapper;
  private ComponentMapper<Owner> ownerComponentMapper;

  @Inject
  public AttackSystem(
      ChosenEntity chosenEntity,
      AttackEntityService attackEntityService
  ) {
    this.chosenEntity = chosenEntity;
    this.attackEntityService = attackEntityService;
  }

  @Override
  public void process(int attacker) {
    if (chosenEntity.isAnyChosen() && statsComponentMapper.has(chosenEntity.peek())) {
      var attacked = chosenEntity.pop();
      if (haveDifferentOwner(attacker, attacked)) {
        attackEntityService.attack(attacker, attacked);
      }
    }
  }

  private boolean haveDifferentOwner(int attacker, int attacked) {
    return !ownerComponentMapper.get(attacker).getToken().equals(ownerComponentMapper.get(attacked).getToken());
  }
}
