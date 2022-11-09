package com.mygdx.game.core.network.messages;

import com.mygdx.game.core.model.MapSize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomConfigMessage {

  MapSize mapSize;
}
