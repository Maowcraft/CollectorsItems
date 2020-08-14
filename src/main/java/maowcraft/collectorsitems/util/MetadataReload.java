package maowcraft.collectorsitems.util;

import maowcraft.collectorsitems.CollectorsItems;
import maowcraft.collectorsitems.util.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Objects;

public class MetadataReload {
    public static void reloadBlockMetadata() {
        Config.reload();
        FileConfiguration cfg = Config.getConfig();
        if (cfg.getKeys(false).size() != 0) {
            for (String key : Objects.requireNonNull(cfg.getConfigurationSection("blocks")).getKeys(false)) {
                String[] parts = Objects.requireNonNull(cfg.getString("blocks." + key + ".location")).split("/");
                World world = Bukkit.getServer().getWorld(parts[3]);
                Location location = new Location(world, Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                assert world != null;
                Block block = world.getBlockAt(location);
                block.setMetadata("collectors_crafter", new FixedMetadataValue(CollectorsItems.getInstance(), cfg.getString("blocks." + key + ".crafter")));
            }
        }
    }
}
