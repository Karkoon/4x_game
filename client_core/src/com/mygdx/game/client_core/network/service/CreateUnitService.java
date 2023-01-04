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

@GameInstanceScope
@Log
public class CreateUnitService {

  private final InfieldUtil infieldUtil;
  private final Lazy<MessageSender> messageSender;
  private final NetworkWorldEntityMapper networkWorldEntityMapper;

  private ComponentMapper<InRecruitment> inRecruitmentMapper;

  @Inject
  public CreateUnitService(
      InfieldUtil infieldUtil,
      Lazy<MessageSender> messageSender,
      NetworkWorldEntityMapper networkWorldEntityMapper,
      World world
  ) {
    this.infieldUtil = infieldUtil;
    this.messageSender = messageSender;
    this.networkWorldEntityMapper = networkWorldEntityMapper;
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
      log.info("create unit " + unitConfigId + " for field " + fieldNetworkId + " request send");
      messageSender.get().send("create_unit" + ":" + unitConfigId + ":" + fieldNetworkId);
    }
  }

  public void createUnit(int fieldId) {
    if (inRecruitmentMapper.has(fieldId)) {
      log.info("There is another recruited unit");
    } else {
      var fieldNetworkId = networkWorldEntityMapper.getNetworkEntity(fieldId);
      log.info("create unit by boy for field " + fieldNetworkId + " request send");
      messageSender.get().send("create_unit_bot" + ":" + fieldNetworkId);
    }
  }
}
