package fr.lg.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Manager statique pour gérer les menus ouverts et les interactions.
 * Fonctionnement similaire à RoleManager, CommandManager, etc.
 */
public class MenuManager implements Listener {
    private static final Map<Player, AbstractMenu> openMenus = new HashMap<>();
    private static boolean initialized = false;

    /**
     * Initialise le MenuManager.
     * À appeler une seule fois au démarrage du plugin.
     *
     * @param pluginInstance L'instance du plugin
     */
    public static void initialize(JavaPlugin pluginInstance) {
        if (initialized) {
            return;
        }
        pluginInstance.getServer().getPluginManager().registerEvents(new MenuManager(), pluginInstance);
        initialized = true;
    }

    /**
     * Ouvre un menu pour un joueur.
     *
     * @param player Le joueur
     * @param menu Le menu à ouvrir
     */
    public static void openMenu(Player player, AbstractMenu menu) {
        openMenus.put(player, menu);
        menu.open();
    }

    /**
     * Obtient le menu actuellement ouvert du joueur.
     *
     * @param player Le joueur
     * @return Le menu ouvert ou null
     */
    public static AbstractMenu getOpenMenu(Player player) {
        return openMenus.get(player);
    }

    /**
     * Ferme le menu d'un joueur.
     *
     * @param player Le joueur
     */
    public static void closeMenu(Player player) {
        openMenus.remove(player);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        // Récupère le menu de l'inventaire via le MenuInventoryHolder
        if (event.getInventory().getHolder() instanceof MenuInventoryHolder holder) {
            AbstractMenu menu = holder.getMenu();
            event.setCancelled(true);
            int slot = event.getSlot();
            if (slot >= 0) {
                menu.handleClick(slot, player, event);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        // Récupère le menu de l'inventaire via le MenuInventoryHolder
        if (event.getInventory().getHolder() instanceof MenuInventoryHolder holder) {
            AbstractMenu menu = holder.getMenu();
            openMenus.remove(player, menu);
        }
    }
}

