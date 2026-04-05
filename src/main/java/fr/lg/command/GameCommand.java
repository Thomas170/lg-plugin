package fr.lg.command;

import org.bukkit.command.CommandSender;

/**
 * Interface abstraite pour toutes les commandes du plugin.
 * Chaque commande doit implémenter cette interface.
 */
public interface GameCommand {

    /**
     * @return Le nom de la commande (ex : "start", "join")
     */
    String getName();

    /**
     * @return La description de la commande
     */
    String getDescription();

    /**
     * @return L'utilisation de la commande (ex : "/lg start [gameId]")
     */
    String getUsage();

    /**
     * Exécute la commande
     *
     * @param sender La personne qui exécute la commande
     * @param args Les arguments de la commande
     * @return true si la commande a été exécutée avec succès
     */
    boolean execute(CommandSender sender, String[] args);

    /**
     * @return true si la commande est réservée aux opérateurs, false sinon
     */
    boolean isOperatorOnly();
}

