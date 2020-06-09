package fr.mineral.Events;

import fr.mineral.Core.Game.Game;
import fr.mineral.Settings.GameSettings;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.ErrorReporting.Error;
import fr.mineral.Utils.Log.GameLogger;
import fr.mineral.Utils.Log.Log;
import fr.mineral.Utils.Range;
import fr.mineral.mineralcontest;
import org.bukkit.Material;
import org.bukkit.entity.Chicken;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class EntityDeathEvent implements Listener {

    @EventHandler
    public void OnEntityDeath(org.bukkit.event.entity.EntityDeathEvent event) {
        if (mineralcontest.isAMineralContestWorld(event.getEntity().getWorld())) {
            Game partie = mineralcontest.getWorldGame(event.getEntity().getWorld());
            if (event.getEntity() instanceof Chicken && partie != null && partie.isGameStarted()) {
                Chicken poulet = (Chicken) event.getEntity();
                // Seulement sur les poulets "custom"
                if (poulet.getCustomName().equalsIgnoreCase(Lang.custom_chicken_name.toString())) {

                    event.getDrops().clear();
                    Range[] items = new Range[4];
                    items[0] = new Range(Material.IRON_INGOT, 0, 50);
                    items[1] = new Range(Material.GOLD_INGOT, 50, 75);
                    items[2] = new Range(Material.DIAMOND, 75, 90);
                    items[3] = new Range(Material.EMERALD, 90, 100);

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

                    } catch (Exception e) {
                        Error.Report(e, partie);
                    }


                }
            }
        }
    }

}
