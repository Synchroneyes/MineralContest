package fr.synchroneyes.mineral.Core.Coffre.Coffres;

import fr.synchroneyes.mineral.Core.Coffre.*;
import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.Core.Player.BaseItem.PlayerBaseItem;
import fr.synchroneyes.mineral.Kits.Classes.Mineur;
import fr.synchroneyes.mineral.Shop.ShopManager;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CoffreMortJoueur extends TimeChestAnimation {

    private Player joueurMort;
    private int tailleInventaire;
    private MCPlayer mcPlayer;

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

        this.mcPlayer = mineralcontest.plugin.getMCPlayer(joueur);

        this.playerInventory = new ArrayList<>();
        for(ItemStack item : joueurMort.getInventory())
            if(item != null && !item.equals(Mineur.getBarrierItem()) && !ShopManager.isAnShopItem(item) && item.getType() != Material.POTION) playerInventory.add(new ItemStack(item.getType(), item.getAmount()));
    }


    @Override
    public int playNoteOnTick() {
        return 0;
    }

    @Override
    public int playNoteOnEnd() {
        return 0;
    }

    @Override
    public void actionToPerformBeforeSpawn() {

    }

    @Override
    public boolean displayWaitingItems() {
        return true;
    }



    @Override
    public String getOpeningChestTitle() {
        return (joueurMort == null) ? "Chargement du titre ..." : Lang.translate(Lang.death_inventory_player_title.toString(), joueurMort);
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
        return Animations.FIVE_LINES_HEART.toList();
    }

    @Override
    public Material getChestMaterial() {
        return Material.ENDER_CHEST;
    }

    @Override
    public int getAnimationTime() {
        return 2;
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

    @Override
    public int getChestAliveTime() {
        MCPlayer player = mineralcontest.plugin.getMCPlayer(joueurMort);
        // temps par défaut
        if(player == null) return 60;
        return player.getGroupe().getParametresPartie().getCVAR("drop_chest_on_death_time").getValeurNumerique();
    }

    @Override
    public TimeChestOpening getTimeTriggerAction() {
        return TimeChestOpening.AFTER_OPENING_ANIMATION;
    }
}
