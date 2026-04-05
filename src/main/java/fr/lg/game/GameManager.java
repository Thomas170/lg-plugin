package fr.lg.game;

import fr.lg.scoreboard.ScoreboardManager;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe centrale représentant une partie de Loup Garou.
 * Gère l'état global de la partie, les joueurs, les rôles, les phases, etc.
 * C'est la classe pivot autour de laquelle tout le reste tourne.
 */
public class GameManager {
    @Setter @Getter
    private static GameState state = GameState.WAITING;
    private static GamePhase phase = GamePhase.DAY;

    public static void initialize() {
        state = GameState.WAITING;
        phase = GamePhase.DAY;
    }

    /**
     * Démarre la partie.
     */
    public static void startGame() {
        state = GameState.PLAYING;
        phase = GamePhase.DAY;
        ScoreboardManager.syncWithGame(state, phase);
    }

    // Au changement de phase (nuit, vote, maire...)
    public static void setPhase(GamePhase newPhase) {
        phase = newPhase;
        ScoreboardManager.syncWithGame(state, phase);
    }
}
