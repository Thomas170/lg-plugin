package fr.lg.command.impl;

import fr.lg.command.AbstractCommand;
import fr.lg.roles.Role;
import fr.lg.roles.RoleType;
import org.bukkit.command.CommandSender;

/**
 * Commande : /lg roledesc <roleName>
 * Affiche la description d'un rôle spécifique.
 */
public class RoleDescriptionCommand extends AbstractCommand {

    @Override
    public String getName() {
        return "description";
    }

    @Override
    public String getDescription() {
        return "Afficher la description d'un rôle spécifique";
    }

    @Override
    public String getUsage() {
        return "/lg description <roleName>";
    }

    @Override
    public boolean isOperatorOnly() {
        return false;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        String roleName = args[0].toUpperCase();
        RoleType roleType;
        try {
            roleType = RoleType.valueOf(roleName);
        } catch (IllegalArgumentException e) {
            sender.sendMessage("§cRôle inconnu: " + roleName);
            return false;
        }

        Role role = roleType.getRole();
        sender.sendMessage("§e=== Description du rôle: " + role.getName() + " ===");
        sender.sendMessage("§e" + role.getDescription());

        return true;
    }
}

