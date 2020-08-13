package maowcraft.collectorsitems.util;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

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
}
