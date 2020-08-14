package maowcraft.collectorsitems.util;

import maowcraft.collectorsitems.CollectorsItems;
import org.bukkit.NamespacedKey;

public class PersistentDataKeys {
    public static final NamespacedKey COLLECTORS = new NamespacedKey(CollectorsItems.getInstance(), "collectors");
    public static final NamespacedKey TIER = new NamespacedKey(CollectorsItems.getInstance(), "collectors_tier");
    public static final NamespacedKey CRAFTER = new NamespacedKey(CollectorsItems.getInstance(), "collectors_crafter");
}
