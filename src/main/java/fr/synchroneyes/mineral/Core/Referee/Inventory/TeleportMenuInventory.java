package fr.synchroneyes.mineral.Core.Referee.Inventory;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.House;
import fr.synchroneyes.mineral.Core.Referee.Items.TeleportToHouseItem;
import fr.synchroneyes.mineral.Core.Referee.Items.TeleportToPlayerItem;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.ChatColorString;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TeleportMenuInventory extends InventoryTemplate {


    /**
     * Remplissable de l'inventaire avec les items
     */
    @Override
    public void setInventoryItems(Player arbitre) {


        LinkedList<TeleportToHouseItem> maisons = new LinkedList<>();

        Groupe playerGroupe = mineralcontest.getPlayerGroupe(arbitre);

        for (House maison : playerGroupe.getGame().getHouses()) {
            for (Player joueur : maison.getTeam().getJoueurs()) {
                String nomItem = ChatColorString.toStringEN(maison.getTeam().getCouleur()) + "_CONCRETE";


                Material materialItem = null;
                try {
                    materialItem = Material.valueOf(nomItem);
                } catch (IllegalArgumentException iae) {
                    materialItem = Material.WHITE_WOOL;
                }

                ItemStack itemJoueur = new ItemStack(materialItem, 1);
                ItemMeta itemMeta = itemJoueur.getItemMeta();
                List<String> description = new ArrayList<>();
                description.add(Lang.referee_item_teleport_to_player_description.toString() + maison.getTeam().getCouleur() + joueur.getDisplayName());
                itemMeta.setLore(description);
                itemMeta.setDisplayName(Lang.referee_item_teleport_to_player_title.toString() + joueur.getDisplayName());

                itemJoueur.setItemMeta(itemMeta);

                registerItem(new TeleportToPlayerItem(joueur, this));
            }
            if (!maison.getTeam().getJoueurs().isEmpty()) maisons.add(new TeleportToHouseItem(maison.getTeam(), this));
        }

        addSpaces(2);
        for (TeleportToHouseItem maison : maisons) registerItem(maison);
    }

    @Override
    public Material getItemMaterial() {
        return Material.ENDER_EYE;
    }


    @Override
    public String getNomInventaire() {
        return Lang.referee_item_teleport_inventory_title.toString();
    }

    @Override
    public String getDescriptionInventaire() {
        return Lang.referee_inventory_teleport_description.toString();
    }
}
