package fr.lg.player;

import fr.lg.roles.RoleTeam;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Gestionnaire centralisé de tous les joueurs d'une partie.
 * Gère l'ajout, le retrait et l'accès aux joueurs.
 * Classe 100% statique - Une seule partie par serveur Minecraft.
 */
@NoArgsConstructor
public class PlayerManager {

    private static final Map<Player, GamePlayer> players = new HashMap<>();

    /**
     * Ajoute un joueur à la partie
     *
     * @param player Le joueur Bukkit à ajouter
     */
    public static void add(Player player) {
        GamePlayer gamePlayer = new GamePlayer(player);
        players.put(player, gamePlayer);
    }

    /**
     * Retire un joueur de la partie
     *
     * @param player Le joueur Bukkit à retirer
     */
    public static void remove(Player player) {
        players.remove(player);
    }

    /**
     * Récupère un GamePlayer par son joueur Bukkit
     *
     * @param player Le joueur Bukkit
     * @return Le GamePlayer, ou null si non trouvé
     */
    public static GamePlayer get(Player player) {
        return players.get(player);
    }

    /**
     * @return Une liste immuable de tous les joueurs
     */
    public static List<GamePlayer> getAll() {
        return new ArrayList<>(players.values());
    }
}

