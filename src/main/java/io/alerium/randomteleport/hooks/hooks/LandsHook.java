package io.alerium.randomteleport.hooks.hooks;

import io.alerium.randomteleport.RandomTeleportPlugin;
import io.alerium.randomteleport.hooks.Hook;
import me.angeschossen.lands.api.integration.LandsIntegration;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LandsHook extends Hook {
    
    private final LandsIntegration integration;
    
    public LandsHook() {
        integration = new LandsIntegration(RandomTeleportPlugin.getInstance());
    }
    
    @Override
    public boolean canTeleport(Location location, Player player) {
        return !integration.isClaimed(location);
    }
    
}
