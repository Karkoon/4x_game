package com.mygdx.game.client_core.network;

import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.data.WebSocketException;
import com.mygdx.game.client_core.model.NetworkJobsQueueJobJobberManager;

import javax.inject.Inject;

public class NetworkJobRegisterHandler implements WebSocketListener {

    private final NetworkJobsQueueJobJobberManager queue;

    @Inject
    public NetworkJobRegisterHandler(
        NetworkJobsQueueJobJobberManager queue
    ) {
        this.queue = queue;
    }

    @Override
    public boolean onOpen(WebSocket webSocket) {
        return false; // TODO: 13.09.2022 implement it (client side connection to the server??)
    }

    @Override
    public boolean onClose(WebSocket webSocket, int closeCode, String reason) {
        return false; // TODO: 13.09.2022 implement it
    }

    @Override
    public boolean onMessage(WebSocket webSocket, String packet) {
        return onMessage(webSocket, packet.getBytes());
    }

    @Override
    public boolean onMessage(WebSocket webSocket, byte[] packet) {
        try {
            queue.add(webSocket, packet);
            return true;
        } catch (final Exception exception) {
            return onError(webSocket,
                new WebSocketException("Unable to handle the received packet: " + packet, exception));
        }
    }

    @Override
    public boolean onError(WebSocket webSocket, Throwable error) {
        return false; // TODO: 13.09.2022 implement it
    }
}
