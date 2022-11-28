package com.mygdx.game.client_core.network;

import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.data.WebSocketException;


public interface OnMessageListener {

  boolean onMessage(WebSocket webSocket, Object packet) throws WebSocketException;

  boolean NOT_HANDLED = false;
  boolean FULLY_HANDLED = true;
}
