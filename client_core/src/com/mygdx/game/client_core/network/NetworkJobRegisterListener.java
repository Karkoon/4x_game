package com.mygdx.game.client_core.network;

import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.data.WebSocketException;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ConcurrentLinkedQueue;

@Singleton
@Log
public class NetworkJobRegisterListener implements WebSocketListener {

    private final ConcurrentLinkedQueue<OnMessageArgs> queue = new ConcurrentLinkedQueue<>();

    public OnMessageArgs poll() {
        return queue.poll();
    }

    public record OnMessageArgs(WebSocket socket, byte[] data) {
        @Override
        public String toString() {
            return new String(data);
        }
    }

    @Inject
    public NetworkJobRegisterListener() {
        super();
    }

    @Override
    public boolean onOpen(WebSocket webSocket) {
        return false;
    }

    @Override
    public boolean onClose(WebSocket webSocket, int closeCode, String reason) {
        return false;
    }

    @Override
    public boolean onMessage(WebSocket webSocket, String packet) {
        return onMessage(webSocket, packet.getBytes());
    }

    @Override
    public boolean onMessage(WebSocket webSocket, byte[] packet) {
        try {
            log.info("onMessage register job args");
            queue.add(new OnMessageArgs(webSocket, packet));
            return true;
        } catch (final Exception exception) {
            return onError(webSocket,
                new WebSocketException("Unable to handle the received packet: " + packet, exception));
        }
    }

    @Override
    public boolean onError(WebSocket webSocket, Throwable error) {
        return false;
    }
}
