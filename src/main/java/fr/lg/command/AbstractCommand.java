package fr.lg.command;

import org.bukkit.command.CommandSender;

public class AbstractCommand implements GameCommand {
    @Override
    public String getName() {
        return "Commande non définie";
    }

    @Override
    public String getDescription() {
        return "Description non définie";
    }

    @Override
    public String getUsage() {
        return "Usage non défini";
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        return true;
    }

    @Override
    public boolean isOperatorOnly() {
        return true;
    }
}
