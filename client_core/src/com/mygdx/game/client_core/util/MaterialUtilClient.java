package com.mygdx.game.client_core.util;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.artemis.utils.IntBag;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.MaterialIncome;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.PlayerMaterial;
import com.mygdx.game.core.model.MaterialBase;
import com.mygdx.game.core.model.MaterialUnit;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@GameInstanceScope
public class MaterialUtilClient {

  @AspectDescriptor(all = {Owner.class, Field.class})
  private EntitySubscription ownerFieldsSubscriber;

  @AspectDescriptor(one = {PlayerMaterial.class})
  private EntitySubscription playerMaterialSubscriber;

  private ComponentMapper<Owner> ownerMapper;
  private ComponentMapper<Field> fieldMapper;
  private ComponentMapper<MaterialIncome> materialIncomeMapper;
  private ComponentMapper<PlayerMaterial> playerMaterialMapper;

  private final PlayerInfo playerInfo;


  @Inject
  MaterialUtilClient(
      PlayerInfo playerInfo,
      World world
  ) {
    world.inject(this);
    this.playerInfo = playerInfo;
  }

  public Map<MaterialBase, Integer> getPlayerMaterial() {
    var materialMap = new HashMap<MaterialBase, Integer>();
    for (int i = 0; i < playerMaterialSubscriber.getEntities().size(); i++) {
      int materialEntityId = playerMaterialSubscriber.getEntities().get(i);
      var materialComponent = playerMaterialMapper.get(materialEntityId);
      materialMap.put(materialComponent.getMaterial(), materialComponent.getValue());
    }
    return materialMap;
  }

  public boolean checkIfCanBuy(Map<MaterialBase, MaterialUnit> materials, IntBag entities) {
    for (int i = 0; i < entities.size(); i++) {
      int entityId = entities.get(i);
      var playerMaterial = playerMaterialMapper.get(entityId);
      if (materials.containsKey(playerMaterial.getMaterial())) {
        var material = materials.get(playerMaterial.getMaterial());
        if (material.getAmount() > playerMaterial.getValue())
          return false;
      }
    }
    return true;
  }

}
