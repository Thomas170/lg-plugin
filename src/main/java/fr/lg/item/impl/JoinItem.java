package fr.lg.item.impl;

import fr.lg.item.AbstractClickableItem;
import fr.lg.item.ItemType;
import fr.lg.player.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Item pour rejoindre la partie.
 * Permet aux joueurs de s'inscrire et de participer à la partie en cours.
 */
public class JoinItem extends AbstractClickableItem {

    public JoinItem() {
        super(Material.COMPASS, "§a Rejoindre la partie");
    }

    @Override
    protected void setupItemMeta() {
        super.setupItemMeta();
        setLore(
            "§7Cliquez droit pour rejoindre la partie"
        );
    }
    
    @Override
    public void onRightClick(Player player) {
        player.sendMessage("§6Vous avez rejoint la partie");
        PlayerManager.add(player);

        player.getInventory().remove(itemStack);

        ItemStack leaveItem = ItemType.LEAVE_ITEM.getItem().getItemStack();
        player.getInventory().addItem(leaveItem);
    }

    @Override
    public boolean shouldGiveToPlayer(Player player) {
        // Tous les joueurs reçoivent cet item
        return true;
    }
}

