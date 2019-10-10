package fr.mineral.Utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerUtils {
    public static void givePlayerBaseItems(Player joueur) {
            /*
            On donne au joueur:
                - Arc
                - 64 fleches
                - Epée en fer
                -
             */

        joueur.getInventory().addItem(new ItemStack(Material.BOW, 1));
        joueur.getInventory().addItem(new ItemStack(Material.ARROW, 64));
        joueur.getInventory().addItem(new ItemStack(Material.IRON_SWORD, 1));
        joueur.getInventory().addItem(new ItemStack(Material.COOKED_BEEF,30));

        // On protege le joueur
        ItemStack[] armure = new ItemStack[4];
        armure[0] = new ItemStack(Material.IRON_BOOTS, 1);
        armure[1] = new ItemStack(Material.IRON_LEGGINGS, 1);
        armure[2] = new ItemStack(Material.IRON_CHESTPLATE, 1);
        armure[3] = new ItemStack(Material.IRON_HELMET, 1);

        joueur.getInventory().setArmorContents(armure);
    }
}
