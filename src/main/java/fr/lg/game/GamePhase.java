package fr.lg.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Énumération des différentes phases du jeu.
 */
@Getter
@RequiredArgsConstructor
public enum GamePhase {
    DAY("Jour"),
    NIGHT("Nuit");

    private final String libelle;
}
