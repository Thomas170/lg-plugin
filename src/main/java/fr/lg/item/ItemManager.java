package fr.lg.item;

import org.bukkit.entity.Player;

/**
 * Manager pour gérer les items cliquables.
 * Gère les interactions et les distributions d'items.
 */
public class ItemManager {

    /**
     * Gère l'interaction quand un joueur clique droit sur un item.
     *
     * @param displayName Le nom d'affichage de l'item
     * @param player Le joueur qui a cliqué
     */
    public static void handleItemClick(String displayName, Player player) {
        ItemType itemType = ItemType.getByDisplayName(displayName);

        if (itemType != null) {
            itemType.getItem().onRightClick(player);
        }
    }

    /**
     * Donne les items appropriés à un joueur.
     *
     * @param player Le joueur
     */
    public static void giveItemsToPlayer(Player player) {
        player.getInventory().clear();

        for (ItemType itemType : ItemType.values()) {
            if (itemType.getItem().shouldGiveToPlayer(player)) {
                player.getInventory().addItem(itemType.getItem().getItemStack());
            }
        }
    }
}

