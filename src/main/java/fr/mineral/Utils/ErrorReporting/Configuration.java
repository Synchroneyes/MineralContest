package fr.mineral.Utils.ErrorReporting;

import fr.mineral.Core.Game.Game;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class Configuration {
    public static JSONObject export() {
        File configFile = new File(mineralcontest.plugin.getDataFolder() + File.separator + "config.yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(configFile);
        JSONObject cvar = new JSONObject();
        JSONObject settings = new JSONObject();
        JSONObject arena = new JSONObject();
        JSONObject _chest_content = new JSONObject();
        for (String cvar_name : configuration.getConfigurationSection("config.cvar").getKeys(false)) {
            cvar.put(cvar_name, configuration.get("config.cvar." + cvar_name));
        }
        for (String cvar_name : configuration.getConfigurationSection("config.settings").getKeys(false)) {
            settings.put(cvar_name, configuration.get("config.settings." + cvar_name));
        }
        for (String cvar_name : configuration.getConfigurationSection("config.arena").getKeys(false)) {
            if (configuration.get("config.arena." + cvar_name) instanceof MemorySection) {
                final MemorySection section = (MemorySection) configuration.get("config.arena." + cvar_name);
                arena.put(cvar_name, new JSONObject());
                for (String chest_c : section.getKeys(false)) {
                    for (String value : configuration.getConfigurationSection(section.getCurrentPath() + "." + chest_c).getKeys(false)) {
                        _chest_content.put(chest_c, new JSONObject().put(value, configuration.get(section.getCurrentPath() + "." + chest_c + "." + value).toString()));
                    }
                }
            } else {
                arena.put(cvar_name, configuration.get("config.arena." + cvar_name));
            }
        }
        arena.put("chest_content", _chest_content);
        final JSONObject json = new JSONObject();
        json.put("config", new JSONObject().put("cvar", cvar).put("settings", settings).put("arena", arena));
        final Game game = mineralcontest.plugin.getGame();
        final JSONObject gameInfo = new JSONObject();
        gameInfo.put("plugin_version", mineralcontest.plugin.getDescription().getVersion());
        gameInfo.put("server_version", Bukkit.getVersion());
        gameInfo.put("game_started", game.isGameStarted());
        gameInfo.put("game_paused", game.isGamePaused());
        gameInfo.put("game_pregame", game.isPreGame());
        gameInfo.put("game_pregame_started", game.isPreGameAndGameStarted());
        gameInfo.put("game_initialized", game.isGameInitialized);
        gameInfo.put("game_timeleft", game.getTempsRestant());
        gameInfo.put("game_killcount", game.killCounter);
        gameInfo.put("game_biome", game.votemap.getWinnerBiome(false));


        final JSONObject teamInfos = new JSONObject();
        for (Player player : game.getRedHouse().getTeam().getJoueurs()) {
            teamInfos.put("team_red", player.getDisplayName());
        }
        for (Player player : game.getYellowHouse().getTeam().getJoueurs()) {
            teamInfos.put("team_yellow", player.getDisplayName());
        }
        for (Player player : game.getBlueHouse().getTeam().getJoueurs()) {
            teamInfos.put("team_blue", player.getDisplayName());
        }

        JSONArray players = new JSONArray();
        for (Player p : mineralcontest.plugin.pluginWorld.getPlayers())
            players.put(p.getDisplayName());

        json.put("game_info", gameInfo);
        json.put("team_info", teamInfos);
        json.put("online_players", players);

        return json;
    }
}
