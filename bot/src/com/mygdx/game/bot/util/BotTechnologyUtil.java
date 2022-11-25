package com.mygdx.game.bot.util;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.bot.model.ChosenBotType;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.client_core.network.service.ResearchTechnologyService;
import com.mygdx.game.config.TechnologyConfig;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.InResearch;
import com.mygdx.game.core.ecs.component.Researched;
import com.mygdx.game.core.ecs.component.Technology;
import com.mygdx.game.core.model.BotType;
import lombok.extern.java.Log;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@GameInstanceScope
@Log
public class BotTechnologyUtil {

  private final float SHOULD_RESEARCH_RANDOM_FIRST = 0.25f;

  private final ChosenBotType chosenBotType;
  private final GameConfigAssets gameConfigAssets;
  private final PlayerInfo playerInfo;
  private final ResearchTechnologyService researchTechnologyService;
  private final Random random;
  private final World world;

  @AspectDescriptor(all = {InResearch.class})
  private EntitySubscription inResearchSubscriber;

  @AspectDescriptor(all = {Researched.class})
  private EntitySubscription researchedSubscriber;

  @AspectDescriptor(all = {Technology.class}, exclude = {Researched.class})
  private EntitySubscription notResearchedSubscriber;


  private ComponentMapper<EntityConfigId> entityConfigIdMapper;
  private ComponentMapper<InResearch> inResearchMapper;
  private ComponentMapper<Researched> researchedMapper;

  @Inject
  public BotTechnologyUtil(
      ChosenBotType chosenBotType,
      GameConfigAssets gameConfigAssets,
      PlayerInfo playerInfo,
      ResearchTechnologyService researchTechnologyService,
      World world
  ) {
    this.chosenBotType = chosenBotType;
    this.gameConfigAssets = gameConfigAssets;
    this.playerInfo = playerInfo;
    this.researchTechnologyService = researchTechnologyService;
    this.random = new Random();
    this.world = world;
    world.inject(this);
  }

  public void research() {
    if (chosenBotType.getBotType() == BotType.RANDOM_FIRST) {
      if (propabilityCheck(SHOULD_RESEARCH_RANDOM_FIRST)
          && inResearchSubscriber.getEntities().size() == 0
          && notResearchedSubscriber.getEntities().size() > 0
      ) {
        var researchedEntities = researchedSubscriber.getEntities();
        var researchedConfigIds = new ArrayList<Integer>();
        for (int i = 0; i < researchedEntities.size(); i++) {
          researchedConfigIds.add((int) entityConfigIdMapper.get(researchedEntities.get(i)).getId());
        }
        var notResearchedEntities = notResearchedSubscriber.getEntities();
        var notResearchedConfigIds = new ArrayList<Integer>();
        for (int i = 0; i < notResearchedSubscriber.getEntities().size(); i++) {
          notResearchedConfigIds.add((int) entityConfigIdMapper.get(notResearchedEntities.get(i)).getId());
        }

        Collections.shuffle(notResearchedConfigIds);
        for (int i = 0; i < notResearchedConfigIds.size(); i++) {
          var notResearchedConfigId = notResearchedConfigIds.get(i);
          var technologyConfig = gameConfigAssets.getGameConfigs().get(TechnologyConfig.class, notResearchedConfigId);
          var dependencies = technologyConfig.getDependencies();
          if (fulfilledDependencies(researchedConfigIds, dependencies)) {
            researchTechnologyService.researchTechnology(notResearchedEntities.get(i));
            break;
          }
        }

      }
    }
  }

  public boolean propabilityCheck(float value) {
    float probabilityValue = random.nextFloat();
    return probabilityValue <= value;
  }

  private boolean fulfilledDependencies(ArrayList<Integer> researchedConfigIds, List<Integer> dependencies) {
    for (Integer dependency : dependencies) {
      if (!researchedConfigIds.contains(dependency))
        return false;
    }
    return true;
  }
}
