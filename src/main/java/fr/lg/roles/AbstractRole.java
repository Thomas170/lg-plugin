package fr.lg.roles;

import lombok.Getter;

/**
 * Classe abstraite de base pour tous les rôles.
 * Fournit une implémentation par défaut des méthodes du Role.
 */
@Getter
public abstract class AbstractRole implements Role {
    @Override
    public String getName() {
        return "Rôle non défini";
    }

    @Override
    public String getDescription() {
        return "Description non définie";
    }

    @Override
    public RoleTeam getTeam() {
        return RoleTeam.INCONNU;
    }

    @Override
    public RoleType getType() {
        return RoleType.INCONNU;
    }

    @Override
    public Integer getMaxNumberInGame() {
        return 1;
    }
}

