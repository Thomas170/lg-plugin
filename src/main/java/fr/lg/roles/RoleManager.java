package fr.lg.roles;

import fr.lg.LGPlugin;
import fr.lg.player.GamePlayer;
import fr.lg.player.PlayerManager;
import lombok.Getter;

import java.util.*;

/**
 * Gestionnaire de tous les joueurs d'une partie.
 * Gère l'ajout, le retrait et l'accès aux joueurs.
 */
public class RoleManager {

    @Getter
    private static final Map<RoleType, Integer> rolesInGame = new HashMap<>();

    /**
     * Ajoute un rôle à la partie
     *
     * @param type Le type de rôle à ajouter
     */
    public static void add(RoleType type) {
        rolesInGame.merge(type, 1, Integer::sum);
    }

    /**
     * Retire un rôle de la partie
     *
     * @param type Le type de rôle à retirer
     */
    public static void remove(RoleType type) {
        rolesInGame.merge(type, -1, (oldVal, newVal) -> {
            int result = oldVal + newVal;
            return result > 0 ? result : null;
        });
    }

    /**
     * Assigne un rôle à un joueur, en respectant les limites configurées
     *
     * @param gamePlayer Le joueur auquel assigner le rôle
     * @param type Le type de rôle à assigner
     */
    public static void assignRole(GamePlayer gamePlayer, RoleType type) {
        if (canAssignRole(type)) {
            gamePlayer.setRole(type.getRole());
        }
    }

    /**
     * Vérifie si un rôle de ce type peut être assigné (en respectant les limites configurées)
     *
     * @param type Le type de rôle à vérifier
     * @return true si le rôle peut être assigné, false sinon
     */
    private static boolean canAssignRole(RoleType type) {
        if (!rolesInGame.containsKey(type)) {
            LGPlugin.getInstance().getLogger().warning("RoleManager: Type de rôle non défini en config: " + type);
            return false;
        }

        long assignedCount = PlayerManager.getAll()
                .stream()
                .filter(p -> p.getRole() != null && p.getRole().getType() == type)
                .count();

        int maxCount = rolesInGame.get(type);
        if (assignedCount >= maxCount) {
            LGPlugin.getInstance().getLogger().warning("RoleManager: Limite atteinte pour le rôle " + type + " (" + assignedCount + "/" + maxCount + ")");
        }

        return assignedCount < maxCount;
    }

    public static List<RoleType> getAll() {
        return new ArrayList<>(rolesInGame.keySet());
    }
}

