package fr.synchroneyes.mineral.Core.Referee.Inventory;

import fr.synchroneyes.mineral.Core.Referee.Items.GestionPartie.*;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GestionPartieInventory extends InventoryTemplate {
    @Override
    public void setInventoryItems(Player arbitre) {
        registerItem(new StartGameItem(null, this));
        registerItem(new PauseGameItem(null, this));
        registerItem(new ResumeGameItem(null, this));
        registerItem(new SpawnChestItem(null, this));
        registerItem(new StartChickenItem(null, this));
        registerItem(new EnableDisableChickenItem(null, this));

        registerItem(new AfficherScoreboardToAdminsItem(null, this));
        registerItem(new AfficherScoreboardToEveryoneItem(null, this));
    }

    @Override
    public Material getItemMaterial() {
        return Material.DIAMOND_SWORD;
    }

    @Override
    public String getNomInventaire() {
        return Lang.referee_inventory_game_title.toString();
    }

    @Override
    public String getDescriptionInventaire() {
        return Lang.referee_inventory_game_description.toString();
    }
}
