package fr.lg.roles.impl;

import fr.lg.roles.AbstractRole;
import fr.lg.roles.RoleTeam;
import fr.lg.roles.RoleType;

/**
 * Rôle : Villageois
 * L'équipe des villageois doit éliminer tous les loups-garous.
 * Les villageois n'ont pas de pouvoir spécial pendant la nuit.
 */
public class Villager extends AbstractRole {

    @Override
    public String getName() {
        return "Villageois";
    }

    @Override
    public String getDescription() {
        return "Un membre de l'équipe des villageois ! Vous n'avez pas de pouvoir spécial, mais vous devez aider à éliminer les loups-garous.";
    }

    @Override
    public RoleTeam getTeam() {
        return RoleTeam.VILLAGER;
    }

    @Override
    public RoleType getType() {
        return RoleType.VILLAGER;
    }

    @Override
    public Integer getMaxNumberInGame() {
        return 5;
    }
}

