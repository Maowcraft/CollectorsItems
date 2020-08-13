package maowcraft.collectorsitems;

import maowcraft.collectorsitems.command.CommandCombine;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class CollectorsItems extends JavaPlugin {

    @Override
    public void onEnable() {
        Objects.requireNonNull(this.getCommand("combine")).setExecutor(new CommandCombine());
    }

    @Override
    public void onDisable() {

    }
}
