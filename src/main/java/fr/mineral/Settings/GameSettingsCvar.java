package fr.mineral.Settings;

import java.io.IOException;

public enum GameSettingsCvar {

    mp_enable_metrics("cvar", "int", "mp_enable_metrics", "1", true),
    mp_randomize_team("cvar", "int", "mp_randomize_team", "1", true),
    mp_enable_item_drop("cvar", "int", "mp_enable_item_drop", "2", true),
    SCORE_IRON("cvar", "int", "SCORE_IRON", "10", true),
    SCORE_GOLD("cvar", "int", "SCORE_GOLD", "50", true),
    SCORE_DIAMOND("cvar", "int", "SCORE_DIAMOND", "150", true),
    SCORE_EMERALD("cvar", "int", "SCORE_EMERALD", "300", true),
    mp_team_max_player("cvar", "int", "mp_team_max_player", "2", true),
    mp_set_language("cvar", "string", "mp_set_language", "french", true),
    world_name("settings", "string", "world_name", "world", true),
    mp_set_playzone_radius("cvar", "int", "mp_set_playzone_radius", "1000", true),
    mp_enable_friendly_fire("cvar", "int", "mp_enable_friendly_fire", "1", true),
    mp_enable_old_pvp("cvar", "int", "mp_enable_old_pvp", "1", true),
    mp_enable_block_adding("cvar", "int", "mp_enable_block_adding", "1", true),
    game_time("settings", "int", "game_time", "60", false),
    pre_game_timer("settings", "int", "pre_game_timer", "1", false),
    chest_opening_cooldown("arena", "int", "chest_opening_cooldown", "7", true),
    max_time_between_chests("arena", "int", "max_time_between_chests", "15", true),
    min_time_between_chests("arena", "int", "min_time_between_chests", "10", true),
    max_item_in_chest("arena", "int", "max_item_in_chest", "20", true),
    min_item_in_chest("arena", "int", "min_item_in_chest", "10", true),
    death_time("settings", "int", "death_time", "10", false);

    private String type;
    private String name;
    private Object value;
    private String expectedValue;
    private boolean canBeReloadedInGame;


    GameSettingsCvar(String type, String expectedValue, String name, String value, boolean canBeReloadedInGame) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.expectedValue = expectedValue;
        this.canBeReloadedInGame = canBeReloadedInGame;
    }

    public boolean canBeReloaded() { return canBeReloadedInGame;}

    public String getType() { return this.type;}
    public String getName() { return this.name;}
    public Object getValue() {
        if(this.expectedValue.equals("int")) return this.getValueInt();
        return this.value;
    }


    public String getExpectedValue() { return this.expectedValue;}
    public int getValueInt() {
        if(this.value instanceof String) {
            return Integer.parseInt((String) this.value);
        } else {
            return (int) this.value;
        }
    }

    public void setValue(Object newValue) {
        this.value = newValue;
        GameSettings gameSettings = GameSettings.getInstance();
        try {
            gameSettings.updatePluginConfig(gameSettings.buildConfigKeyValue(this), this.value);
            gameSettings.saveConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        return (String) this.value;
    }

    public static Object getValueFromCVARName(String name) {
        if(!GameSettings.getInstance().isConfigLoaded) GameSettings.getInstance().loadGameSettings(GameSettings.PLUGIN_START);
        for(GameSettingsCvar cvar : values())
            if(cvar.getName().equals(name))
                return cvar.getValue();
        return null;
    }

    public static void setValueFromCVARName(String name, String value) throws IOException {
        for(GameSettingsCvar cvar : values())
            if(cvar.getName().equals(name)){
                cvar.setValue(value);
                return;
            }

    }
}
