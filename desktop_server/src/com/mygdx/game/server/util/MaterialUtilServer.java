package com.mygdx.game.server.util;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.PlayerMaterial;
import com.mygdx.game.core.model.MaterialUnit;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.network.gameinstance.services.WorldService;
import lombok.NonNull;

import javax.inject.Inject;
import java.util.List;

@GameInstanceScope
public class MaterialUtilServer extends WorldService {

  @AspectDescriptor(all = {Owner.class, PlayerMaterial.class})
  private EntitySubscription ownerPlayerMaterialSubscriber;

  private ComponentMapper<Owner> ownerMapper;
  private ComponentMapper<PlayerMaterial> playerMaterialMapper;

  private final World world;

  @Inject
  MaterialUtilServer(
      World world
  ) {
    world.inject(this);
    this.world = world;
  }

  public boolean checkIfCanBuy(String playerToken, @NonNull List<MaterialUnit> materials) {
    for (int i = 0; i < ownerPlayerMaterialSubscriber.getEntities().size(); i++) {
      int entityId = ownerPlayerMaterialSubscriber.getEntities().get(i);
      if (ownerMapper.get(entityId).getToken().equals(playerToken)) {
        var playerMaterial = playerMaterialMapper.get(entityId);
        for (var material : materials) {
          if (material.getBase() == playerMaterial.getMaterial()) {
            if (material.getAmount() > playerMaterial.getValue())
              return false;
          }
        }
      }
    }
    return true;
  }

  public void removeMaterials(String playerToken, List<MaterialUnit> materials) {
    for (int i = 0; i < ownerPlayerMaterialSubscriber.getEntities().size(); i++) {
      int entityId = ownerPlayerMaterialSubscriber.getEntities().get(i);
      if (ownerMapper.get(entityId).getToken().equals(playerToken)) {
        var playerMaterial = playerMaterialMapper.get(entityId);
        for (var material : materials) {
          if (material.getBase() == playerMaterial.getMaterial()) {
            playerMaterial.setValue(playerMaterial.getValue() - material.getAmount());
            setDirty(entityId, PlayerMaterial.class, world);
          }
        }
      }
    }
  }
}
