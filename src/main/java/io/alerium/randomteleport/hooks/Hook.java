package io.alerium.randomteleport.hooks;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class Hook {

    /**
     * This method checks if a Location is safe for a Player
     * @param location The Location
     * @param player The Player
     * @return True if it is safe
     */
    public abstract boolean canTeleport(Location location, Player player);
    
}
