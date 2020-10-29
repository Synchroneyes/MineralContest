package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Settings.GameSettings;
import fr.synchroneyes.mineral.Statistics.Class.ChickenKillerStat;
import fr.synchroneyes.mineral.Statistics.Class.MonsterKillerStat;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.ErrorReporting.Error;
import fr.synchroneyes.mineral.Utils.Log.GameLogger;
import fr.synchroneyes.mineral.Utils.Log.Log;
import fr.synchroneyes.mineral.Utils.Range;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Material;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class EntityDeathEvent implements Listener {

    @EventHandler
    public void OnEntityDeath(org.bukkit.event.entity.EntityDeathEvent event) {
        if (mineralcontest.isAMineralContestWorld(event.getEntity().getWorld())) {
            Game partie = mineralcontest.getWorldGame(event.getEntity().getWorld());
            if (event.getEntity() instanceof LivingEntity && partie != null && partie.isGameStarted()) {


                if(partie.getArene().chickenWaves.isFromChickenWave(event.getEntity())) {
                    LivingEntity poulet = (LivingEntity) event.getEntity();
                    Player tueur = poulet.getKiller();
                    // Seulement sur les poulets "custom"

                    if (poulet.getCustomName() == null) return;
                    if (poulet.getCustomName().equalsIgnoreCase(Lang.custom_chicken_name.toString())) {

                        event.getDrops().clear();
                        Range[] items = new Range[4];
                        items[0] = new Range(Material.IRON_INGOT, 0, 75);
                        items[1] = new Range(Material.GOLD_INGOT, 75, 95);
                        items[2] = new Range(Material.DIAMOND, 95, 98);
                        items[3] = new Range(Material.EMERALD, 98, 100);

                        GameSettings settings = partie.groupe.getParametresPartie();

                        try {
                            int min = settings.getCVAR("chicken_spawn_min_item_count").getValeurNumerique();// get game
                            int max = settings.getCVAR("chicken_spawn_max_item_count").getValeurNumerique();// get game
                            Random random = new Random();
                            int nombre = (random.nextInt((max - min) - 1) + min);

                            for (int i = 0; i < nombre; ++i) {
                                Material droppedItem = Range.getInsideRange(items, random.nextInt(100));
                                GameLogger.addLog(new Log("chicken_drop", "Chicken dropped 1x " + droppedItem.toString() + "", "chicken_killed"));
                                event.getDrops().add(new ItemStack(droppedItem, 1));
                            }

                            if (tueur != null) partie.getStatsManager().register(ChickenKillerStat.class, tueur, null);

                        } catch (Exception e) {
                            Error.Report(e, partie);
                        }


                    }
                }

            }

            if (event.getEntity() instanceof Monster && partie != null && partie.isGameStarted()) {
                Player tueur = event.getEntity().getKiller();
                if (tueur != null) {
                    partie.getStatsManager().register(MonsterKillerStat.class, tueur, null);
                }
            }
        }
    }

}
