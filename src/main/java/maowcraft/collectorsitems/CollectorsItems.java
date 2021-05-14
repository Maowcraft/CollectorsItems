package maowcraft.collectorsitems;

import maowcraft.collectorsitems.command.CommandCombine;
import maowcraft.collectorsitems.event.BlockEvents;
import maowcraft.collectorsitems.util.MetadataReload;
import maowcraft.collectorsitems.util.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class CollectorsItems extends JavaPlugin {
    private static CollectorsItems instance;

    @Override
    public void onEnable() {
        instance = this;

        registerEvents();

        Objects.requireNonNull(this.getCommand("combine")).setExecutor(new CommandCombine());

        this.getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        Config.setup();
        MetadataReload.reloadBlockMetadata();
    }

    @Override
    public void onDisable() {
        Config.save();
    }

    private void registerEvents() {
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(new BlockEvents(), this);
    }

    public static CollectorsItems getInstance() {
        return instance;
    }
}
