package fr.mineral.Core.MapBuilder.Item;

import fr.mineral.Translation.Lang;
import fr.mineral.Utils.Player.PlayerUtils;
import fr.mineral.Utils.Save.SaveHouse;
import fr.mineral.Utils.SaveableBlock;
import fr.mineral.mineralcontest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;

public class HouseEgg {
    private ItemStack item;
    private String itemName;

    private String direction;
    private Player player;

    FileConfiguration data;



    public HouseEgg() {
        this.item = new ItemStack(Material.EGG);
        this.itemName = Lang.map_builder_item_name.toString();

        ItemMeta m = this.item.getItemMeta();
        m.setDisplayName(this.itemName);
        this.item.setItemMeta(m);
    }

    private void setPlayer(Player p) {
        this.player = p;
    }

    public static void giveEggToPlayer(Player p) {
        HouseEgg houseEgg = new HouseEgg();
        houseEgg.setPlayer(p);
        p.getInventory().setItemInMainHand(houseEgg.item);
    }

    public String getItemName() { return this.itemName;}

    public static void spawnHouse(Player player) {
        String direction = PlayerUtils.getLookingDirection(player);
        assert direction != null;
        switch(direction) {
            case "NE":
            case "NW":
                direction = "N";
                break;

            case "SE":
            case "SW":
                direction = "S";
                break;
        }

        SaveHouse sh = mineralcontest.plugin.getSaveHouse();
        try {
            sh.loadHouse(mineralcontest.plugin.getDataFolder() + "/data/houses/" + direction + ".yml", player);
        }catch(IOException ioe) {
            player.sendMessage("Unable to load the file " + mineralcontest.plugin.getDataFolder() + "/data/houses/" + direction + ".yml");
        }
    }
}
