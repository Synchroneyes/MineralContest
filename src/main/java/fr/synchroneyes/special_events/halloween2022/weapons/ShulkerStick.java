package fr.synchroneyes.special_events.halloween2022.weapons;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.List;

public class ShulkerStick {


    public static String getItemName() {
        return ChatColor.RED + "(MineralContest) " + ChatColor.GOLD + "GravityStick";
    }

    public static ItemStack getItem() {
        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta meta = stick.getItemMeta();

        meta.setDisplayName(getItemName());
        List<String> lore = new LinkedList<>();
        lore.add("Arme secrète obtenu lors de votre victoire contre le chien de ???");
        lore.add("Visez un joueur, et faites un clic gauche...");
        lore.add("Attention, pour que cet item fonctionne, il ne doit pas être stacké");
        meta.setLore(lore);

        stick.setItemMeta(meta);

        return stick;
    }

    public static int cooldownBetweenAttacks() {
        return 1;
    }
}
