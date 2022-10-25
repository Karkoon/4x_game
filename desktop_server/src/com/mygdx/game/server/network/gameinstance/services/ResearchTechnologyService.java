package com.mygdx.game.server.network.gameinstance.services;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.TechnologyConfig;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.InResearch;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.ecs.entityfactory.ComponentFactory;
import com.mygdx.game.server.model.Client;

import javax.inject.Inject;

@GameInstanceScope
public class ResearchTechnologyService extends WorldService {

  private final ComponentFactory componentFactory;
  private final GameConfigAssets gameConfigAssets;
  private World world;

  private ComponentMapper<EntityConfigId> entityConfigIdMapper;
  private ComponentMapper<InResearch> inResearchMapper;

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
    var componentsToSend = new Class[]{EntityConfigId.class, InResearch.class};

    var entityConfigId = entityConfigIdMapper.get(entityId);
    var technologyConfig = gameConfigAssets.getGameConfigs().get(TechnologyConfig.class, entityConfigId.getId());

    var inResearch = inResearchMapper.create(entityId);
    inResearch.setConfigRequiredScience(technologyConfig.getRequiredScience());
    inResearch.setScienceLeft(technologyConfig.getRequiredScience());

    componentFactory.createSharedComponents(entityId,
      componentsToSend,
      new Class[]{}
    );
    setDirty(entityId, InResearch.class, world);

    world.process();
  }

}
