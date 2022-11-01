package com.mygdx.game.core.network.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WinAnnouncementMessage {
  private String winnerToken;
  private String winnerName;
}
