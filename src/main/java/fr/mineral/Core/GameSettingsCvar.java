package fr.mineral.Core;

import java.io.IOException;

public enum GameSettingsCvar {

    mp_enable_metrics("cvar", "int", "mp_enable_metrics", "1"),
    mp_randomize_team("cvar", "int", "mp_randomize_team", "1"),
    mp_enable_item_drop("cvar", "int", "mp_enable_item_drop", "2"),
    SCORE_IRON("cvar", "int", "SCORE_IRON", "10"),
    SCORE_GOLD("cvar", "int", "SCORE_GOLD", "50"),
    SCORE_DIAMOND("cvar", "int", "SCORE_DIAMOND", "150"),
    SCORE_EMERALD("cvar", "int", "SCORE_EMERALD", "300"),
    mp_team_max_player("cvar", "int", "mp_team_max_player", "2"),
    mp_set_language("cvar", "string", "mp_set_language", "french"),
    world_name("settings", "string", "world_name", "world"),
    mp_set_playzone_radius("cvar", "int", "mp_set_playzone_radius", "1000"),
    mp_enable_friendly_fire("cvar", "int", "mp_enable_friendly_fire", "1");

    /*
    TODO APPLY SET PLAYZONE AND FRIENDLY FIRE
     */

    private String type;
    private String name;
    private Object value;
    private String expectedValue;

    GameSettingsCvar(String type, String expectedValue, String name, String value) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.expectedValue = expectedValue;
    }

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
