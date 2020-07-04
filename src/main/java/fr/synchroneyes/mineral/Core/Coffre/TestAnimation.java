package fr.synchroneyes.mineral.Core.Coffre;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;

public class TestAnimation extends AutomatedChestAnimation {


    public TestAnimation() {
        super(9 * 3);
    }

    @Override
    public String getOpeningChestTitle() {
        return ChatColor.DARK_RED + "Piratage du largage...";
    }

    @Override
    public String getOpenedChestTitle() {
        return ChatColor.DARK_GREEN + "Le coffre est ouvert!";
    }

    @Override
    public Material getWaitingItemMaterial() {
        return Material.RED_STAINED_GLASS_PANE;
    }

    @Override
    public Material getUsedItemMaterial() {
        return Material.GREEN_STAINED_GLASS_PANE;
    }

    @Override
    public LinkedList<Integer> getOpeningSequence() {

        LinkedList<Integer> sequence = new LinkedList<>();

        sequence.add(0);
        sequence.add(18);
        sequence.add(26);
        sequence.add(8);
        sequence.add(11);
        sequence.add(12);
        sequence.add(13);
        sequence.add(14);
        sequence.add(15);

        return sequence;
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
        LinkedList<ItemStack> items = new LinkedList<>();
        items.add(new ItemStack(Material.DIAMOND, 64));
        return items;
    }

    @Override
    public boolean automaticallyGiveItemsToPlayer() {
        return true;
    }
}
