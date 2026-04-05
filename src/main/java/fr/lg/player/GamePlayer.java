package fr.lg.player;

import fr.lg.roles.Role;
import fr.lg.roles.RoleTeam;
import fr.lg.scoreboard.ScoreboardManager;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Contient toutes les informations d'un joueur dans le contexte du jeu.
 */
@Getter
@Setter
public class GamePlayer {

    private final Player player;
    private Role role;
    private boolean alive;

    public GamePlayer(Player player) {
        this.player = player;
        this.alive = true;
    }

    public void kill(String deathMessage) {
        this.alive = false;
        Bukkit.broadcast(Component.text(deathMessage));
        ScoreboardManager.onPlayerDeath();
    }

    public RoleTeam getTeam() {
        return role.getTeam();
    }
}
