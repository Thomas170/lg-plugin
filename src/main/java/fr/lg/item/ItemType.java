package fr.lg.item;

import fr.lg.item.impl.AdminItem;
import fr.lg.item.impl.JoinItem;
import fr.lg.item.impl.LeaveItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Énumération des types d'items cliquables disponibles dans le jeu.
 * Similaire à CommandType, chaque item est instantié ici.
 */
@Getter
@RequiredArgsConstructor
public enum ItemType {
    ADMIN_ITEM(new AdminItem()),
    JOIN_ITEM(new JoinItem()),
    LEAVE_ITEM(new LeaveItem());

    private final ClickableItem item;

    /**
     * Trouve l'ItemType correspondant à un nom d'affichage.
     *
     * @param displayName Le nom d'affichage de l'item
     * @return L'ItemType trouvé, ou null si non trouvé
     */
    public static ItemType getByDisplayName(String displayName) {
        for (ItemType itemType : ItemType.values()) {
            if (itemType.item.getDisplayName().equals(displayName)) {
                return itemType;
            }
        }
        return null;
    }
}


