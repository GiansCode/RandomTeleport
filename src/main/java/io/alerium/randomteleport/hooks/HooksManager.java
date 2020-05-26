package io.alerium.randomteleport.hooks;

import io.alerium.randomteleport.RandomTeleportPlugin;
import io.alerium.randomteleport.hooks.hooks.LandsHook;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HooksManager {
    
    private final RandomTeleportPlugin plugin = RandomTeleportPlugin.getInstance();
    private final List<Hook> hooks = new ArrayList<>();

    /**
     * This method checks all the enabled hooks
     */
    public void reload() {
        hooks.clear();
        
        if (canRegisterHook("lands", "Lands")) {
            hooks.add(new LandsHook());
            plugin.getLogger().info("Hooked into Lands plugin");
        }
    }

    /**
     * This method checks if a Location is safe for a Player
     * @param location The Location
     * @param player The Player
     * @return True if it is safe
     */
    public boolean canTeleport(Location location, Player player) {
        for (Hook hook : hooks) {
            if (!hook.canTeleport(location, player))
                return false;
        }
        
        return true;
    }

    /**
     * This method checks if an Hook can be enabled
     * @param hookName The Hook name
     * @param pluginName The required plugin name
     * @return True if it can be enabled
     */
    private boolean canRegisterHook(String hookName, String pluginName) {
        if (!plugin.getConfig().getBoolean("hooks." + hookName + ".enabled"))
            return false;

        return Bukkit.getPluginManager().getPlugin(pluginName) != null;
    }
    
}
