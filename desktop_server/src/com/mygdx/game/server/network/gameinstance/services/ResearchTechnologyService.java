package com.mygdx.game.server.network.gameinstance.services;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.TechnologyConfig;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.InResearch;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.Researched;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.ecs.entityfactory.ComponentFactory;
import com.mygdx.game.server.model.Client;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class ResearchTechnologyService extends WorldService {

  private final ComponentFactory componentFactory;
  private final GameConfigAssets gameConfigAssets;
  private World world;

  private ComponentMapper<EntityConfigId> entityConfigIdMapper;
  private ComponentMapper<InResearch> inResearchMapper;
  private ComponentMapper<Owner> ownerMapper;
  private ComponentMapper<Researched> researchedMapper;

  @AspectDescriptor(all = {Owner.class, InResearch.class})
  private EntitySubscription ownerInResearchSubscriber;

  @Inject
  public ResearchTechnologyService(
      ComponentFactory componentFactory,
      GameConfigAssets gameConfigAssets,
      World world
  ) {
    this.componentFactory = componentFactory;
    this.gameConfigAssets = gameConfigAssets;
    world.inject(this);
  }

  public void researchTechnology(int entityId, Client client) {
    for (int i = 0; i < ownerInResearchSubscriber.getEntities().size(); i++) {
      var owner = ownerMapper.get(ownerInResearchSubscriber.getEntities().get(i));
      if (owner.getToken().equals(client.getPlayerToken())) {
        log.info("This player is researching something already");
        return;
      }
    }
    if (researchedMapper.has(entityId)) {
      log.info("Technology is researched already");
      return ;
    }
    var entityConfigId = entityConfigIdMapper.get(entityId);
    var technologyConfig = gameConfigAssets.getGameConfigs().get(TechnologyConfig.class, entityConfigId.getId());

    var inResearch = inResearchMapper.create(entityId);
    inResearch.setConfigRequiredScience(technologyConfig.getRequiredScience());
    inResearch.setScienceLeft(technologyConfig.getRequiredScience());

    setDirty(entityId, InResearch.class, world);
    world.process();
  }

}
