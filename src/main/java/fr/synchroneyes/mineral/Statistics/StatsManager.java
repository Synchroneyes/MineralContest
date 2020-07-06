package fr.synchroneyes.mineral.Statistics;

import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Statistics.Class.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class StatsManager {

    // Liste de statistiques disponible
    private List<Statistic> availableStats;

    // Partie dans lequel on doit s'occuper des stats
    private Game partie;

    public StatsManager(Game game) {
        this.partie = game;
        this.availableStats = new LinkedList<>();

        registerEvents();
    }

    private void registerEvents() {
        availableStats.add(new KillStat());
        availableStats.add(new ArenaChestStat());
        availableStats.add(new MinerStat());
        availableStats.add(new BuilderStat());
        availableStats.add(new TalkStat());
        availableStats.add(new ChickenKillerStat());
        availableStats.add(new MonsterKillerStat());
        availableStats.add(new MostParachuteHitStat());
    }

    public void register(Class event, Player joueur, Object valeur) {
        for (Statistic stat : availableStats)
            if (stat.getClass().equals(event)) {
                stat.perform(joueur, valeur);
                return;
            }
    }

    public List<ItemStack> getAllStatsAsItemStack() {
        List<ItemStack> items = new LinkedList<>();
        for (Statistic stat : availableStats) {
            Bukkit.getLogger().info("Adding " + stat.getClass().toString() + " items ...");
            items.addAll(stat.toItemStack());
        }
        return items;
    }

}
