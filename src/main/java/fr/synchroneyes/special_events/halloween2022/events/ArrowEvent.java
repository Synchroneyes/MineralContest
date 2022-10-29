package fr.synchroneyes.special_events.halloween2022.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Pillager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class ArrowEvent implements Listener {


    @EventHandler
    public void onPlayerHitByArrow(EntityDamageByEntityEvent event){


        if(event.getDamager() instanceof Arrow) {
            Arrow fleche = (Arrow) event.getDamager();
            if(fleche.getShooter() instanceof Monster){
                Monster monstre = (Monster) fleche.getShooter();

                // vérification si les dégats viennent d'un boss
                if(monstre.hasMetadata("isBoss")){
                    List<MetadataValue> values = monstre.getMetadata("BossDamage");
                    if(values.isEmpty()) return;

                    int valeurDegat = values.get(0).asInt();
                    event.setDamage(valeurDegat);
                }
            }

        }


    }

}


