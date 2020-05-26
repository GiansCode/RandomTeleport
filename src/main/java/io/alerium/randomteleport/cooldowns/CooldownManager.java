package io.alerium.randomteleport.cooldowns;

import io.alerium.randomteleport.RandomTeleportPlugin;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {
    
    @Getter private final Map<UUID, Long> cooldowns = new HashMap<>();
    
    public void enable() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(RandomTeleportPlugin.getInstance(), () -> {
            for (UUID uuid : cooldowns.keySet()) {
                if (System.currentTimeMillis() >= cooldowns.get(uuid))
                    cooldowns.remove(uuid);
            }
        }, 20, 20);
    }

    /**
     * This method checks if a Player is in cooldown
     * @param uuid The UUID of the Player
     * @return True if it is in cooldown
     */
    public boolean isInCooldown(UUID uuid) {
        long time = cooldowns.getOrDefault(uuid, 0L);
        return time != 0 && System.currentTimeMillis() < time;
    }
    
}
