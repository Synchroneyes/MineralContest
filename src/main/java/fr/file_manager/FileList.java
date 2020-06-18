package fr.file_manager;

import java.io.File;

public enum FileList {

    Lang_french_file("language", "french.yml"),
    Lang_english_file("language", "english.yml"),
    Config_default_plugin("config", "plugin_config.yml"),
    Config_default_game("config/game", "game_settings.yml"),
    Config_default_arena_chest("config/arena", "arena_chest_content.yml"),
    Config_default_player_base_item("config/game", "player_base_items.yml"),
    CustomMap_arena_scheme("custom-maps-models", "arena.yml"),
    CustomMap_north_house_scheme("custom-maps-models", "north-house.yml"),
    CustomMap_south_house_scheme("custom-maps-models", "south-house.yml"),
    CustomMap_east_house_scheme("custom-maps-models", "east-house.yml"),
    CustomMap_west_house_scheme("custom-maps-models", "west-house.yml");

    private String path;
    private String fileName;

    FileList(String path, String fileName) {
        this.path = path;
        this.fileName = fileName;
    }


    public String getPath() {
        return path;
    }

    public String getFileName() {
        return fileName;
    }

    public String toString() {
        String dossier = getPath().replace("/", File.separator);
        return dossier + File.separator + getFileName();
    }
}
