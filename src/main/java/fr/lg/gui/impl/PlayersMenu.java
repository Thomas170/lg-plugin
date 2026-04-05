package fr.lg.gui.impl;

import fr.lg.gui.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Menu affichant la liste des joueurs actuellement connectés.
 */
public class PlayersMenu extends AbstractMenu {

    public PlayersMenu(Player player) {
        super(player, "§6Liste des joueurs", 54);
    }

    @Override
    protected void setupMenu() {
        setupPagination();
    }

    @Override
    protected List<?> getItems() {
        return new ArrayList<>(Bukkit.getOnlinePlayers());
    }

    @Override
    protected MenuItem createItemForElement(Object item, int index) {
        Player targetPlayer = (Player) item;

        return new MenuItemBuilder(Material.PLAYER_HEAD)
            .setName("§e" + targetPlayer.getName())
            .setLore(
                "§7UUID: " + targetPlayer.getUniqueId().toString().substring(0, 8) + "...",
                "§7Monde: " + targetPlayer.getWorld().getName()
            )
            .build(9 + index);
    }
}

