package com.example.playerdatasync;

import org.bukkit.entity.Player;

import java.util.Objects;

public final class PlayerDataSyncApi {
  private PlayerDataSyncApi() {}

  private static PlayerDataSyncPlugin pluginInstance;

  /**
   * INTERNAL CALL.
   * @param plugin plugin.
   */
  public static void provide(PlayerDataSyncPlugin plugin) {
    pluginInstance = Objects.requireNonNull(plugin, "Cannot provide a null PlayerDataSyncPlugin)");
  }

  public static void transfertPlayer(Player player, String serverName) {
    pluginInstance.transfertPlayer(player, serverName);
  }

}
