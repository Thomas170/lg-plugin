package fr.lg.command;

import fr.lg.command.impl.*;
import org.bukkit.command.CommandSender;

import java.util.*;

/**
 * Gestionnaire centralisé pour toutes les commandes du plugin.
 * Permet l'enregistrement et l'exécution des commandes.
 */
public class CommandManager {

    public static boolean executeCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§e=== Commandes Loup-Garou ===");
            for (CommandType type : CommandType.values()) {
                GameCommand cmd = type.getCommand();
                sender.sendMessage("§f" + cmd.getUsage() + " §8- §7" + cmd.getDescription());
            }
            return true;
        }

        String subCommand = args[0];
        CommandType commandType;

        try {
            commandType = CommandType.valueOf(subCommand.toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage("§cCommande inconnue: " + subCommand);
            return false;
        }

        String[] cmdArgs = Arrays.copyOfRange(args, 1, args.length);

        GameCommand gameCommand = commandType.getCommand();

        // Vérifier les permissions
        if (gameCommand.isOperatorOnly() && !sender.isOp()) {
            sender.sendMessage("§cVous n'avez pas la permission");
            return false;
        }

        return gameCommand.execute(sender, cmdArgs);
    }
}

