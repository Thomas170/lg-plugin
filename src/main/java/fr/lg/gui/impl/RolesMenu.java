package fr.lg.gui.impl;

import fr.lg.gui.*;
import fr.lg.roles.RoleManager;
import fr.lg.roles.RoleType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Menu pour gérer la liste des rôles disponibles.
 * Affiche les rôles et permet d'ajouter/supprimer des rôles de la partie.
 */
public class RolesMenu extends AbstractMenu {

    public RolesMenu(Player player) {
        super(player, "§6Liste des rôles", 54);
    }

    @Override
    protected void setupMenu() {
        setupPagination();
    }

    @Override
    protected List<?> getItems() {
        return new ArrayList<>(List.of(RoleType.values()));
    }

    @Override
    protected MenuItem createItemForElement(Object item, int index) {
        RoleType roleType = (RoleType) item;

        // Vérifier que le rôle n'est pas null
        if (roleType.getRole() == null) {
            return null;
        }

        // Déterminer la limite max pour ce rôle
        int maxCount = roleType.getRole().getMaxNumberInGame();

        // Récupérer le nombre de ce rôle dans la config actuelle
        int currentCount = RoleManager.getRolesInGame().getOrDefault(roleType, 0);

        String loreText = currentCount > 0
            ? "§7Actuellement: §e" + currentCount + "§7/§e" + maxCount
            : "§7Non sélectionné (max: §e" + maxCount + "§7)";

        return new MenuItemBuilder(Material.BOOK)
            .setName("§e" + roleType.name())
            .setLore(
                loreText,
                "§7Clic gauche: ajouter",
                "§7Clic droit: retirer"
            )
            .onClick((p, event) -> {
                int current = RoleManager.getRolesInGame().getOrDefault(roleType, 0);
                int max = roleType.getRole().getMaxNumberInGame();

                // Clic gauche = ajouter (si pas au max)
                if (event.isLeftClick()) {
                    if (current < max) {
                        RoleManager.add(roleType);
                        p.sendMessage("§a✓ Rôle §e" + roleType.name() + " §aajouté (§e" + (current + 1) + "§a/§e" + max + "§a)");
                        refresh();
                    } else {
                        p.sendMessage("§cLimite atteinte pour ce rôle (§e" + max + "§c)");
                    }
                }
                // Clic droit = retirer
                else if (event.isRightClick()) {
                    if (current > 0) {
                        RoleManager.remove(roleType);
                        p.sendMessage("§a✓ Rôle §e" + roleType.name() + " §aretiré (§e" + (current - 1) + "§a/§e" + max + "§a)");
                        refresh();
                    } else {
                        p.sendMessage("§cCe rôle n'est pas configuré");
                    }
                }
            })
            .build(9 + index);
    }
}

