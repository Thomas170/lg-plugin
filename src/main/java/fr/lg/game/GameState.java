package fr.lg.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Énumération des états possibles du jeu.
 */
@Getter
@RequiredArgsConstructor
public enum GameState {
    WAITING("En attente de joueurs"),
    PLAYING("Partie en cours");

    private final String displayName;
}

