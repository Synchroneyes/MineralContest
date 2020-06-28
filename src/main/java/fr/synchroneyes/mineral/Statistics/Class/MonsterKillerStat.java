package fr.synchroneyes.mineral.Statistics.Class;

import fr.synchroneyes.mineral.Statistics.MeilleurStatistic;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MonsterKillerStat extends MeilleurStatistic {
    @Override
    public void perform(Player p, Object target) {
        if (!infoJoueurs.containsKey(p)) infoJoueurs.put(p, 0);

        int nbPouletsTue = infoJoueurs.get(p);
        infoJoueurs.replace(p, nbPouletsTue + 1);
    }

    @Override
    public String getHighestPlayerTitle() {
        return Lang.stats_monster_killer_title.toString();
    }

    @Override
    public String getHighestItemSubTitle() {
        return Lang.stats_monster_killer_subtitle.toString().replace("%d", getHighestPlayerValue() + "");
    }

    @Override
    public Material getHighestPlayerIcon() {
        return Material.ZOMBIE_HEAD;
    }
}
