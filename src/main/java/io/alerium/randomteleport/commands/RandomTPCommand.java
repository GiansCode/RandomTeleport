package io.alerium.randomteleport.commands;

import io.alerium.randomteleport.RandomTeleportPlugin;
import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class RandomTPCommand implements CommandExecutor {
    
    private final RandomTeleportPlugin plugin = RandomTeleportPlugin.getInstance();
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return true;
        
        Player player = (Player) sender;
        if (!player.hasPermission("randomteleport.command")) {
            player.sendMessage(plugin.getMessage("noPermission"));
            return true;
        }

        if (plugin.getCooldown() != -1 && plugin.getCooldownManager().isInCooldown(player.getUniqueId())) {
            player.sendMessage(plugin.getMessage("cooldown"));
            return true;
        }
        
        if (plugin.getCooldown() != -1)
            plugin.getCooldownManager().getCooldowns().put(player.getUniqueId(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(plugin.getCooldown()));
        
        if (plugin.getWarmup() != -1) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> randomTeleport(player, Bukkit.getWorld(plugin.getWorld()), plugin.getMinRangeX(), plugin.getMinRangeZ(), plugin.getMaxRangeX(), plugin.getMaxRangeZ()), 20 * plugin.getWarmup());
            player.sendMessage(plugin.getMessage("warmup"));
            return true;
        }
        
        randomTeleport(player, Bukkit.getWorld(plugin.getWorld()), plugin.getMinRangeX(), plugin.getMinRangeZ(), plugin.getMaxRangeX(), plugin.getMaxRangeZ());
        return true;
    }

    /**
     * This method tries to find a safe Location to teleport a Player
     * @param player The Player to teleport
     * @param world The World
     * @param minX The min X coordinate to teleport the Player
     * @param minZ The min Z coordinate to teleport the Player
     * @param maxX The max X coordinate to teleport the Player
     * @param maxZ The max Z coordinate to teleport the Player
     */
    private void randomTeleport(Player player, World world, int minX, int minZ, int maxX, int maxZ) {
        int x = ThreadLocalRandom.current().nextInt(minX, maxX);
        int z = ThreadLocalRandom.current().nextInt(minZ, maxZ);

        Location location = new Location(world, x, 1, z);
        System.out.println(location);
        PaperLib.getChunkAtAsync(location).thenAccept(chunk -> {
            if (chunk == null) {
                randomTeleport(player, world, minX, minZ, maxX, maxZ);
                return;
            }

            Location tpLoc = chunk.getWorld().getHighestBlockAt(location).getLocation().add(0, 1, 0);
            if (!plugin.getHooksManager().canTeleport(location, player)) {
                randomTeleport(player, world, minX, minZ, maxX, maxZ);
                return;
            }

            PaperLib.teleportAsync(player, tpLoc).thenAccept(success -> player.sendMessage(
                    plugin.getMessage("teleported")
                    .replaceAll("%x%", Integer.toString(tpLoc.getBlockX()))
                    .replaceAll("%y%", Integer.toString(tpLoc.getBlockY()))
                    .replaceAll("%z%", Integer.toString(tpLoc.getBlockZ()))
            ));
        });
    }
    
}
