package fr.synchroneyes.mineral.Shop.Items.Equipe;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Shop.Items.Abstract.ConsumableItem;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Material;

public class TeleportEquipeAreneAuto extends ConsumableItem {
    @Override
    public String getNomItem() {
        return "Auto /arene";
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{"Ce bonus permet de téléporter toute l'équipe à l'arène", "automatiquement dès que le coffre apparait"};
    }

    @Override
    public Material getItemMaterial() {
        return Material.ENDER_EYE;
    }

    @Override
    public boolean isEnabledOnRespawn() {
        return false;
    }

    @Override
    public boolean isEnabledOnPurchase() {
        return true;
    }

    @Override
    public int getNombreUtilisations() {
        return 1;
    }

    @Override
    public void onItemUse() {
        Groupe playerGroup = mineralcontest.getPlayerGroupe(this.joueur);

        if (playerGroup == null) return;

        Equipe playerTeam = playerGroup.getPlayerTeam(this.joueur);
        if (playerTeam == null) return;

        playerGroup.getGame().getArene().addTeamToAutomatedTeleport(playerTeam);
    }

    @Override
    public int getPrice() {
        return 200;
    }

}
