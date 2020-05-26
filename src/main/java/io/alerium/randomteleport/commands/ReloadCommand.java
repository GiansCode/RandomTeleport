package io.alerium.randomteleport.commands;

import io.alerium.randomteleport.RandomTeleportPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
    
    private final RandomTeleportPlugin plugin = RandomTeleportPlugin.getInstance();
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("randomteleport.reload")) {
            sender.sendMessage(plugin.getMessage("noPermission"));
            return true;
        }
        
        plugin.reload();
        sender.sendMessage(plugin.getMessage("reloaded"));
        return true;
    }
    
}
