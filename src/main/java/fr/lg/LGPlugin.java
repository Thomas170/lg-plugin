package fr.lg;

import fr.lg.command.CommandManager;
import fr.lg.command.CommandType;
import fr.lg.game.GameManager;
import fr.lg.game.ServerStateManager;
import fr.lg.gui.MenuManager;
import fr.lg.listener.ItemClickListener;
import fr.lg.listener.SpawnItemListener;
import fr.lg.roles.RoleType;
import fr.lg.scoreboard.ScoreboardManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public class LGPlugin extends JavaPlugin {

    @Getter
    private static LGPlugin instance;


    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Plugin Loup Garou activé !");

        // Initialiser les managers
        ServerStateManager.initialize();
        GameManager.initialize();
        MenuManager.initialize(this);
        ScoreboardManager.initialize(this);

        getLogger().info(RoleType.values().length + " rôles disponibles");
        getLogger().info(CommandType.values().length + " commandes enregistrées");

        // Enregistrer les listeners
        Bukkit.getPluginManager().registerEvents(new SpawnItemListener(), this);
        Bukkit.getPluginManager().registerEvents(new ItemClickListener(), this);

        // Enregistrer le gestionnaire de commande Bukkit
        Objects.requireNonNull(getCommand("lg")).setExecutor(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin Loup Garou désactivé !");
        ScoreboardManager.shutdown();
    }

    /**
     * Gestionnaire de la commande /lg
     */
    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {
        return CommandManager.executeCommand(sender, args);
    }
}