package fr.lg.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Représente un item cliquable dans un menu.
 */
public record MenuItem(
    ItemStack itemStack,
    int slot,
    ItemClickHandler clickHandler
) {
    /**
     * Interface fonctionnelle pour gérer les clics sur un item.
     */
    @FunctionalInterface
    public interface ItemClickHandler {
        void onClick(Player player, InventoryClickEvent event);
    }
}
