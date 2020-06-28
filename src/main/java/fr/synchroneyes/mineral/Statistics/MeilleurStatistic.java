package fr.synchroneyes.mineral.Statistics;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public abstract class MeilleurStatistic extends Statistic {


    protected Map<Player, Integer> infoJoueurs;

    public MeilleurStatistic() {
        this.infoJoueurs = new HashMap<>();
    }


    @Override
    public Player getHighestPlayer() {
        return getMaxPlayer().getKey();
    }


    @Override
    public int getHighestPlayerValue() {
        return getMaxPlayer().getValue();
    }

    @Override
    public boolean isStatUsable() {
        return (!infoJoueurs.isEmpty());
    }

    @Override
    public Player getLowestPlayer() {
        return null;
    }


    @Override
    public String getLowerPlayerTitle() {
        return null;
    }

    @Override
    public String getLowestItemSubTitle() {
        return null;
    }


    @Override
    public Material getLowestPlayerIcon() {
        return null;
    }


    @Override
    public int getLowerPlayerValue() {
        return 0;
    }

    public boolean isLowestValueRequired() {
        return false;
    }


    private Map.Entry<Player, Integer> getMaxPlayer() {
        int max = -1;
        Map.Entry<Player, Integer> meilleur = null;
        for (Map.Entry<Player, Integer> info : infoJoueurs.entrySet()) {
            if (info.getValue() > max) {
                max = info.getValue();
                meilleur = info;
            }
        }

        return meilleur;
    }


}
