package com.mygdx.game.client_core.network.service;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.MessageSender;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import com.mygdx.game.core.ecs.component.InResearch;
import com.mygdx.game.core.ecs.component.Researched;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class ResearchTechnologyService {

  private final Lazy<MessageSender> sender;
  private final NetworkWorldEntityMapper networkWorldEntityMapper;

  private ComponentMapper<InResearch> inResearchMapper;
  private ComponentMapper<Researched> researchedMapper;

  @AspectDescriptor(all = {InResearch.class})
  private EntitySubscription inResearchSubscriber;

  @Inject
  public ResearchTechnologyService(
    Lazy<MessageSender> sender,
    NetworkWorldEntityMapper networkWorldEntityMapper,
    World world
  ) {
    world.inject(this);
    this.sender = sender;
    this.networkWorldEntityMapper = networkWorldEntityMapper;
  }

  public void researchTechnology(int entityId) {
    log.info("research:" + entityId);
    if (inResearchMapper.has(entityId)) {
      log.info("Can't research because this is already in progress");
    } else if (inResearchSubscriber.getEntities().size() > 0) {
      log.info("Can't research because something else is in progress");
    } else if (researchedMapper.has(entityId)) {
      log.info("Can't research because it's researched");
    } else {
      int networkEntity = networkWorldEntityMapper.getNetworkEntity(entityId);
      sender.get().send("research:" + networkEntity);
    }
  }
}
