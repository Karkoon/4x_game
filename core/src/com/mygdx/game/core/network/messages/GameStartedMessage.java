package com.mygdx.game.core.network.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class GameStartedMessage {

  String playerToken;
}
