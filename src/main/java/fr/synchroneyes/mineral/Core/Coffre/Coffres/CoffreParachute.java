package fr.synchroneyes.mineral.Core.Coffre.Coffres;

import fr.synchroneyes.mineral.Core.Coffre.Animations;
import fr.synchroneyes.mineral.Core.Coffre.AutomatedChestAnimation;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.List;

public class CoffreParachute extends AutomatedChestAnimation {

    /**
     * Constructeur, permet de donner en paramètre le nom de l'inventaire ainsi que la taille
     */
    public CoffreParachute() {
        // On veut 5 lignes
        super(5 * 9);
    }

    @Override
    public boolean displayWaitingItems() {
        return false;
    }

    @Override
    public String getOpeningChestTitle() {
        return ChatColor.DARK_RED + "Ouverture du largage ...";
    }

    @Override
    public String getOpenedChestTitle() {
        return ChatColor.DARK_GREEN + "Le coffre a été ouvert";
    }

    @Override
    public ItemStack getWaitingItemMaterial() {
        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) meta.setDisplayName(" ");
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public ItemStack getUsedItemMaterial() {
        ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) meta.setDisplayName(" ");
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public LinkedList<Integer> getOpeningSequence() {
        return Animations.FIVE_LINES_AROUND_THEN_CENTER.toList();
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

        LinkedList<ItemStack> items = new LinkedList<>();
        items.add(new ItemStack(Material.EMERALD, 64));
        return items;
    }

    @Override
    public boolean automaticallyGiveItemsToPlayer() {
        return false;
    }
}
