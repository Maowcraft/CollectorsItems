package maowcraft.collectorsitems.event;

import maowcraft.collectorsitems.CollectorsItems;
import maowcraft.collectorsitems.util.InventoryUtils;
import maowcraft.collectorsitems.util.MetadataReload;
import maowcraft.collectorsitems.util.PersistentDataKeys;
import maowcraft.collectorsitems.util.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Objects;

public class BlockEvents implements Listener {
    @SuppressWarnings("ConstantConditions")
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        assert meta != null;
        if (dataContainer.has(PersistentDataKeys.COLLECTORS, PersistentDataType.INTEGER)) {
            int isCollectors = dataContainer.get(PersistentDataKeys.COLLECTORS, PersistentDataType.INTEGER);
            String crafter = dataContainer.get(PersistentDataKeys.CRAFTER, PersistentDataType.STRING);
            if (isCollectors == 1) {
                Block block = event.getBlock();
                Location location = block.getLocation();
                String key = (location.getBlockX() + "" + location.getBlockY() + "" + location.getBlockZ());
                FileConfiguration cfg = Config.getConfig();
                cfg.set("blocks." + key + ".crafter", crafter);
                cfg.set("blocks." + key + ".location", location.getBlockX() + "/" + location.getBlockY() + "/" +
                        location.getBlockZ() + "/" + location.getWorld().getName());
                Config.save();
                block.setMetadata("collectors_crafter", new FixedMetadataValue(CollectorsItems.getInstance(), crafter));
            }
        }
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        Config.reload();
        FileConfiguration cfg = Config.getConfig();
        Block block = event.getBlock();
        ItemStack droppedItem = new ItemStack(block.getType());
        List<MetadataValue> metadata = block.getMetadata("collectors_crafter");
        for (MetadataValue metadataValue : metadata) {
            if (metadataValue.getOwningPlugin() == CollectorsItems.getInstance()) {
                String crafter = metadataValue.asString();
                ItemMeta meta = droppedItem.getItemMeta();
                assert meta != null;
                String name = InventoryUtils.getNameFromType(droppedItem.getType());
                event.setDropItems(false);
                InventoryUtils.makeStackCollectors(droppedItem, meta, event.getPlayer(), name, crafter, block.getLocation());
                if (cfg.getKeys(false).size() != 0) {
                    for (String key : Objects.requireNonNull(cfg.getConfigurationSection("blocks")).getKeys(false)) {
                        String[] parts = Objects.requireNonNull(cfg.getString("blocks." + key + ".location")).split("/");
                        World world = event.getPlayer().getWorld();
                        Location location = new Location(world, Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                        if (key.equals(location.getBlockX() + "" + location.getBlockY() + "" + location.getBlockZ())) {
                            cfg.set("blocks." + key, null);
                            Config.save();
                        }
                    }
                }
            }
        }
    }
}
