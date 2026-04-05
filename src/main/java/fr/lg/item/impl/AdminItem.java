package fr.lg.item.impl;

import fr.lg.gui.MenuManager;
import fr.lg.gui.impl.AdminMenu;
import fr.lg.item.AbstractClickableItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Item admin pour ouvrir le menu d'administration.
 * Réservé aux administrateurs du serveur.
 */
public class AdminItem extends AbstractClickableItem {

    public AdminItem() {
        super(Material.NETHER_STAR, "§6 Configurer la partie");
    }

    @Override
    protected void setupItemMeta() {
        super.setupItemMeta();
        setLore(
            "§7Cliquez droit pour ouvrir",
            "§7le menu d'administration"
        );
    }

    @Override
    public void onRightClick(Player player) {
        if (!player.isOp()) {
            player.sendMessage("§cVous n'êtes pas administrateur!");
            return;
        }

        AdminMenu adminMenu = new AdminMenu(player);
        MenuManager.openMenu(player, adminMenu);
    }

    @Override
    public boolean shouldGiveToPlayer(Player player) {
        return player.isOp();
    }
}

