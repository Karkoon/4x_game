package com.mygdx.game.client_core.network.service;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.MessageSender;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import com.mygdx.game.client_core.util.InfieldUtil;
import com.mygdx.game.core.ecs.component.InRecruitment;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class CreateUnitService {

  private final Lazy<MessageSender> sender;
  private final NetworkWorldEntityMapper networkWorldEntityMapper;
  private final InfieldUtil infieldUtil;
  private ComponentMapper<InRecruitment> inRecruitmentMapper;

  @Inject
  public CreateUnitService(
      Lazy<MessageSender> sender,
      NetworkWorldEntityMapper networkWorldEntityMapper,
      InfieldUtil infieldUtil,
      World world
  ) {
    this.sender = sender;
    this.networkWorldEntityMapper = networkWorldEntityMapper;
    this.infieldUtil = infieldUtil;
    world.inject(this);
  }

  public void createUnit(long unitConfigId, int fieldId) {
    if (!infieldUtil.checkIfCanBuildUnit(fieldId, unitConfigId)) {
      log.info("You don't have enough buildings");
    } else if (inRecruitmentMapper.has(fieldId)) {
      log.info("There is another recruited unit");
    } else if (!infieldUtil.checkIfEnoughMaterialsToRecruitUnit(unitConfigId)){
      log.info("You don't have enough materials");
    } else {
      var fieldNetworkId = networkWorldEntityMapper.getNetworkEntity(fieldId);
      log.info("create unit for unit " + unitConfigId + " for field " + fieldNetworkId + " request send");
      sender.get().send("create_unit" + ":" + unitConfigId + ":" + fieldNetworkId);
    }
  }
}
