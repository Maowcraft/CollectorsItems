package maowcraft.collectorsitems.command;

import maowcraft.collectorsitems.util.InventoryUtils;
import maowcraft.collectorsitems.util.RequiredAmount;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
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
                List<ItemStack> toDelete = new ArrayList<>();
                for (ItemStack item : items) {
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
                    if (item.getType().equals(material)) {
                        if (totalFound < requiredAmount.amount) {
                            totalFound += item.getAmount();
                            toDelete.add(item);
                        }
                    }
                }
                if (totalFound >= requiredAmount.amount) {
                    for (ItemStack item1 : toDelete) {
                        inventory.removeItem(item1);
                    }
                    ItemStack itemStack = new ItemStack(material);
                    ItemMeta meta = itemStack.getItemMeta();
                    assert meta != null;
                    String name = InventoryUtils.getNameFromType(itemStack.getType());
                    InventoryUtils.makeStackCollectors(itemStack, meta, player, name, player.getDisplayName(), null);
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
