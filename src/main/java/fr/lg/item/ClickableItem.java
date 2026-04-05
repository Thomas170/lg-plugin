package fr.lg.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Interface pour un item cliquable interactif.
 */
public interface ClickableItem {

    /**
     * Retourne l'ItemStack à afficher.
     *
     * @return L'ItemStack de cet item
     */
    ItemStack getItemStack();

    /**
     * Retourne le nom de l'item (pour la comparaison).
     *
     * @return Le nom d'affichage de l'item
     */
    String getDisplayName();

    /**
     * Gère l'interaction quand le joueur clique droit sur l'item.
     *
     * @param player Le joueur qui a cliqué
     */
    void onRightClick(Player player);

    /**
     * Indique si cet item doit être donné au joueur au spawn.
     *
     * @param player Le joueur
     * @return true si le joueur doit recevoir cet item
     */
    boolean shouldGiveToPlayer(Player player);
}

