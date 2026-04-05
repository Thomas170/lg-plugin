package fr.lg.game;

import org.bukkit.*;
import org.bukkit.entity.Player;

/**
 * Manager pour gérer l'état du serveur lors d'une partie.
 * Configure les game modes, les game rules, la difficulté, etc.
 */
public class ServerStateManager {

    /**
     * Initialise l'état du serveur au chargement du plugin.
     * Configurer le serveur avec les bons paramètres pour les parties de Loup Garou.
     */
    public static void initialize() {
        World world = Bukkit.getWorlds().getFirst();
        if (world == null) {
            Bukkit.getLogger().warning("Aucun monde trouvé !");
            return;
        }

        // Difficulté en Peaceful
        world.setDifficulty(Difficulty.PEACEFUL);

        // Bloquer le cycle jour/nuit (rester au jour)
        world.setGameRule(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setTime(6000); // Mettre à midi (6000 = midi)

        // Bloquer les changements météorologiques
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setStorm(false);
        world.setThundering(false);

        // Activer la régénération naturelle
        world.setGameRule(org.bukkit.GameRule.NATURAL_REGENERATION, true);

        // Empêcher les dégâts
        world.setGameRule(org.bukkit.GameRule.FALL_DAMAGE, false);
        world.setGameRule(org.bukkit.GameRule.FIRE_DAMAGE, false);
        world.setGameRule(org.bukkit.GameRule.DO_ENTITY_DROPS, false);

        // Empêcher le spawn de mobs
        world.setGameRule(org.bukkit.GameRule.DO_MOB_SPAWNING, false);

        Bukkit.getLogger().info("État du serveur initialisé");
    }

    /**
     * Met un joueur en mode Adventure avec les bons paramètres
     *
     * @param player Le joueur
     */
    public static void applyGameStateToPlayer(Player player) {
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
    }
}

