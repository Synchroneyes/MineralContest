package fr.synchroneyes.mineral.Statistics.Class;

import fr.synchroneyes.mineral.Statistics.MeilleurStatistic;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Cette classe sert à classer les joueurs en fonction du nombre de bloc posé
 */
public class BuilderStat extends MeilleurStatistic {


    @Override
    public void perform(Player p, Object target) {

        if (!infoJoueurs.containsKey(p)) infoJoueurs.put(p, 0);

        int nbBlockPose = infoJoueurs.get(p);
        infoJoueurs.replace(p, nbBlockPose + 1);
    }


    @Override
    public String getHighestPlayerTitle() {
        return Lang.stats_builder_title.getDefault();
    }

    @Override
    public String getHighestItemSubTitle() {
        return Lang.stats_builder_subtitle.toString().replace("%d", getHighestPlayerValue() + "");
    }

    @Override
    public Material getHighestPlayerIcon() {
        return Material.BRICK_WALL;
    }


}
