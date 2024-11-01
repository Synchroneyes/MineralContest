package fr.synchroneyes.special_events.halloween2024.chests;

import fr.synchroneyes.mineral.Core.Coffre.Animations;
import fr.synchroneyes.mineral.Core.Coffre.AutomatedChestAnimation;
import fr.synchroneyes.mineral.Core.Coffre.AutomatedChestManager;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.mineralcontest;
import fr.synchroneyes.special_events.halloween2024.utils.HalloweenTitle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.LinkedList;
import java.util.List;

public class ParkourChest extends AutomatedChestAnimation {

    private Game game;

    public ParkourChest(int tailleInventaire, AutomatedChestManager manager) {
        super(tailleInventaire, manager);
        this.game = manager.getGroupe().getGame();
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
    public void actionToPerformAfterAnimationOver() {
        MCPlayer mcPlayer = mineralcontest.plugin.getMCPlayer(getPlayerOpenedChest());
        mcPlayer.getEquipe().ajouterPoints(1500);

        getPlayerOpenedChest().sendMessage(mineralcontest.prefixPrive + "Vous avez gagné 1500 points pour avoir ouvert ce coffre !");
    }

    @Override
    public boolean displayWaitingItems() {
        return true;
    }

    @Override
    public String getOpeningChestTitle() {
        return "Coffre de Parkour";
    }

    @Override
    public String getOpenedChestTitle() {
        return "Coffre de Parkour";
    }

    @Override
    public ItemStack getWaitingItemMaterial() {
        ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("");
        item.setItemMeta(itemMeta);

        return item;
    }

    @Override
    public ItemStack getUsedItemMaterial() {
        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("");
        item.setItemMeta(itemMeta);

        return item;
    }

    @Override
    public LinkedList<Integer> getOpeningSequence() {
        return Animations.SIX_LINES_PUMPKINS.toList();
    }

    @Override
    public Material getChestMaterial() {
        return Material.CHEST;
    }

    @Override
    public int getAnimationTime() {
        return 5;
    }

    @Override
    public boolean canChestBeOpenedByMultiplePlayers() {
        return false;
    }

    @Override
    public List<ItemStack> genererContenuCoffre() {
        return new LinkedList<>();
    }

    @Override
    public boolean automaticallyGiveItemsToPlayer() {
        return true;
    }
}
