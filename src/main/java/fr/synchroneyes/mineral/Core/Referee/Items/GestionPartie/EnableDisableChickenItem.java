package fr.synchroneyes.mineral.Core.Referee.Items.GestionPartie;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Referee.Inventory.InventoryTemplate;
import fr.synchroneyes.mineral.Core.Referee.Items.RefereeItemTemplate;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class EnableDisableChickenItem extends RefereeItemTemplate {

    public EnableDisableChickenItem(Object target, InventoryTemplate inventaireSource) {
        super(target, inventaireSource);
    }

    @Override
    public void performClick(Player joueur) {
        Groupe groupe = mineralcontest.getPlayerGroupe(joueur);
        if (groupe == null) return;

        boolean chickenEnabled = groupe.getGame().getArene().chickenWaves.isEnabled();
        groupe.getGame().getArene().chickenWaves.setEnabled(!chickenEnabled);

        if (chickenEnabled) joueur.sendMessage(mineralcontest.prefixPrive + Lang.chiken_wave_now_disabled.toString());
        else joueur.sendMessage(mineralcontest.prefixPrive + Lang.chiken_wave_now_enabled.toString());
    }

    @Override
    public String getNomItem() {
        return Lang.referee_item_enable_disable_chicken_wave_title.toString();
    }

    @Override
    public String getDescriptionItem() {
        return Lang.referee_item_enable_disable_chicken_wave_description.toString();
    }

    @Override
    public Material getItemMaterial() {
        return Material.ORANGE_CONCRETE;
    }
}
