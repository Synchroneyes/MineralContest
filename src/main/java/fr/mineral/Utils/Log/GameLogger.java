package fr.mineral.Utils.Log;

import com.sk89q.worldedit.event.platform.CommandEvent;
import fr.mineral.Core.Game.Game;
import org.bukkit.Bukkit;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.Stack;

public class GameLogger {

    private static Stack<Log> logs;
    private static GameLogger instance;

    private GameLogger() {
        if (instance == null) instance = this;
        logs = new Stack<>();
    }

    public static GameLogger getInstance() {
        if (instance == null) return new GameLogger();
        return instance;
    }


    public static void addLog(Log log) {
        if (instance == null) instance = new GameLogger();
        log.setId(logs.size()+1);
        logs.add(log);
        Bukkit.getLogger().info(log.toJson());
    }

    public static String toJson() {
        JSONObject object = new JSONObject();
        for (Log log : logs) {
            object.put("" + log.getId(), log.toJsonObject());
        }

        return object.toString();
    }

}
