package com.mygdx.game.core.network.messages;

import com.mygdx.game.core.model.PlayerLobby;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PlayerJoinedRoomMessage {

  List<PlayerLobby> users;
}
