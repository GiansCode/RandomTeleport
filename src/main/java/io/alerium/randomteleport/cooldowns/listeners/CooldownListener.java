package io.alerium.randomteleport.cooldowns.listeners;

import io.alerium.randomteleport.RandomTeleportPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class CooldownListener implements Listener {
    
    private final RandomTeleportPlugin plugin = RandomTeleportPlugin.getInstance();
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getCooldownManager().getCooldowns().remove(event.getPlayer().getUniqueId());
    }
    
}
