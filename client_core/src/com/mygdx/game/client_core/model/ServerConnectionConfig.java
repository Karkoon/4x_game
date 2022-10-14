package com.mygdx.game.client_core.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServerConnectionConfig {
  private String host;
  private int port;
}
