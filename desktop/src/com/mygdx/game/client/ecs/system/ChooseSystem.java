package com.mygdx.game.client.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.IntMap;
import com.mygdx.game.client.ecs.component.Choosable;
import com.mygdx.game.client.model.ChosenEntity;
import com.mygdx.game.client.model.ClickInput;
import com.mygdx.game.client.ui.ChooseEntityDialogFactory;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.ecs.component.Name;
import com.mygdx.game.client_core.ecs.component.Position;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@All({Choosable.class, Position.class, Name.class})
@GameInstanceScope
public class ChooseSystem extends IteratingSystem {

  private final ClickInput clickInput;
  private final ChooseEntityDialogFactory dialogFactory;
  private final ChosenEntity chosenEntities;
  private final ClickedEntities clickedEntities;

  private ComponentMapper<Position> positionMapper;
  private ComponentMapper<Name> nameMapper;

  @Inject
  public ChooseSystem(
      ClickInput clickInput,
      ChooseEntityDialogFactory dialogFactory,
      ChosenEntity chosenEntity
  ) {
    this.clickInput = clickInput;
    this.dialogFactory = dialogFactory;
    this.chosenEntities = chosenEntity;
    this.clickedEntities = new ClickedEntities();
  }

  @Override
  protected void begin() {
    clickedEntities.reset();
  }

  @Override
  protected void process(int entityId) {
    if (!clickInput.isHandled()) {
      clickedEntities.saveIfClicked(entityId, clickInput);
    }
  }

  @Override
  protected void end() {
    if (clickedEntities.moreThanOneClicked()) {
      showChooseEntityDialog();
    } else if (clickedEntities.areAnyClicked()) {
      chosenEntities.addChosen(clickedEntities.getClickedEntity());
    }
    clickInput.setHandled(true);
  }

  private void showChooseEntityDialog() {
    var entitiesWithName = clickedEntities.getAllClickedWithName();
    dialogFactory.createAndShow(entitiesWithName, chosenEntities::addChosen);
  }

  private class ClickedEntities {

    private final IntBag clickedEntitiesIds = new IntBag();
    private float minDistance = Float.MAX_VALUE;
    public static final float CLICK_RADIUS = 90f;

    public void reset() {
      minDistance = Float.MAX_VALUE;
      clickedEntitiesIds.clear();
    }

    private boolean isWithinClickRadius(Vector3 worldPosition, Ray ray) {
      var dist2 = ray.origin.dst2(worldPosition);
      if (dist2 > minDistance) {
        return false;
      }
      if (Intersector.intersectRaySphere(ray, worldPosition, CLICK_RADIUS, null)) {
        minDistance = dist2;
        return true;
      }
      return false;
    }

    public void saveIfClicked(int entityId, ClickInput clickInput) {
      var position = positionMapper.get(entityId).getValue();
      if (isWithinClickRadius(position, clickInput.getValue())) {
        log.info("clicked entity");
        clickedEntitiesIds.add(entityId);
      }
    }

    public boolean areAnyClicked() {
      return !clickedEntitiesIds.isEmpty();
    }

    public boolean moreThanOneClicked() {
      return clickedEntitiesIds.size() > 1;
    }

    public IntMap<Name> getAllClickedWithName() {
      var entitiesWithName = new IntMap<Name>();
      for (var i = 0; i < clickedEntitiesIds.size(); i++) {
        var entityId = clickedEntitiesIds.get(i);
        entitiesWithName.put(clickedEntitiesIds.get(i), nameMapper.get(entityId));
      }
      return entitiesWithName;
    }

    public int getClickedEntity() {
      return clickedEntitiesIds.get(0);
    }
  }
}
