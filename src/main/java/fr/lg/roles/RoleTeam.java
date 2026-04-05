package fr.lg.roles;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Énumération des différents rôles du jeu.
 */
@Getter
@RequiredArgsConstructor
public enum RoleTeam {
    VILLAGER,
    WEREWOLF,
    ALONE,
    INCONNU
}
