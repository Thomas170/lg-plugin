package fr.lg.listener;

import fr.lg.item.ItemManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Listener pour les clics sur les items spéciaux.
 * Gère automatiquement tous les items cliquables via ItemManager.
 */
public class ItemClickListener implements Listener {

    /**
     * Gère les clics droits sur les items spéciaux.
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getItemMeta() == null) {
            return;
        }

        String itemName = item.getItemMeta().getDisplayName();

        // Gère dynamiquement tous les items cliquables
        ItemManager.handleItemClick(itemName, player);
    }
}

