package com.mygdx.game.client_core.network.comp_handlers;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.core.ecs.component.MoveRange;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
public class MoveRangeHandler implements ComponentMessageListener.Handler {
    private final ComponentMapper<MoveRange> moveRangeMapper;

    @Inject
    public MoveRangeHandler(
            World world
    ) {
        this.moveRangeMapper = world.getMapper(MoveRange.class);
    }

    @Override
    public boolean handle(WebSocket webSocket, int worldEntity, Component component) {
        log.info("Read move_range component " + worldEntity);

        var entityMoveRange = (MoveRange) moveRangeMapper.create(worldEntity);

        entityMoveRange.setMoveRange(((MoveRange) component).getMoveRange());
        entityMoveRange.setCurrentRange(((MoveRange) component).getCurrentRange());

        return FULLY_HANDLED;
    }
}
