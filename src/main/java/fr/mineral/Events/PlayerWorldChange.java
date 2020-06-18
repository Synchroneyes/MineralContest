package fr.mineral.Events;

import fr.groups.Core.Groupe;
import fr.mineral.Scoreboard.ScoreboardUtil;
import fr.mineral.mineralcontest;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerWorldChange implements Listener {
    @EventHandler
    public void onPlayerchangeWorld(PlayerChangedWorldEvent event) {


        if (mineralcontest.isAMineralContestWorld(event.getFrom())) {
            ScoreboardUtil.unrankedSidebarDisplay(event.getPlayer(), "");
        }


        /**
         * Si un joueur change de monde, et qu'il rejoint le monde avec le hub,
         * et que l'on est dans la version "non" communautaire, c'est-à-dire un serveur
         * "lambda", alors on l'ajoute au groupe par défaut
         */
        Player joueur = event.getPlayer();
        if(mineralcontest.isInMineralContestHub(joueur) && !mineralcontest.communityVersion) {
            Groupe groupeParDefaut = mineralcontest.plugin.getNonCommunityGroup();
            if(groupeParDefaut == null) return;
            if(joueur.isOp()) groupeParDefaut.addAdmin(joueur);
            else groupeParDefaut.addJoueur(joueur);

            joueur.teleport(mineralcontest.plugin.defaultSpawn);
        }

    }
}
