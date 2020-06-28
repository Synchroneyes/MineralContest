package fr.synchroneyes.mineral.Statistics.Class;

import fr.synchroneyes.mineral.Statistics.MeilleurStatistic;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.Material;
import org.bukkit.entity.Player;


public class ArenaChestStat extends MeilleurStatistic {

    // Tableau representant un joueur avec le nombre de coffre d'arène ouvert

    @Override
    public void perform(Player p, Object target) {
        if (!infoJoueurs.containsKey(p)) infoJoueurs.put(p, 0);

        int nombreDeCoffreOuvertActuellement = infoJoueurs.get(p);
        infoJoueurs.replace(p, nombreDeCoffreOuvertActuellement + 1);
    }


    @Override
    public String getHighestPlayerTitle() {
        return Lang.stats_arena_chest_title.getDefault();
    }

    @Override
    public String getHighestItemSubTitle() {
        return Lang.stats_arena_chest_subtitle.toString().replace("%d", getHighestPlayerValue() + "");
    }

    @Override
    public Material getHighestPlayerIcon() {
        return Material.CHEST;
    }


    /**
     * Cette statistique est utilisable seulement si des coffres ont été ouvert
     *
     * @return
     */
    @Override
    public boolean isStatUsable() {
        return (!infoJoueurs.isEmpty());
    }
}
