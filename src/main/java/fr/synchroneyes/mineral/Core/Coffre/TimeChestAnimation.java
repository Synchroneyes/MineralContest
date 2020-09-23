package fr.synchroneyes.mineral.Core.Coffre;

import fr.synchroneyes.custom_events.MCAutomatedChestTimeOverEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public abstract class TimeChestAnimation extends AutomatedChestAnimation{


    private int timeLeft;

    @Getter
    private boolean canTimeBeReduced = false;

    /**
     * Constructeur, permet de donner en paramètre le nom de l'inventaire ainsi que la taille
     *
     * @param tailleInventaire - Taille de l'inventaire, doit-être un multiple de 7
     * @param manager
     */
    public TimeChestAnimation(int tailleInventaire, AutomatedChestManager manager) {
        super(tailleInventaire, manager);
        this.timeLeft = getChestAliveTime();
    }

    /**
     * Définir en seconde le temps où un coffre doit rester avant de disparaitre
     * @return
     */
    public abstract int getChestAliveTime();

    /**
     * Définir à quel moment le timer doit démarrer
     * @return
     */
    public abstract TimeChestOpening getTimeTriggerAction();


    public void reduceChestTime() {
        this.timeLeft--;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    /**
     * Méthode appelée lors de la fin du timer de ce coffre
     */
    public void deleteChest() {
        MCAutomatedChestTimeOverEvent event = new MCAutomatedChestTimeOverEvent(this);
        Bukkit.getPluginManager().callEvent(event);
        this.getLocation().getBlock().setType(Material.AIR);
        this.isAnimationOver = true;
        if(this.openingPlayer != null) openingPlayer.closeInventory();
    }

    @Override
    public void spawn() {
        if(getTimeTriggerAction() == TimeChestOpening.ON_SPAWN) this.canTimeBeReduced = true;
        super.spawn();
    }

    @Override
    public void actionToPerformAfterAnimationOver() {
        if(getTimeTriggerAction() == TimeChestOpening.AFTER_OPENING_ANIMATION) this.canTimeBeReduced = true;
    }
}
