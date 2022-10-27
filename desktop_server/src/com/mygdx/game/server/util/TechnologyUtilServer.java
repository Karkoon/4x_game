package com.mygdx.game.server.util;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.mygdx.game.core.ecs.component.InResearch;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.Researched;
import com.mygdx.game.core.model.MaterialBase;
import com.mygdx.game.core.model.MaterialUnit;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.network.gameinstance.services.WorldService;
import lombok.extern.java.Log;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@GameInstanceScope
@Log
public class TechnologyUtilServer extends WorldService {

  private final MaterialUtilServer materialUtilServer;
  private final World world;

  private ComponentMapper<InResearch> inResearchMapper;
  private ComponentMapper<Researched> researchedMapper;
  private ComponentMapper<Owner> ownerMapper;

  @AspectDescriptor(all = {InResearch.class})
  private EntitySubscription inResearchSubscriber;

  @Inject
  TechnologyUtilServer(
      MaterialUtilServer materialUtilServer,
      World world
  ) {
    world.inject(this);
    this.materialUtilServer = materialUtilServer;
    this.world = world;
  }

  public void researchTechnologies() {
    for (int i = 0; i < inResearchSubscriber.getEntities().size(); i++) {
      int technologyEntityId = inResearchSubscriber.getEntities().get(i);
      var inResearch = inResearchMapper.get(technologyEntityId);
      var owner = ownerMapper.get(technologyEntityId);
      int playerScience = materialUtilServer.getPlayerMaterial(owner.getToken(), MaterialBase.SCIENCE);
      int maxCanUse = Math.min(playerScience, inResearch.getScienceLeft());
      log.info("Player science to use " + playerScience);
      log.info("Science points left to research " + inResearch.getScienceLeft());
      if (maxCanUse > 0) {
        log.info("Try to remove science: " + maxCanUse);
        Map<MaterialBase, MaterialUnit> materialsToRemove = new HashMap<>();
        materialsToRemove.put(MaterialBase.SCIENCE, new MaterialUnit(MaterialBase.SCIENCE, maxCanUse));
        materialUtilServer.removeMaterials(owner.getToken(), materialsToRemove);
        inResearch.setScienceLeft(inResearch.getScienceLeft()-maxCanUse);
        setDirty(technologyEntityId, InResearch.class, world);
        if (inResearch.getScienceLeft() == 0) {
          log.info("Successfully researched");
          inResearchMapper.remove(technologyEntityId);
          researchedMapper.create(technologyEntityId);
          setDirty(technologyEntityId, Researched.class, world);
        }
      }
    }
  }
}
