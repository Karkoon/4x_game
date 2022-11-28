package com.mygdx.game.core.network.messages;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.core.model.PlayerLobby;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerJoinedRoomMessage {

  Array<PlayerLobby> users;
}
