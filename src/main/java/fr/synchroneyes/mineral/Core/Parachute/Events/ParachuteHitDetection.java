package fr.synchroneyes.mineral.Core.Parachute.Events;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Parachute.Parachute;
import fr.synchroneyes.mineral.Core.Parachute.ParachuteManager;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ParachuteHitDetection implements Listener {

    /**
     * Fonction appelée lors d'une detection d'un tir de fleche qui touche quelque chose
     *
     * @param event
     */
    @EventHandler
    public void onBlockHitByArrow(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            Arrow fleche = (Arrow) event.getEntity();

            // On vérifie si le tireur est un joueur
            if (fleche.getShooter() instanceof Player) {
                Player tireur = (Player) fleche.getShooter();

                // On regarde si le tireur fait parti du plugin
                if (mineralcontest.isInAMineralContestWorld(tireur)) {
                    // On récupère le groupe du joueur
                    Groupe playerGroup = mineralcontest.getPlayerGroupe(tireur);

                    // SI il n'a pas de groupe, on s'en fou et on arrête
                    if (playerGroup == null) return;

                    ParachuteManager parachuteManager = playerGroup.getParachuteManager();

                    if (parachuteManager == null) return;


                    for (Parachute parachute : parachuteManager.getParachutes())
                        if (parachute.isParachuteHit(fleche)) {
                            parachute.receiveDamage(fleche.getDamage());
                            fleche.remove();
                        }
                }
            }
        }
    }
}
