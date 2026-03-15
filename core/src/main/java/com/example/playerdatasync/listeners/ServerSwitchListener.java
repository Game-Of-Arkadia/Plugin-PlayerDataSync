package com.example.playerdatasync.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.example.playerdatasync.core.PlayerDataSync;
import com.example.playerdatasync.database.DatabaseManager;
import com.example.playerdatasync.managers.MessageManager;

/**
 * Handles server switch requests that originate from in-game commands.
 * This ensures player data is safely stored before a BungeeCord transfer
 * and prevents duplication by clearing the inventory only after a successful save.
 */
public class ServerSwitchListener implements Listener {
    private final PlayerDataSync plugin;
    private final DatabaseManager databaseManager;
    private final MessageManager messageManager;

    public ServerSwitchListener(PlayerDataSync plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.messageManager = plugin.getMessageManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onServerSwitchCommand(PlayerCommandPreprocessEvent event) {
        if (!plugin.isBungeecordIntegrationEnabled()) {
            return;
        }

        String rawMessage = event.getMessage();
        if (rawMessage == null || rawMessage.isEmpty()) {
            return;
        }

        String trimmed = rawMessage.trim();
        if (!trimmed.startsWith("/")) {
            return;
        }

        String[] parts = trimmed.split("\\s+");
        if (parts.length == 0) {
            return;
        }

        String baseCommand = parts[0].startsWith("/") ? parts[0].substring(1) : parts[0];
        if (!baseCommand.equalsIgnoreCase("server")) {
            return;
        }

        Player player = event.getPlayer();
        if (parts.length < 2) {
            player.sendMessage(messageManager.get("prefix") + " "
                + messageManager.get("invalid_syntax").replace("{usage}", "/server <server>"));
            return;
        }

        String targetServer = parts[1];
        event.setCancelled(true);

        if (plugin.getConfigManager() != null && plugin.getConfigManager().shouldShowSyncMessages() 
            && player.hasPermission("playerdatasync.message.show.saving")) {
            player.sendMessage(messageManager.get("prefix") + " " + messageManager.get("server_switch_save"));
        }

        plugin.saveAndConnect(player, targetServer);
    }
}
