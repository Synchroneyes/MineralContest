package fr.world_downloader;

import org.json.JSONObject;

public class MapInfo {
    public String map_url;
    public String map_name;
    public String map_size;
    public String map_size_display;
    public String map_description;
    public String map_file_name;
    public String map_folder_name;

    public static MapInfo fromJsonObject(JSONObject object) {
        MapInfo map = new MapInfo();
        map.map_name = object.getString("map_name");
        map.map_url = object.getString("map_url");
        map.map_size = object.getString("map_size");
        map.map_size_display = object.getString("map_size_display");
        map.map_description = object.getString("map_description");
        map.map_file_name = object.getString("map_file_name");
        map.map_folder_name = object.getString("map_folder_name");

        return map;
    }
}
