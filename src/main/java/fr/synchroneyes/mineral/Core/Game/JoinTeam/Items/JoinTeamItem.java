package fr.synchroneyes.mineral.Core.Game.JoinTeam.Items;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.ChatColorString;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class JoinTeamItem extends ItemInterface {

    private Equipe team;

    public JoinTeamItem(Equipe equipe) {
        team = equipe;
    }

    @Override
    public Material getItemMaterial() {
        return Material.valueOf(ChatColorString.toStringEN(team.getCouleur()) + "_CONCRETE");
    }

    @Override
    public String getNomInventaire() {
        return team.getCouleur() + team.getNomEquipe();
    }

    @Override
    public List<String> getDescriptionInventaire() {
        // On ajoute la liste des joueurs présent dans l'équipe
        List<String> list = new ArrayList<>();
        if (team.getJoueurs().isEmpty()) {
            list.add(Lang.currently_no_player_in_this_team.toString());
            return list;
        }

        for (Player joueur : team.getJoueurs())
            list.add("> " + joueur.getDisplayName());

        return list;
    }

    @Override
    public void performClick(Player joueur) {
        Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);
        if (playerGroup == null) {
            joueur.closeInventory();
            return;
        }
        if (playerGroup.getGame() == null) {
            joueur.closeInventory();
            return;
        }

        // Si le joueur possède une équipe, on le retire de son équipe
        Equipe playerTeam = playerGroup.getPlayerTeam(joueur);
        if (playerTeam != null) playerTeam.removePlayer(joueur);

        // On ajoute le joueur dans sa nouvelle équipe

        try {

            this.team.addPlayerToTeam(joueur, true, !playerGroup.getGame().isGameStarted());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
