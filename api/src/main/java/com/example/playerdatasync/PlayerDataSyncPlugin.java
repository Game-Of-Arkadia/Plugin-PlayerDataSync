package com.example.playerdatasync;

import org.bukkit.entity.Player;

public interface PlayerDataSyncPlugin {

  void transfertPlayer(Player player, String servername);

}
