package fr.lg.item;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe abstraite de base pour les items cliquables.
 */
public abstract class AbstractClickableItem implements ClickableItem {

    protected final ItemStack itemStack;
    protected final String displayName;

    /**
     * Crée un nouvel item.
     *
     * @param material Le matériau de l'item
     * @param displayName Le nom d'affichage (avec codes couleur)
     */
    public AbstractClickableItem(Material material, String displayName) {
        this.itemStack = new ItemStack(material);
        this.displayName = displayName;
        setupItemMeta();
    }

    /**
     * Configure les métadonnées de l'item (nom, lore, etc).
     * Peut être surchargée dans les sous-classes.
     */
    protected void setupItemMeta() {
        itemStack.editMeta(meta -> meta.displayName(Component.text(displayName)));
    }

    /**
     * Ajoute une description (lore) à l'item.
     *
     * @param lore Les lignes de description
     */
    protected void setLore(String... lore) {
        itemStack.editMeta(meta -> {
            List<Component> components = Arrays.stream(lore)
                    .map(Component::text)
                    .collect(Collectors.toList());
            meta.lore(components);
        });
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}

