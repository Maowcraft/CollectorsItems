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
                RequiredAmount requiredAmount;
                switch(stack.getMaxStackSize()) {
                    case 1:
                        requiredAmount = RequiredAmount.SINGLE_STACK;
                        break;
                    case 16:
                        requiredAmount = RequiredAmount.SIXTEEN_STACK;
                        break;
                    default:
                        requiredAmount = RequiredAmount.SIXTY_FOUR_STACK;
                        break;
                }
                int amountFound = 0;
                for (ItemStack item : items) {
                    if (item.getType() == material) {
                        amountFound += item.getAmount();
                        if (amountFound >= requiredAmount.amount) break;
                    }
                }
                int extraItemAmount = (amountFound - requiredAmount.amount);
                if (extraItemAmount > 0) {
                    amountFound -= extraItemAmount;
                }
                if (amountFound >= requiredAmount.amount) {
                    int amountLeft = amountFound;
                    while (amountLeft > stack.getMaxStackSize()) {
                        inventory.removeItem(new ItemStack(stack.getType(), stack.getMaxStackSize()));
                        amountLeft -= stack.getMaxStackSize();
                    }
                    inventory.removeItem(new ItemStack(stack.getType(), amountLeft));
                    ItemStack stack1 = new ItemStack(material);
                    ItemMeta meta = stack1.getItemMeta();
                    assert meta != null;
                    String name = InventoryUtils.getNameFromType(stack1.getType());
                    InventoryUtils.makeStackCollectors(stack1, meta, player, name, player.getDisplayName(), null);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[Collector's Items]&r Crafted a &6Collector's " + name + "&r."));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[Collector's Items]&r Missing " + (requiredAmount.amount - amountFound) + " required items."));
                }
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[Collector's Items]&r Not holding an item."));
            }
            return true;
        }
        return false;
    }
}
