package maowcraft.collectorsitems.util;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class InventoryUtils {
    public static List<ItemStack> getAllItems(PlayerInventory inventory) {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack != null) {
                items.add(inventory.getItem(i));
            }
        }
        return items;
    }

    public static String getNameFromType(Material material) {
        return WordUtils.capitalizeFully(material.name().replace('_', ' ').toLowerCase());
    }

    public static void makeStackCollectors(ItemStack itemStack, ItemMeta meta, Player player, String name, String crafter, Location dropLocation) {
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        dataContainer.set(PersistentDataKeys.COLLECTORS, PersistentDataType.INTEGER, 1);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',  "&6Collector's " + name + "&r"));
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Crafted by " + crafter);
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.setUnbreakable(true);
        itemStack.setItemMeta(meta);
        if (dropLocation == null) {
            player.getInventory().addItem(itemStack);
        } else {
            if (player.getGameMode() != GameMode.CREATIVE) {
                player.getWorld().dropItemNaturally(dropLocation, itemStack);
            }
        }
    }
}
