package fr.lg.command.impl;

import fr.lg.command.AbstractCommand;
import fr.lg.game.GameManager;
import fr.lg.game.GameState;
import fr.lg.player.GamePlayer;
import fr.lg.player.PlayerManager;
import fr.lg.roles.RoleManager;
import fr.lg.roles.RoleType;
import org.bukkit.command.CommandSender;

import java.util.*;

/**
 * Commande : /lg start
 * Démarre la partie de Loup-Garou en distribuant les rôles aléatoirement.
 */
public class StartGameCommand extends AbstractCommand {

    @Override
    public String getName() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "Démarrer la partie de Loup-Garou";
    }

    @Override
    public String getUsage() {
        return "/lg start";
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        List<GamePlayer> players = PlayerManager.getAll();
        List<RoleType> roles = RoleManager.getAll();

        if (players.size() < 4) {
            sender.sendMessage("§cLa partie n'a pas assez de joueurs (minimum 4)");
            return false;
        }

        if (roles.size() != players.size()) {
            sender.sendMessage("§cLe nombre de rôles configurés ne correspond pas au nombre de joueurs");
            sender.sendMessage("§cJoueurs: §f" + players.size() + " §cRôles: §f" + roles.size());
            return false;
        }

        // Mélanger les joueurs et assigner les rôles
        Collections.shuffle(players);
        for (int i = 0; i < players.size(); i++) {
            RoleManager.assignRole(players.get(i), roles.get(i));
        }

        GameManager.setState(GameState.PLAYING);
        sender.sendMessage("§a✓ Partie démarrée !");

        // Notifier tous les joueurs
        for (GamePlayer gamePlayer : PlayerManager.getAll()) {
            gamePlayer.getPlayer().sendMessage("§e§l=== LOUP-GAROU ===");
            if (gamePlayer.getRole() != null) {
                gamePlayer.getPlayer().sendMessage("§eVotre rôle: §f" + gamePlayer.getRole().getName());
                gamePlayer.getPlayer().sendMessage("§eÉquipe: §f" + gamePlayer.getRole().getTeam());
                gamePlayer.getPlayer().sendMessage("§e" + gamePlayer.getRole().getDescription());
            }
        }

        return true;
    }
}

