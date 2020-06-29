package fr.synchroneyes.mineral.Statistics.Class;

import fr.synchroneyes.mineral.Statistics.Statistic;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.Pair;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class KillStat extends Statistic {

    private enum Type {KILL, DEATH}

    ;

    Map<Player, Pair<Integer, Integer>> informationsKills;

    public KillStat() {
        this.informationsKills = new HashMap<>();
    }


    /**
     * Méthode appelée lorsqu'un joueur est tué!
     *
     * @param tueur - La personne ayant tué
     * @param cible - La personne morte
     */
    @Override
    public void perform(Player tueur, Object cible) {
        if (cible instanceof Player) {
            Player victime = (Player) cible;

            // Si la cible est la même que le tueur, ça veut dire suicide!
            if (cible.equals(tueur)) {
                enregister(tueur, Type.DEATH);
                return;
            }

            enregister(tueur, Type.KILL);
            enregister(victime, Type.DEATH);
            return;
        }

        if (cible == null) {
            enregister(tueur, Type.DEATH);
            return;
        }
    }


    /**
     * Retourne le joueur avec le plus de kill
     *
     * @return
     */
    @Override
    public Player getHighestPlayer() {
        int maxKills = Integer.MIN_VALUE;
        Player highest = null;

        for (Map.Entry<Player, Pair<Integer, Integer>> infoJoueur : informationsKills.entrySet()) {
            Pair<Integer, Integer> ratio = infoJoueur.getValue();
            if (ratio.getKey() > maxKills) {
                highest = infoJoueur.getKey();
                maxKills = ratio.getKey();
            }
        }
        return highest;
    }

    @Override
    public Player getLowestPlayer() {
        int maxDeaths = Integer.MIN_VALUE;
        Player lowest = null;

        for (Map.Entry<Player, Pair<Integer, Integer>> infoJoueur : informationsKills.entrySet()) {
            Pair<Integer, Integer> ratio = infoJoueur.getValue();
            if (ratio.getValue() > maxDeaths) {
                lowest = infoJoueur.getKey();
                maxDeaths = ratio.getValue();
            }
        }
        return lowest;
    }

    @Override
    public String getHighestPlayerTitle() {
        return Lang.stats_kill_best_ranked_title.toString();
    }

    @Override
    public String getLowerPlayerTitle() {
        return Lang.stats_kill_worst_ranked_title.toString();
    }

    @Override
    public String getHighestItemSubTitle() {
        return Lang.stats_kill_best_ranked_subtitle.toString().replace("%d", getHighestPlayerValue() + "");
    }

    @Override
    public String getLowestItemSubTitle() {
        return Lang.stats_kill_worst_ranked_subtitle.toString().replace("%d", getLowerPlayerValue() + "");
    }

    @Override
    public Material getHighestPlayerIcon() {
        return Material.DIAMOND_SWORD;
    }

    @Override
    public Material getLowestPlayerIcon() {
        return Material.WOODEN_SWORD;
    }

    /**
     * Retourne le nombre de kill de la personne ayant tué le plus de personne
     *
     * @return nbKill
     */
    @Override
    public int getHighestPlayerValue() {
        Pair<Integer, Integer> ratio = informationsKills.get(getHighestPlayer());
        return ratio.getKey();
    }

    /**
     * Retourne le nombre de mort de la personne étant mort le plus de fois
     *
     * @return nbDeath
     */
    @Override
    public int getLowerPlayerValue() {
        Pair<Integer, Integer> ratio = informationsKills.get(getLowestPlayer());
        return ratio.getValue();
    }

    @Override
    public boolean isLowestValueRequired() {
        return true;
    }

    @Override
    public boolean isStatUsable() {

        // On ne peut pas utiliser cette stat si il n'y a eu aucun kill ou deces
        if (informationsKills.isEmpty()) return false;

        // On ne peut pas utiliser cette stat si il n'y a eu aucun kill
        for (Map.Entry<Player, Pair<Integer, Integer>> infoJoueur : informationsKills.entrySet())
            // Si le joueur a fait au moins 1 kill, c'est qu'il y a eu un mort, donc on peut l'utiliser
            if (infoJoueur.getValue().getKey() > 0) return true;

        return false;
    }

    private void enregister(Player joueur, Type type) {
        Pair<Integer, Integer> ratioActuel;

        if (!informationsKills.containsKey(joueur)) {
            ratioActuel = new Pair<>(0, 0);
            informationsKills.put(joueur, ratioActuel);
        } else {
            ratioActuel = informationsKills.get(joueur);
        }

        if (type == Type.KILL) {
            Pair<Integer, Integer> nouveauRatio = new Pair(ratioActuel.getKey() + 1, ratioActuel.getValue());
            informationsKills.replace(joueur, nouveauRatio);
        } else if (type == Type.DEATH) {
            Pair<Integer, Integer> nouveauRatio = new Pair(ratioActuel.getKey(), ratioActuel.getValue() + 1);
            informationsKills.replace(joueur, nouveauRatio);
        }
    }
}
