package fr.lg.roles.impl;

import fr.lg.roles.AbstractRole;
import fr.lg.roles.RoleTeam;
import fr.lg.roles.RoleType;

/**
 * Rôle : Voyante
 * Role spécial de l'équipe des villageois.
 * La voyante peut deviner le rôle d'un joueur chaque nuit.
 */
public class Seer extends AbstractRole {

    @Override
    public String getName() {
        return "Voyante";
    }

    @Override
    public String getDescription() {
        return "Un membre spécial des villageois ! Chaque nuit, vous pouvez deviner le rôle d'un joueur.";
    }

    @Override
    public RoleTeam getTeam() {
        return RoleTeam.VILLAGER;
    }

    @Override
    public RoleType getType() {
        return RoleType.SEER;
    }
}
