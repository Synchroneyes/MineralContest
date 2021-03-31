package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;


/**
 * Permet d'activer ou non la faim dans le plugin
 */
public class PlayerFoodLevelEvent implements Listener {

    @EventHandler
    public void onPlayerHunger(FoodLevelChangeEvent event){
        if(event.getEntity() instanceof Player) {
            Player joueur = (Player) event.getEntity();
            if(mineralcontest.isInAMineralContestWorld(joueur)){
                MCPlayer mcPlayer = mineralcontest.plugin.getMCPlayer(joueur);

                if(mcPlayer.getGroupe() == null) return;

                if(mcPlayer.getGroupe().getParametresPartie().getCVAR("enable_hunger").getValeurNumerique() != 1) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}
