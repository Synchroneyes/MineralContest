package fr.synchroneyes.mineral.Core.Coffre.Coffres;

import fr.synchroneyes.mineral.Core.Coffre.Animations;
import fr.synchroneyes.mineral.Core.Coffre.AutomatedChestAnimation;
import fr.synchroneyes.mineral.Core.Coffre.AutomatedChestManager;
import fr.synchroneyes.mineral.Kits.Classes.Mineur;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CoffreMortJoueur extends AutomatedChestAnimation {

    private Player joueurMort;
    private int tailleInventaire;

    private List<ItemStack> playerInventory;

    /**
     * Constructeur, permet de donner en paramètre le nom de l'inventaire ainsi que la taille
     *
     * @param tailleInventaire - Taille de l'inventaire, doit-être un multiple de 7
     * @param manager
     */
    public CoffreMortJoueur(int tailleInventaire, AutomatedChestManager manager, Player joueur) {
        super(tailleInventaire, manager);
        this.joueurMort = joueur;
        this.tailleInventaire = tailleInventaire;

        this.playerInventory = new ArrayList<>();
        for(ItemStack item : joueurMort.getInventory())
            if(item != null && !item.equals(Mineur.getBarrierItem())) playerInventory.add(new ItemStack(item.getType(), item.getAmount()));
    }

    @Override
    public void actionToPerformBeforeSpawn() {
        //this.inventaireCoffre = Bukkit.createInventory(null, tailleInventaire, "Inventaire de " + joueurMort.getDisplayName());
    }

    @Override
    public void actionToPerformAfterAnimationOver() {

    }

    @Override
    public boolean displayWaitingItems() {
        return true;
    }

    @Override
    public String getOpeningChestTitle() {
        return (joueurMort == null) ? "Chargement du titre ..." : "Inventaire de " + joueurMort.getDisplayName();
    }

    @Override
    public String getOpenedChestTitle() {
        return getOpeningChestTitle();
    }

    @Override
    public ItemStack getWaitingItemMaterial() {
        return new ItemStack(Material.RED_STAINED_GLASS_PANE);
    }

    @Override
    public ItemStack getUsedItemMaterial() {
        return new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
    }

    @Override
    public LinkedList<Integer> getOpeningSequence() {
        return Animations.FIVE_LINES_UP_TO_DOWN_LEFT_TO_RIGHT.toList();
    }

    @Override
    public Material getChestMaterial() {
        return Material.ENDER_CHEST;
    }

    @Override
    public int getAnimationTime() {
        return 0;
    }

    @Override
    public boolean canChestBeOpenedByMultiplePlayers() {
        return false;
    }

    @Override
    public List<ItemStack> genererContenuCoffre() {
        return this.playerInventory;
    }

    @Override
    public boolean automaticallyGiveItemsToPlayer() {
        return false;
    }
}
