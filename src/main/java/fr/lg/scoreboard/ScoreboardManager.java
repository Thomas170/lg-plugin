package fr.lg.scoreboard;

import fr.lg.LGPlugin;
import fr.lg.game.GameManager;
import fr.lg.game.GamePhase;
import fr.lg.game.GameState;
import fr.lg.player.GamePlayer;
import fr.lg.player.PlayerManager;
import fr.lg.roles.RoleType;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

import java.util.*;

/**
 * Gère l'affichage du scoreboard (sidebar) en jeu.
 * Affiche :
 * - Le titre dynamique selon l'état de la partie
 * - Les rôles configurés avec leur nombre restant (mis à jour à la mort d'un joueur)
 * - Le nombre de joueurs vivants / total
 * Usage :
 *   ScoreboardManager.initialize(plugin);
 *   ScoreboardManager.updateAll();           // Rafraîchit tous les scoreboards
 *   ScoreboardManager.onPlayerDeath(player); // Appelé lors de la mort d'un joueur
 *   ScoreboardManager.setPhase(GamePhase.NIGHT); // Change la phase affichée
 */
public class ScoreboardManager {

    // -----------------------------------------------------------------------
    // Phase de jeu étendue (pour le titre du scoreboard)
    // -----------------------------------------------------------------------

    @Getter
    public enum DisplayPhase {
        WAITING("§e§lEn attente"),
        DAY("§6§lEn jeu §7(§eJour§7)"),
        NIGHT("§9§lEn jeu §7(§1Nuit§7)"),
        VOTE("§c§lEn jeu §7(§cVote§7)"),
        MAYOR("§6§lEn jeu §7(§6Maire§7)");

        private final String title;

        DisplayPhase(String title) {
            this.title = title;
        }

    }

    // -----------------------------------------------------------------------
    // État interne
    // -----------------------------------------------------------------------

    private static DisplayPhase currentPhase = DisplayPhase.WAITING;
    private static BukkitTask refreshTask = null;

    // Couleurs par équipe (RoleTeam) — ajouter un nouveau rôle ne nécessite pas de modifier ce fichier
    private static final Map<fr.lg.roles.RoleTeam, String> TEAM_COLORS = new EnumMap<>(fr.lg.roles.RoleTeam.class);

    static {
        TEAM_COLORS.put(fr.lg.roles.RoleTeam.VILLAGER, "§f");  // blanc
        TEAM_COLORS.put(fr.lg.roles.RoleTeam.WEREWOLF, "§4");  // rouge foncé
        TEAM_COLORS.put(fr.lg.roles.RoleTeam.ALONE, "§5");  // violet
        TEAM_COLORS.put(fr.lg.roles.RoleTeam.INCONNU, "§7");  // gris
    }

    // -----------------------------------------------------------------------
    // Initialisation
    // -----------------------------------------------------------------------

    /**
     * Initialise le ScoreboardManager et lance le rafraîchissement automatique.
     *
     * @param plugin L'instance du plugin
     */
    public static void initialize(LGPlugin plugin) {
        // Rafraîchissement toutes les 40 ticks (2 secondes) — léger
        refreshTask = Bukkit.getScheduler().runTaskTimer(plugin, ScoreboardManager::updateAll, 20L, 40L);
        plugin.getLogger().info("ScoreboardManager initialisé.");
    }

    /**
     * Arrête le rafraîchissement automatique (à appeler dans onDisable).
     */
    public static void shutdown() {
        if (refreshTask != null) {
            refreshTask.cancel();
            refreshTask = null;
        }
    }

    // -----------------------------------------------------------------------
    // API publique
    // -----------------------------------------------------------------------

    /**
     * Change la phase affichée dans le titre du scoreboard.
     *
     * @param phase La nouvelle phase
     */
    public static void setPhase(DisplayPhase phase) {
        currentPhase = phase;
        updateAll();
    }

    /**
     * Synchronise la phase du scoreboard avec l'état/phase interne du jeu.
     * À appeler lors d'une transition d'état ou de phase.
     *
     * @param state L'état actuel du jeu
     * @param phase La phase actuelle du jeu (DAY/NIGHT)
     */
    public static void syncWithGame(GameState state, GamePhase phase) {
        if (state == GameState.WAITING) {
            setPhase(DisplayPhase.WAITING);
        } else {
            setPhase(phase == GamePhase.NIGHT ? DisplayPhase.NIGHT : DisplayPhase.DAY);
        }
    }

    /**
     * Doit être appelé lorsqu'un joueur meurt en jeu.
     * Met à jour tous les scoreboards pour retirer son rôle du comptage.
     */
    public static void onPlayerDeath() {
        updateAll();
    }

    /**
     * Rafraîchit le scoreboard de tous les joueurs connectés.
     */
    public static void updateAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updatePlayer(player);
        }
    }

    /**
     * Rafraîchit le scoreboard d'un joueur spécifique.
     *
     * @param player Le joueur à mettre à jour
     */
    public static void updatePlayer(Player player) {
        org.bukkit.scoreboard.ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();

        // --- Titre ---
        Objective objective = board.registerNewObjective(
                "lg_sidebar",
                Criteria.DUMMY,
                Component.text(currentPhase.getTitle())
        );
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // --- Construction des lignes ---
        List<String> lines = buildLines();

        // Bukkit Scoreboard : le score détermine l'ordre (décroissant).
        // IMPORTANT : l'entrée (clé du score) est affichée telle quelle dans la sidebar.
        // On utilise donc des codes couleur vides ("§0", "§1"...) comme clés invisibles,
        // et on met le vrai texte uniquement dans le prefix de la team.
        int score = lines.size();
        int index = 0;
        for (String line : lines) {
            // Clé unique et invisible : juste un code couleur (§0 à §f puis combinaisons)
            String key = colorKey(index++);
            Team team = board.registerNewTeam("line_" + score);
            team.addEntry(key);
            // Le prefix porte le vrai contenu, la clé reste vide visuellement
            team.prefix(Component.text(line));
            team.suffix(Component.empty());
            objective.getScore(key).setScore(score);
            score--;
        }

        player.setScoreboard(board);
    }

    // -----------------------------------------------------------------------
    // Construction des lignes
    // -----------------------------------------------------------------------

    private static List<String> buildLines() {
        List<String> lines = new ArrayList<>();

        // Séparateur haut
        lines.add("§8§m──────────────");

        // --- Joueurs vivants ---
        List<GamePlayer> allPlayers = PlayerManager.getAll();
        long alive = allPlayers.stream().filter(GamePlayer::isAlive).count();
        int total = allPlayers.size();

        if (total > 0) {
            String totalValue = GameManager.getState() == GameState.WAITING ? "" : "/§f" + total;
            lines.add("§fJoueurs: §a" + alive + totalValue);
        } else {
            lines.add("§7Aucun joueur inscrit");
        }

        lines.add("§8§m──────────────");

        // --- Rôles en jeu ---
        if (GameManager.getState() == GameState.WAITING) {
            lines.addAll(buildRolesConfigured());
        } else {
            lines.addAll(buildRolesAlive());
        }

        // Séparateur bas
        lines.add("§8§m──────────────");

        return lines;
    }

    /**
     * En attente : affiche les rôles configurés (depuis RoleManager).
     */
    private static List<String> buildRolesConfigured() {
        List<String> lines = new ArrayList<>();
        Map<RoleType, Integer> rolesInGame = fr.lg.roles.RoleManager.getRolesInGame();

        if (rolesInGame.isEmpty()) {
            lines.add("§7Aucun rôle configuré");
            return lines;
        }

        for (Map.Entry<RoleType, Integer> entry : rolesInGame.entrySet()) {
            RoleType type = entry.getKey();
            int count = entry.getValue();
            if (type == RoleType.INCONNU) continue;

            String color = TEAM_COLORS.getOrDefault(type.getRole() != null ? type.getRole().getTeam() : fr.lg.roles.RoleTeam.INCONNU, "§7");
            String name = type.getRole() != null ? type.getRole().getName() : type.name();
            lines.add(color + name + " §8x" + count);
        }

        return lines;
    }

    /**
     * En jeu : affiche uniquement les rôles encore en vie.
     * Le nombre reflète les joueurs vivants portant ce rôle.
     */
    private static List<String> buildRolesAlive() {
        List<String> lines = new ArrayList<>();

        // Compter les joueurs vivants par rôle
        Map<RoleType, Integer> aliveByRole = new LinkedHashMap<>();
        for (GamePlayer gp : PlayerManager.getAll()) {
            if (!gp.isAlive()) continue;
            if (gp.getRole() == null) continue;
            RoleType type = gp.getRole().getType();
            if (type == RoleType.INCONNU) continue;
            aliveByRole.merge(type, 1, Integer::sum);
        }

        if (aliveByRole.isEmpty()) {
            lines.add("§7Aucun joueur en vie");
            return lines;
        }

        for (Map.Entry<RoleType, Integer> entry : aliveByRole.entrySet()) {
            RoleType type = entry.getKey();
            int count = entry.getValue();
            String color = TEAM_COLORS.getOrDefault(type.getRole() != null ? type.getRole().getTeam() : fr.lg.roles.RoleTeam.INCONNU, "§7");
            String name = type.getRole() != null ? type.getRole().getName() : type.name();
            lines.add(color + name + " §8x" + count);
        }

        return lines;
    }

    // -----------------------------------------------------------------------
    // Utilitaires
    // -----------------------------------------------------------------------

    /**
     * Génère une clé unique et visuellement vide pour une ligne de scoreboard.
     * On utilise des combinaisons de codes couleur §0-§f : chaque index donne
     * une chaîne distincte qui s'affiche comme vide dans la sidebar.
     */
    private static final String[] COLOR_CODES = {
            "§0","§1","§2","§3","§4","§5","§6","§7",
            "§8","§9","§a","§b","§c","§d","§e","§f"
    };

    private static String colorKey(int index) {
        if (index < COLOR_CODES.length) {
            return COLOR_CODES[index];
        }
        int hi = index / COLOR_CODES.length;
        int lo = index % COLOR_CODES.length;
        return COLOR_CODES[hi] + COLOR_CODES[lo];
    }
}