package fr.synchroneyes.mineral.Core.Coffre.Coffres;

import fr.synchroneyes.mineral.Core.Coffre.Animations;
import fr.synchroneyes.mineral.Core.Coffre.AutomatedChestAnimation;
import fr.synchroneyes.mineral.Core.Coffre.AutomatedChestManager;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.List;

public class CoffreBoss extends AutomatedChestAnimation {


    private List<ItemStack> items;

    /**
     * Constructeur, permet de donner en paramètre le nom de l'inventaire ainsi que la taille
     * @param manager
     */
    public CoffreBoss(List<ItemStack> items, AutomatedChestManager manager) {
        super(54, manager);
        this.items = items;
    }

    @Override
    public void actionToPerformBeforeSpawn() {

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
        return "Ouverture du coffre...";
    }

    @Override
    public String getOpenedChestTitle() {
        return "Coffre de boss ouvert";
    }

    @Override
    public ItemStack getWaitingItemMaterial() {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public ItemStack getUsedItemMaterial() {
        ItemStack item = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public LinkedList<Integer> getOpeningSequence() {
        return Animations.SIX_LINES_PUMPKINS.toList();
    }

    @Override
    public Material getChestMaterial() {
        return Material.ENDER_CHEST;
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
        return this.items;
    }

    @Override
    public boolean automaticallyGiveItemsToPlayer() {
        return true;
    }
}
