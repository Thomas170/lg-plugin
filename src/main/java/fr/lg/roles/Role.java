package fr.lg.roles;

/**
 * Interface abstraite pour tous les rôles du jeu Loup-Garou.
 * Chaque rôle doit implémenter cette interface.
 */
public interface Role {

    /**
     * @return Le nom du rôle
     */
    String getName();

    /**
     * @return La description du rôle
     */
    String getDescription();

    /**
     * @return L'équipe à laquelle appartient ce rôle
     */
    RoleTeam getTeam();

    /**
     * @return Le type de rôle (ex : VILLAGER, WEREWOLF, etc.)
     */
    RoleType getType();

    /**
     * @return Le nombre maximum de joueurs pouvant avoir ce rôle dans une partie
     */
    Integer getMaxNumberInGame();
}

