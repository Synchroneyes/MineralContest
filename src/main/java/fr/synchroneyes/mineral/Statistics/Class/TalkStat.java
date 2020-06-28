package fr.synchroneyes.mineral.Statistics.Class;

import fr.synchroneyes.mineral.Statistics.MeilleurStatistic;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.Material;
import org.bukkit.entity.Player;


/**
 * Cette classe classe les joueurs en fonction du nombre de message envoyé
 */
public class TalkStat extends MeilleurStatistic {


    @Override
    public void perform(Player p, Object target) {
        if (!infoJoueurs.containsKey(p)) infoJoueurs.put(p, 0);

        int nbMessageEnvoye = infoJoueurs.get(p);

        infoJoueurs.replace(p, nbMessageEnvoye + 1);
    }

    @Override
    public String getHighestPlayerTitle() {
        return Lang.stats_most_talking_title.toString();
    }

    @Override
    public String getHighestItemSubTitle() {
        return Lang.stats_most_talking_subtitle.toString().replace("%d", getHighestPlayerValue() + "");
    }

    @Override
    public Material getHighestPlayerIcon() {
        return Material.WRITABLE_BOOK;
    }

}
