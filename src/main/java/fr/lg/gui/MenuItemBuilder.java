package fr.lg.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Builder pour faciliter la création d'items de menu.
 */
public class MenuItemBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;
    private MenuItem.ItemClickHandler clickHandler;

    public MenuItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    /**
     * Définit le nom de l'item.
     *
     * @param name Le nom à afficher
     * @return this pour le chaînage
     */
    public MenuItemBuilder setName(String name) {
        if (itemMeta != null) {
            itemMeta.setDisplayName(name);
        }
        return this;
    }

    /**
     * Ajoute une description à l'item.
     *
     * @param lore Les lignes de description
     * @return this pour le chaînage
     */
    public MenuItemBuilder setLore(String... lore) {
        if (itemMeta != null) {
            itemMeta.setLore(Arrays.asList(lore));
        }
        return this;
    }

    /**
     * Définit l'action au clic.
     *
     * @param handler L'action à exécuter
     * @return this pour le chaînage
     */
    public MenuItemBuilder onClick(MenuItem.ItemClickHandler handler) {
        this.clickHandler = handler;
        return this;
    }

    /**
     * Définit l'action au clic avec une fonction simple.
     *
     * @param action La fonction à exécuter
     * @return this pour le chaînage
     */
    public MenuItemBuilder onClick(Consumer<Player> action) {
        this.clickHandler = (player, event) -> action.accept(player);
        return this;
    }

    /**
     * Construit le MenuItem.
     *
     * @param slot La position dans l'inventaire
     * @return Le MenuItem construit
     */
    public MenuItem build(int slot) {
        if (itemMeta != null) {
            itemStack.setItemMeta(itemMeta);
        }
        return new MenuItem(itemStack, slot, clickHandler);
    }

    /**
     * Crée un bouton de retour au menu précédent.
     *
     * @param onBack Action à exécuter au retour
     * @return Le MenuItem du bouton de retour
     */
    public static MenuItem createBackButton(Runnable onBack) {
        MenuItem.ItemClickHandler handler = (player, event) -> onBack.run();

        ItemStack itemStack = new ItemStack(Material.ARROW);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName("§c Retour");
            itemStack.setItemMeta(itemMeta);
        }

        return new MenuItem(itemStack, 0, handler);
    }

    /**
     * Crée un bouton pour fermer l'inventaire.
     *
     * @return Le MenuItem du bouton de fermeture
     */
    public static MenuItem createCloseButton() {
        MenuItem.ItemClickHandler handler = (player, event) -> {
            MenuManager.closeMenu(player);
            player.closeInventory();
        };

        ItemStack itemStack = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName("§c Fermer");
            itemStack.setItemMeta(itemMeta);
        }

        return new MenuItem(itemStack, 0, handler);
    }
}

