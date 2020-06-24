package fr.synchroneyes.mineral.Core.Referee;

import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.Stack;

public class Referee {


    public static Player refereeForcingVote;

    public static void forceVote(Player refereeForcingVote) {
        Referee.refereeForcingVote = refereeForcingVote;
    }


    /**
     * Récupère le livre d'arbitrage
     */
    public static ItemStack getRefereeItem() {
        ItemStack item = new ItemStack(Material.BOOK, 1);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Lang.referee_item_name.toString());
        item.setItemMeta(itemMeta);
        return item;
    }
}
