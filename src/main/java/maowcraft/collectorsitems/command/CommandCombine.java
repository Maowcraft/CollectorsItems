package maowcraft.collectorsitems.command;

import maowcraft.collectorsitems.util.InventoryUtils;
import maowcraft.collectorsitems.util.RequiredAmount;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class CommandCombine implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PlayerInventory inventory = player.getInventory();
            ItemStack stack = inventory.getItem(inventory.getHeldItemSlot());
            if (stack != null) {
                Material material = stack.getType();
                List<ItemStack> items = InventoryUtils.getAllItems(inventory);
                RequiredAmount requiredAmount = RequiredAmount.SIXTY_FOUR_STACK;
                boolean gotRequiredAmount = false;
                int totalFound = 0;
                for (ItemStack item : items) {
                    if (item.getType().equals(material)) {
                        totalFound += item.getAmount();
                    }
                    if (!gotRequiredAmount) {
                        gotRequiredAmount = true;
                        switch(item.getMaxStackSize()) {
                            case 1:
                                requiredAmount = RequiredAmount.SINGLE_STACK;
                                break;
                            case 16:
                                requiredAmount = RequiredAmount.SIXTEEN_STACK;
                                break;
                            case 64:
                                requiredAmount = RequiredAmount.SIXTY_FOUR_STACK;
                                break;
                        }
                    }
                }
                if (totalFound == requiredAmount.amount) {
                    for (ItemStack item : items) {
                        inventory.remove(item);
                    }
                    ItemStack itemStack = new ItemStack(material);
                    ItemMeta meta = itemStack.getItemMeta();
                    assert meta != null;
                    String name = InventoryUtils.getNameFromType(itemStack.getType());
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Collector's " + name + "&r"));
                    meta.setLore(Collections.singletonList(ChatColor.GRAY + "Crafted by " + player.getDisplayName()));
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
                    meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
                    meta.setUnbreakable(true);
                    itemStack.setItemMeta(meta);
                    inventory.addItem(itemStack);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[Collector's Items]&r Crafted a &6Collector's " + name + "&r."));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[Collector's Items]&r Missing " + (requiredAmount.amount - totalFound) + " required items."));
                }
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[Collector's Items]&r Not holding an item."));
            }
            return true;
        }
        return false;
    }
}