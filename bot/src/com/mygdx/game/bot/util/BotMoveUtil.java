package com.mygdx.game.bot.util;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.badlogic.gdx.utils.IntArray;
import com.mygdx.game.bot.hud.FieldUtil;
import com.mygdx.game.bot.model.ChosenBotType;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.client_core.network.service.MoveEntityService;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.Stats;
import lombok.extern.java.Log;

import javax.inject.Inject;
import java.util.Random;

@GameInstanceScope
@Log
public class BotMoveUtil {
    private final MoveEntityService moveEntityService;
    private final FieldUtil fieldUtil;
    private final ChosenBotType chosenBotType;
    private final PlayerInfo playerInfo;
    private final Random random;
    private final World world;

    @AspectDescriptor(all = {Stats.class, Owner.class, Coordinates.class})
    private EntitySubscription unitsSubscription;

    private ComponentMapper<Coordinates> coordinatesMapper;
    private ComponentMapper<Owner> ownerMapper;
    private ComponentMapper<Stats> statsMapper;

    @Inject
    public BotMoveUtil (
            MoveEntityService moveEntityService,
            FieldUtil fieldUtil,
            ChosenBotType chosenBotType,
            PlayerInfo playerInfo,
            World world
    ) {
        this.moveEntityService = moveEntityService;
        this.fieldUtil = fieldUtil;
        this.chosenBotType = chosenBotType;
        this.playerInfo = playerInfo;
        this.random = new Random();
        this.world = world;
        world.inject(this);
    }

    public void move(int unitId) {
        IntArray availableFields = fieldUtil.selectAvailableFields(unitId);
        if (availableFields == null || availableFields.isEmpty()){
            return;
        }
        int field = availableFields.random();
        log.info("moving entity");
        moveEntityService.moveEntity(unitId, coordinatesMapper.get(field));
    }
}