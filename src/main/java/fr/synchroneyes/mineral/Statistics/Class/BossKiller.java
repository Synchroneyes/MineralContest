package fr.synchroneyes.mineral.Statistics.Class;

import fr.synchroneyes.mineral.Statistics.MeilleurStatistic;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BossKiller extends MeilleurStatistic {
    @Override
    public void perform(Player p, Object target) {
        if (!infoJoueurs.containsKey(p)) infoJoueurs.put(p, 0);

        int nbPouletsTue = infoJoueurs.get(p);
        infoJoueurs.replace(p, nbPouletsTue + 1);
    }

    @Override
    public String getHighestPlayerTitle() {
        return "Tueur de boss";
    }

    @Override
    public String getHighestItemSubTitle() {
        return "Avec %d de boss tué";
    }

    @Override
    public Material getHighestPlayerIcon() {
        return Material.SPIDER_EYE;
    }
}
