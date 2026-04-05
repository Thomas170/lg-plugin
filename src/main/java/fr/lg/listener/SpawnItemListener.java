package fr.lg.listener;

import fr.lg.LGPlugin;
import fr.lg.game.ServerStateManager;
import fr.lg.item.ItemManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;

/**
 * Listener pour donner les items au spawn des joueurs.
 */
public class SpawnItemListener implements Listener {

    /**
     * Donne les items au joueur quand il rejoint le serveur
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        LGPlugin.getInstance().getLogger().info("Joueur connecté: " + player.getName());

        // Donner les items appropriés au joueur
        ItemManager.giveItemsToPlayer(player);

        // Appliquer l'état de jeu si une partie est en cours
        ServerStateManager.applyGameStateToPlayer(player);

        LGPlugin.getInstance().getLogger().info(player.getName() + " - Items distribués");
    }
}

