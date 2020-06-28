package fr.synchroneyes.mineral.Statistics.Class;

import fr.synchroneyes.mineral.Statistics.Statistic;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class MinerStat extends Statistic {

    Map<Player, Integer> playerInformation;

    public MinerStat() {
        this.playerInformation = new HashMap<>();
    }

    @Override
    public void perform(Player p, Object target) {
        if (!playerInformation.containsKey(p)) playerInformation.put(p, 0);

        int nombreDeBlockCasse = playerInformation.get(p);
        playerInformation.replace(p, nombreDeBlockCasse + 1);

    }


    @Override
    public Player getHighestPlayer() {
        int max = -1;
        Player maxPlayer = null;
        for (Map.Entry<Player, Integer> infoJoueur : playerInformation.entrySet())
            if (infoJoueur.getValue() > max) {
                max = infoJoueur.getValue();
                maxPlayer = infoJoueur.getKey();
            }
        return maxPlayer;

    }

    @Override
    public Player getLowestPlayer() {
        int max = Integer.MAX_VALUE;
        Player maxPlayer = null;
        for (Map.Entry<Player, Integer> infoJoueur : playerInformation.entrySet())
            if (infoJoueur.getValue() < max) {
                max = infoJoueur.getValue();
                maxPlayer = infoJoueur.getKey();
            }
        return maxPlayer;
    }

    @Override
    public String getHighestPlayerTitle() {
        return Lang.stats_miner_best_ranked_title.toString();
    }

    @Override
    public String getLowerPlayerTitle() {
        return Lang.stats_miner_worst_ranked_title.getDefault();
    }

    @Override
    public String getHighestItemSubTitle() {
        return Lang.stats_miner_subtitle.toString().replace("%d", getHighestPlayerValue() + "");
    }

    @Override
    public String getLowestItemSubTitle() {
        return Lang.stats_miner_subtitle.toString().replace("%d", getLowerPlayerValue() + "");
    }

    @Override
    public Material getHighestPlayerIcon() {
        return Material.DIAMOND_PICKAXE;
    }

    @Override
    public Material getLowestPlayerIcon() {
        return Material.WOODEN_PICKAXE;
    }

    @Override
    public int getHighestPlayerValue() {
        int max = -1;
        Player maxPlayer = null;
        for (Map.Entry<Player, Integer> infoJoueur : playerInformation.entrySet())
            if (infoJoueur.getValue() > max) {
                max = infoJoueur.getValue();
                maxPlayer = infoJoueur.getKey();
            }
        return max;
    }

    @Override
    public int getLowerPlayerValue() {
        int max = Integer.MAX_VALUE;
        Player maxPlayer = null;
        for (Map.Entry<Player, Integer> infoJoueur : playerInformation.entrySet())
            if (infoJoueur.getValue() < max) {
                max = infoJoueur.getValue();
                maxPlayer = infoJoueur.getKey();
            }
        return max;
    }

    @Override
    public boolean isLowestValueRequired() {
        return true;
    }

    @Override
    public boolean isStatUsable() {
        return (!playerInformation.isEmpty());
    }
}
