package fr.synchroneyes.mineral.Core.Referee.Items.GestionPartie;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Referee.Inventory.InventoryTemplate;
import fr.synchroneyes.mineral.Core.Referee.Items.RefereeItemTemplate;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class StartChickenItem extends RefereeItemTemplate {
    public StartChickenItem(Object target, InventoryTemplate inventaireSource) {
        super(target, inventaireSource);
    }

    @Override
    public void performClick(Player joueur) {
        Groupe groupe = mineralcontest.getPlayerGroupe(joueur);
        if (groupe == null || !groupe.getGame().isGameStarted()) return;

        if (!groupe.getGame().getArene().chickenWaves.isEnabled()) {
            joueur.sendMessage(mineralcontest.prefixPrive + Lang.chicken_wave_error_disabled.toString());
            return;
        }
        groupe.getGame().getArene().chickenWaves.genererProchaineVague();
        groupe.getGame().getArene().chickenWaves.apparitionPoulets();
        groupe.sendToEveryone(mineralcontest.prefixGroupe + Lang.chicken_wave_spawned.toString());
    }

    @Override
    public String getNomItem() {
        return Lang.referee_item_start_chicken_wave_title.toString();
    }

    @Override
    public String getDescriptionItem() {
        return "";
    }

    @Override
    public Material getItemMaterial() {
        return Material.IRON_INGOT;
    }
}
