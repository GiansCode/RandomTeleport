package io.alerium.randomteleport;

import io.alerium.randomteleport.commands.RandomTPCommand;
import io.alerium.randomteleport.commands.ReloadCommand;
import io.alerium.randomteleport.cooldowns.CooldownManager;
import io.alerium.randomteleport.cooldowns.listeners.CooldownListener;
import io.alerium.randomteleport.hooks.HooksManager;
import io.alerium.randomteleport.utils.WorldBorderUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class RandomTeleportPlugin extends JavaPlugin {

    @Getter private static RandomTeleportPlugin instance;
    
    private CooldownManager cooldownManager;
    private HooksManager hooksManager;
    
    private String world;
    private int cooldown;
    private int warmup;
    
    private int minRangeX;
    private int minRangeZ;
    private int maxRangeX;
    private int maxRangeZ;
    
    @Override
    public void onEnable() {
        instance = this;
        
        saveDefaultConfig();
        setupConfig();
        setupRanges();
        
        registerInstances();
        registerListeners();
        registerCommands();
        
        getLogger().info("Plugin enabled");
    }

    public String getMessage(String path) {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages." + path));
    }
    
    public void reload() {
        reloadConfig();
        
        setupConfig();
        setupRanges();
        
        hooksManager.reload();
    }
    
    private void setupConfig() {
        world = getConfig().getString("world-to-teleport-to");
        cooldown = getConfig().getBoolean("cooldown.enabled") ? getConfig().getInt("cooldown.time") : -1;
        warmup = getConfig().getBoolean("warmup.enabled") ? getConfig().getInt("warmup.time") : -1;
    }
    
    private void setupRanges() {
        ConfigurationSection section = getConfig().getConfigurationSection("hook-into-worldborder");
        if (section.getBoolean("enabled")) {
            minRangeX = 0;
            minRangeZ = 0;
            
            int[] data = WorldBorderUtil.getWorldRanges(world);
            maxRangeX = data[0];
            maxRangeZ = data[1];
            return;
        }
        
        minRangeX = section.getInt("min-range-x");
        minRangeZ = section.getInt("min-range-z");
        
        maxRangeX = section.getInt("max-range-x");
        maxRangeZ = section.getInt("max-range-z");
    }

    private void registerInstances() {
        cooldownManager = new CooldownManager();
        cooldownManager.enable();
        
        hooksManager = new HooksManager();
        hooksManager.reload();
    }
    
    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new CooldownListener(), this);
    }
    
    private void registerCommands() {
        getCommand("randomteleport").setExecutor(new RandomTPCommand());
        getCommand("rtpreload").setExecutor(new ReloadCommand());
    }
    
}
