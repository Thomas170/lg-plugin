package fr.lg.gui;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe abstraite représentant un menu-inventaire générique.
 * Supporte aussi les menus avec pagination pour les listes.
 */
public abstract class AbstractMenu {

    @Getter
    protected final Player player;
    protected final String title;
    protected final int size;
    @Getter
    protected Inventory inventory;
    protected final Map<Integer, MenuItem> menuItems = new HashMap<>();
    @Setter
    protected AbstractMenu previousMenu;

    // Pour la pagination (menus avec listes)
    protected static final int ITEMS_PER_PAGE = 45;
    protected int currentPage = 0;

    /**
     * Crée un nouveau menu.
     *
     * @param player Le joueur qui verra le menu
     * @param title Le titre de l'inventaire
     * @param size La taille de l'inventaire (9, 18, 27, 36, 45, 54)
     */
    public AbstractMenu(Player player, String title, int size) {
        this.player = player;
        this.title = title;
        this.size = size;
    }

    /**
     * Ajoute un item au menu.
     *
     * @param slot La position dans l'inventaire
     * @param menuItem L'item à ajouter
     */
    public void addItem(int slot, MenuItem menuItem) {
        menuItems.put(slot, menuItem);
    }

    /**
     * Crée et affiche le menu.
     */
    public void open() {
        this.inventory = Bukkit.createInventory(new MenuInventoryHolder(this), size, title);
        menuItems.clear();
        setupMenu();
        addBackButton();
        fillInventory();
        player.openInventory(inventory);
    }

    /**
     * Configure le menu avec les items spécifiques.
     * À implémenter dans les sous-classes.
     */
    protected abstract void setupMenu();

    /**
     * Ajoute le bouton de retour en haut à gauche.
     */
    protected void addBackButton() {
        if (previousMenu != null) {
            MenuItem backItem = MenuItemBuilder.createBackButton(() -> {
                MenuManager.openMenu(player, previousMenu);
            });
            addItem(0, backItem);
        } else {
            MenuItem closeItem = MenuItemBuilder.createCloseButton();
            addItem(0, closeItem);
        }
    }

    /**
     * Gère le clic sur un item du menu.
     *
     * @param slot La position du slot cliqué
     * @param player Le joueur qui a cliqué
     */
    public void handleClick(int slot, Player player, org.bukkit.event.inventory.InventoryClickEvent event) {
        MenuItem menuItem = menuItems.get(slot);
        if (menuItem != null && menuItem.clickHandler() != null) {
            menuItem.clickHandler().onClick(player, event);
        }
    }

    /**
     * Remplit l'inventaire avec les items enregistrés.
     */
    protected void fillInventory() {
        for (Map.Entry<Integer, MenuItem> entry : menuItems.entrySet()) {
            inventory.setItem(entry.getKey(), entry.getValue().itemStack());
        }
    }

    /**
     * Retourne la liste des éléments à afficher (pour pagination).
     * À surcharger si le menu utilise la pagination.
     *
     * @return La liste des éléments, ou null si pas de pagination
     */
    protected List<?> getItems() {
        return null;
    }

    /**
     * Crée un item pour un élément de liste (pour pagination).
     * À surcharger si le menu utilise la pagination.
     *
     * @param item L'élément
     * @param index L'index
     * @return Le MenuItem créé
     */
    protected MenuItem createItemForElement(Object item, int index) {
        return null;
    }

    /**
     * Configure la pagination pour les menus avec listes.
     * À appeler dans setupMenu() pour les menus paginés.
     */
    protected void setupPagination() {
        List<?> items = getItems();
        if (items == null || items.isEmpty()) {
            return;
        }

        int startIndex = currentPage * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, items.size());

        int slot = 9; // Commence à la deuxième ligne
        int pageIndex = 0;
        for (int i = startIndex; i < endIndex; i++) {
            MenuItem item = createItemForElement(items.get(i), pageIndex);
            if (item != null) {
                addItem(slot, item);
                slot++;
            }
            pageIndex++;
        }

        // Boutons de pagination
        int maxPages = (items.size() + ITEMS_PER_PAGE - 1) / ITEMS_PER_PAGE;
        if (currentPage > 0) {
            MenuItem prevItem = new MenuItem(
                new org.bukkit.inventory.ItemStack(Material.ARROW),
                45,
                (p, event) -> {
                    currentPage--;
                    refresh();
                }
            );
            addItem(45, prevItem);
        }

        if (currentPage < maxPages - 1) {
            MenuItem nextItem = new MenuItem(
                new org.bukkit.inventory.ItemStack(Material.ARROW),
                53,
                (p, event) -> {
                    currentPage++;
                    refresh();
                }
            );
            addItem(53, nextItem);
        }
    }

    /**
     * Rafraîchit le menu en le réouvrant.
     */
    public void refresh() {
        menuItems.clear();
        open();
    }
}

