package fr.synchroneyes.mineral.Utils;

import fr.synchroneyes.mineral.Utils.ErrorReporting.Error;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

import java.util.List;

public class WorldUtils {

    /*
    Credit: https://bukkit.org/threads/remove-dropped-items-on-ground.100750/
     */
    public static void removeAllDroppedItems(World w) {

        List<Entity> entList = w.getEntities();//get all entities in the world

        for (Entity current : entList) {//loop through the list
            if (current instanceof Item) {//make sure we aren't deleting mobs/players
                current.remove();//remove it
            }
        }
    }

}
