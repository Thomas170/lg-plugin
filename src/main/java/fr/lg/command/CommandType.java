package fr.lg.command;

import fr.lg.command.impl.RoleDescriptionCommand;
import fr.lg.command.impl.StartGameCommand;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Énumération des types de commandes disponibles dans le jeu.
 */
@Getter
@RequiredArgsConstructor
public enum CommandType {
    START_GAME(new StartGameCommand()),
    ROLE_DESCRIPTION(new RoleDescriptionCommand()),
    INCONNU(null);

    private final GameCommand command;
}
