package fr.lg.gui;

import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Classe pour associer un menu à un inventaire Bukkit.
 * Cela permet de retrouver le menu à partir de l'événement d'inventaire.
 */
@Getter
public class MenuInventoryHolder implements InventoryHolder {
    private final AbstractMenu menu;

    public MenuInventoryHolder(AbstractMenu menu) {
        this.menu = menu;
    }

    @Override
    public Inventory getInventory() {
        return menu.getInventory();
    }
}

