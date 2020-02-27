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

    private String name;
    private String direction;
    private Player player;

    FileConfiguration data;



    public HouseEgg(String Name, Player p) {
        this.item = new ItemStack(Material.EGG);
        this.itemName = Lang.map_builder_item_name.toString();

        ItemMeta m = this.item.getItemMeta();
        m.setDisplayName(this.itemName);
        this.item.setItemMeta(m);

        this.name = Name;
        this.player = p;
        p.getInventory().setItemInMainHand(this.item);
    }

    public String getItemName() { return this.itemName;}

    public void spawnHouse() {
        this.direction = PlayerUtils.getLookingDirection(this.player);
        assert this.direction != null;
        switch(this.direction) {
            case "NE":
            case "NW":
                this.direction = "N";
                break;

            case "SE":
            case "SW":
                this.direction = "S";
                break;
        }

        SaveHouse sh = mineralcontest.plugin.getSaveHouse();
        try {
            sh.load(mineralcontest.plugin.getDataFolder() + "/data/houses/" + this.direction + ".yml", this.player);
        }catch(IOException ioe) {
            this.player.sendMessage("Unable to load the file " + mineralcontest.plugin.getDataFolder() + "/data/houses/" + this.direction + ".yml");
        }
    }
}
