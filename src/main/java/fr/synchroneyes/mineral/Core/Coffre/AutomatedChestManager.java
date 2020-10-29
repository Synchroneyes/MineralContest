package fr.synchroneyes.mineral.Core.Coffre;

import fr.synchroneyes.custom_events.MCAutomatedChestTimeOverEvent;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Coffre.Coffres.CoffreArene;
import fr.synchroneyes.mineral.Core.Coffre.Coffres.CoffreParachute;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class AutomatedChestManager implements Listener {

    private List<AutomatedChestAnimation> coffresAvecAnimation;
    private Queue<TimeChestAnimation> coffreAvecDureeDeVie;
    private Groupe groupe;

    private BukkitTask chestTimedLoop;

    public AutomatedChestManager(Groupe groupe) {
        this.coffresAvecAnimation = new LinkedList<>();
        this.coffreAvecDureeDeVie = new LinkedBlockingQueue<>();
        this.groupe = groupe;

        registerCoffres();

        Bukkit.getPluginManager().registerEvents(this, mineralcontest.plugin);
    }


    public Groupe getGroupe() {
        return groupe;
    }

    private void registerCoffres() {
        this.coffresAvecAnimation.add(new CoffreParachute(this));
        this.coffresAvecAnimation.add(new CoffreArene(this, null));
    }

    /**
     * Permet d'ajouter un nouveau coffre
     * @param chestAnimation
     */
    public void addChest(AutomatedChestAnimation chestAnimation) {
        if(!this.coffresAvecAnimation.contains(chestAnimation)) this.coffresAvecAnimation.add(chestAnimation);
    }

    /**
     * Permet d'ajouter un nouveau coffre avec durée de vie
     * @param chestAnimation
     */
    public void addTimedChest(TimeChestAnimation chestAnimation) {

        this.coffreAvecDureeDeVie.add(chestAnimation);
        if(chestTimedLoop == null) startChestTimerLoop();

    }


    public AutomatedChestAnimation getFromInventory(Inventory v) {
        for (AutomatedChestAnimation automatedChestAnimation : coffresAvecAnimation)
            if (automatedChestAnimation.getInventory().equals(v)) return automatedChestAnimation;
        return null;
    }

    public void replace(Class c, AutomatedChestAnimation automatedChestAnimation) {
        for (AutomatedChestAnimation automatedChestAnimation1 : coffresAvecAnimation) {
            if (automatedChestAnimation1.getClass().equals(c)) {
                coffresAvecAnimation.remove(automatedChestAnimation1);
                break;
            }
        }

        coffresAvecAnimation.add(automatedChestAnimation);
    }

    public boolean isThisAnAnimatedInventory(Inventory i) {
        for (AutomatedChestAnimation chestAnimation : coffresAvecAnimation)
            if (chestAnimation.getInventory().equals(i)) return true;
        return false;
    }

    public AutomatedChestAnimation getChestAnomation(Block b) {


        for (AutomatedChestAnimation automatedChest : coffresAvecAnimation) {
            if (automatedChest.getLocation() == null) continue;
            if (automatedChest.getLocation().equals(b.getLocation())) return automatedChest;
        }

        for (AutomatedChestAnimation timedChest : coffreAvecDureeDeVie) {
            if (timedChest.getLocation() == null) continue;
            if (timedChest.getLocation().equals(b.getLocation())) return timedChest;
        }
        return null;
    }

    public boolean isThisBlockAChestAnimation(Block b) {
        for (AutomatedChestAnimation automatedChest : coffresAvecAnimation) {
            //Bukkit.getLogger().info(automatedChest + " - " + automatedChest.getLocation() + " - " + automatedChest.getChestMaterial());
            if (automatedChest.getLocation() != null && automatedChest.getLocation().equals(b.getLocation()))
                return true;
        }

        for(TimeChestAnimation timedChest : coffreAvecDureeDeVie) {
            if(timedChest.getLocation() != null && timedChest.getLocation().equals(b.getLocation())) return true;
        }
        return false;
    }


    /**
     * Méthode appelée lorsqu'un coffre doit disparaitre
     * @param event
     */
    @EventHandler
    public void onTimedChestEnd(MCAutomatedChestTimeOverEvent event) {
        TimeChestAnimation coffre = (TimeChestAnimation) event.getAutomatedChest();
        coffreAvecDureeDeVie.remove(coffre);

        if(coffreAvecDureeDeVie.isEmpty()) endChestTimerLoop();
    }

    private void endChestTimerLoop() {
        if(chestTimedLoop != null) {
            chestTimedLoop.cancel();
            chestTimedLoop = null;
        }
    }

    private void startChestTimerLoop() {
        if(chestTimedLoop != null) endChestTimerLoop();
        chestTimedLoop = Bukkit.getScheduler().runTaskTimer(mineralcontest.plugin, this::doTimedChestTick, 0, 20);
    }

    private void doTimedChestTick() {

        // Si la liste est vide, on arrête le timer
        if(coffresAvecAnimation.isEmpty()) {
            endChestTimerLoop(); return;
        }

        // On traite chaque coffre
        for(TimeChestAnimation coffre : coffreAvecDureeDeVie) {

            // Si le temps ne peut pas diminuer, on passe au coffre suivant
            if(!coffre.isCanTimeBeReduced()) {
                continue;
            }

            // Si le temps n'est pas encore terminé
            if(coffre.getTimeLeft() > 0) {
                coffre.reduceChestTime();
                continue;
            }

            // Le temps est terminé
            coffre.deleteChest();
        }


    }


}
