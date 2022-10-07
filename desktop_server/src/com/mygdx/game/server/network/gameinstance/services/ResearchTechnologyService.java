package com.mygdx.game.server.network.gameinstance.services;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.Research;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.ecs.entityfactory.ComponentFactory;
import com.mygdx.game.server.model.Client;

import javax.inject.Inject;

@GameInstanceScope
public class ResearchTechnologyService extends WorldService {

  private ComponentMapper<Research> researchMapper;
  private final ComponentFactory componentFactory;

  private World world;

  @Inject
  public ResearchTechnologyService(
      World world,
      ComponentFactory componentFactory
  ) {
    world.inject(this);
    this.componentFactory = componentFactory;
  }

  public void researchTechnology(int entityId, Client client) {
    var componentsToSend = new Class[]{EntityConfigId.class, Research.class};

    researchMapper.create(entityId);
    componentFactory.createSharedComponents(entityId,
      componentsToSend,
      new Class[]{}
    );
    setDirty(entityId, Research.class, world);

    world.process();
  }

}
