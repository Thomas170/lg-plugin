package fr.lg.roles.impl;

import fr.lg.roles.AbstractRole;
import fr.lg.roles.RoleTeam;
import fr.lg.roles.RoleType;

/**
 * Rôle : Loup-Garou
 * L'équipe des loups-garous doit éliminer tous les villageois.
 * Les loups-garous peuvent tuer un villageois pendant la nuit.
 */
public class Werewolf extends AbstractRole {

    @Override
    public String getName() {
        return "Loup-Garou";
    }

    @Override
    public String getDescription() {
        return "Un membre de l'équipe des loups-garous ! Chaque nuit, vous pouvez choisir un villageois à éliminer.";
    }

    @Override
    public RoleTeam getTeam() {
        return RoleTeam.WEREWOLF;
    }

    @Override
    public RoleType getType() {
        return RoleType.WEREWOLF;
    }

    @Override
    public Integer getMaxNumberInGame() {
        return 3;
    }
}

