package fr.lg.gui.impl;

import fr.lg.game.GameManager;
import fr.lg.gui.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Menu admin principal avec options pour gérer la partie.
 */
public class AdminMenu extends AbstractMenu {

    public AdminMenu(Player player) {
        super(player, "§6Menu Admin", 27);
    }

    @Override
    protected void setupMenu() {
        // Item pour gérer les rôles
        MenuItem rolesItem = new MenuItemBuilder(Material.WRITABLE_BOOK)
            .setName("§6Gérer les rôles")
            .setLore("Cliquez pour gérer les rôles de la partie")
            .onClick((p) -> {
                RolesMenu rolesMenu = new RolesMenu(p);
                rolesMenu.setPreviousMenu(this);
                MenuManager.openMenu(p, rolesMenu);
            })
            .build(11);
        addItem(11, rolesItem);

        // Item pour voir la liste des joueurs
        MenuItem playersItem = new MenuItemBuilder(Material.PAPER)
            .setName("§6Liste des joueurs")
            .setLore("Cliquez pour voir la liste des joueurs")
            .onClick((p) -> {
                PlayersMenu playersMenu = new PlayersMenu(p);
                playersMenu.setPreviousMenu(this);
                MenuManager.openMenu(p, playersMenu);
            })
            .build(13);
        addItem(13, playersItem);

        // Item pour démarrer la partie
        MenuItem startItem = new MenuItemBuilder(Material.LIME_CONCRETE)
            .setName("§aLancer la partie")
            .setLore("Cliquez pour lancer la partie")
            .onClick((p) -> {
                p.sendMessage("§a La partie a été lancée!");
                p.closeInventory();
                GameManager.startGame();
            })
            .build(15);
        addItem(15, startItem);
    }
}

