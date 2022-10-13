package com.mygdx.game.client.di.gameinstance;

import com.mygdx.game.client.network.DesktopEntityConfigHandler;
import com.mygdx.game.client.network.DesktopMaterialHandler;
import com.mygdx.game.client.network.FieldOwnerColorHandler;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import com.mygdx.game.client_core.network.comp_handlers.BuildingHandler;
import com.mygdx.game.client_core.network.comp_handlers.CanAttackHandler;
import com.mygdx.game.client_core.network.comp_handlers.CoordinatesHandler;
import com.mygdx.game.client_core.network.comp_handlers.DesktopUnderConstructionHandler;
import com.mygdx.game.client_core.network.comp_handlers.EntityConfigHandler;
import com.mygdx.game.client_core.network.comp_handlers.FieldHandler;
import com.mygdx.game.client_core.network.comp_handlers.OwnerHandler;
import com.mygdx.game.client_core.network.comp_handlers.ResearchHandler;
import com.mygdx.game.client_core.network.comp_handlers.StatsHandler;
import com.mygdx.game.client_core.network.comp_handlers.SubFieldHandler;
import com.mygdx.game.client_core.network.comp_handlers.UnderConstructionHandler;
import com.mygdx.game.core.ecs.component.Building;
import com.mygdx.game.core.ecs.component.CanAttack;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.PlayerMaterial;
import com.mygdx.game.core.ecs.component.Research;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.core.ecs.component.SubField;
import com.mygdx.game.core.ecs.component.UnderConstruction;
import dagger.Module;
import dagger.Provides;
import lombok.extern.java.Log;

@Log
@Module
public class ComponentMessageListenerModule {

  @Provides
  @GameInstanceScope
  public ComponentMessageListener provideComponentMessageListener(
      NetworkWorldEntityMapper mapper,
      EntityConfigHandler entityConfigHandler,
      DesktopEntityConfigHandler desktopEntityConfigHandler,
      DesktopMaterialHandler desktopMaterialHandler,
      CoordinatesHandler coordinatesHandler,
      FieldHandler fieldHandler,
      SubFieldHandler subFieldHandler,
      UnderConstructionHandler underConstructionHandler,
      DesktopUnderConstructionHandler desktopUnderConstructionHandler,
      BuildingHandler buildingHandler,
      OwnerHandler ownerHandler,
      FieldOwnerColorHandler fieldOwnerColorHandler,
      ResearchHandler researchHandler,
      StatsHandler statsHandler,
      CanAttackHandler canAttackHandler
  ) {
    var listener = new ComponentMessageListener(mapper); // jobs
    listener.registerHandler(EntityConfigId.class, entityConfigHandler);
    listener.registerHandler(EntityConfigId.class, desktopEntityConfigHandler);
    listener.registerHandler(PlayerMaterial.class, materialHandler);
    listener.registerHandler(PlayerMaterial.class, desktopMaterialHandler);
    listener.registerHandler(Coordinates.class, coordinatesHandler);
    listener.registerHandler(Field.class, fieldHandler);
    listener.registerHandler(SubField.class, subFieldHandler);
    listener.registerHandler(UnderConstruction.class, underConstructionHandler);
    listener.registerHandler(UnderConstruction.class, desktopUnderConstructionHandler);
    listener.registerHandler(Building.class, buildingHandler);
    listener.registerHandler(Owner.class, ownerHandler);
    listener.registerHandler(Owner.class, fieldOwnerColorHandler);
    listener.registerHandler(Research.class, researchHandler);
    listener.registerHandler(Stats.class, statsHandler);
    listener.registerHandler(CanAttack.class, canAttackHandler);
    return listener;
  }
}
