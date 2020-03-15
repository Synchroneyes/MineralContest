package fr.mineral.Core.MapBuilder;

import fr.mineral.Core.MapBuilder.Item.HouseEgg;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Utils {

    /*
        Convert a regular item into a building item
     */
    public static void itemStackConverter(ItemStack item, Player player) {
        switch(item.getType()) {
            case EGG:
                // This is an house egg
                HouseEgg.giveEggToPlayer(player);
                break;
        }
    }
}
