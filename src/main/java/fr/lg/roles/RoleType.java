package fr.lg.roles;

import fr.lg.roles.impl.Seer;
import fr.lg.roles.impl.Villager;
import fr.lg.roles.impl.Werewolf;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Énumération des différents rôles du jeu.
 */
@Getter
@RequiredArgsConstructor
public enum RoleType {
    VILLAGER(new Villager()),
    WEREWOLF(new Werewolf()),
    SEER(new Seer()),
    INCONNU(null);

    private final Role role;
}
