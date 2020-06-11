package fr.world_downloader.Inventories;

import fr.mineral.Translation.Lang;
import fr.world_downloader.Items.MapDownloadItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

public class MapListInventory extends InventoryInterface {


    @Override
    public void setInventoryItems(Player arbitre) {
        String json = "{ \"maps\": [ { \"map_name\": \"mc_savane\", \"map_url\": \"http://localhost/get/savane\", \"map_description\": \"Une petite map à jouer avec 3 équipes\\n, Dans la savane\" }, { \"map_name\": \"mc_egypte\", \"map_url\": \"http://localhost/get/egypte\", \"map_description\": \"Une petite map à jouer avec 3 équipes\\n, dans des pyramides\" }, { \"map_name\": \"mc_4_team_max\", \"map_url\": \"http://localhost/get/4_team_max\", \"map_description\": \"Une petite map à jouer avec 4 teams\\n, avec 4 tams\" }, ] }";
        JSONObject jsonObject = new JSONObject(json);

        JSONArray maps = jsonObject.getJSONArray("maps");
        for (int i = 0; i < maps.length(); ++i) {
            JSONObject map = maps.getJSONObject(i);

            registerItem(new MapDownloadItem(map.get("map_name").toString(),
                    map.get("map_url").toString(),
                    map.get("map_description").toString()));
        }

    }

    @Override
    public Material getItemMaterial() {
        return Material.MAP;
    }

    @Override
    public String getNomInventaire() {
        return Lang.map_downloader_inventory_name.toString();
    }

    @Override
    public String getDescriptionInventaire() {
        return Lang.map_downloader_inventory_name.toString();
    }
}
