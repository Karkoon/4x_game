package com.mygdx.game.bot.util;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.mygdx.game.bot.hud.FieldUtil;
import com.mygdx.game.bot.model.ChosenBotType;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.ecs.component.Movable;
import com.mygdx.game.client_core.network.service.MoveEntityService;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.core.model.BotType;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class BotMoveUtil {
    private final MoveEntityService moveEntityService;
    private final FieldUtil fieldUtil;
    private final ChosenBotType chosenBotType;

    @AspectDescriptor(all = {Stats.class, Owner.class, Coordinates.class, Movable.class})
    private EntitySubscription unitsSubscription;

    private ComponentMapper<Coordinates> coordinatesMapper;

    @Inject
    public BotMoveUtil (
            MoveEntityService moveEntityService,
            FieldUtil fieldUtil,
            ChosenBotType chosenBotType,
            World world
    ) {
        this.moveEntityService = moveEntityService;
        this.fieldUtil = fieldUtil;
        this.chosenBotType = chosenBotType;
        world.inject(this);
    }

    public void move(int unitId) {
        if (chosenBotType.getBotType() == BotType.RANDOM_FIRST) {
            var availableFields = fieldUtil.selectAvailableFields(unitId);
            if (availableFields.isEmpty()){
                return;
            }
            int field = availableFields.random();
            log.info("moving entity");
            moveEntityService.moveEntity(unitId, coordinatesMapper.get(field));
        } else if (chosenBotType.getBotType() == BotType.TRAINED) {
            // Q-learning
        }
    }
}
