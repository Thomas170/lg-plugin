package fr.lg.item.impl;

import fr.lg.item.AbstractClickableItem;
import fr.lg.item.ItemType;
import fr.lg.player.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Item pour quitter la partie.
 * Permet aux joueurs de se retirer de la partie en cours.
 */
public class LeaveItem extends AbstractClickableItem {

    public LeaveItem() {
        super(Material.RED_BED, "§a Quitter la partie");
    }

    @Override
    protected void setupItemMeta() {
        super.setupItemMeta();
        setLore(
            "§7Cliquez droit pour quitter la partie"
        );
    }
    
    @Override
    public void onRightClick(Player player) {
        player.sendMessage("§4Vous avez quitté la partie");
        PlayerManager.remove(player);

        player.getInventory().remove(itemStack);

        ItemStack joinItem = ItemType.JOIN_ITEM.getItem().getItemStack();
        player.getInventory().addItem(joinItem);
    }

    @Override
    public boolean shouldGiveToPlayer(Player player) {
        return false;
    }
}

