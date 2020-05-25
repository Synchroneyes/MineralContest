package fr.mineral.Utils.Log;

import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

public class Log {
    private String type;
    private String content;
    private Date hour;
    private String cause;
    private int id;

    public Log(String type, String content, String cause) {
        this.type = type;
        this.content = content;
        this.hour = new Date();
        this.cause = cause;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String toJson() {

        DateFormat shortDateFormat = DateFormat.getDateTimeInstance(
                DateFormat.SHORT,
                DateFormat.SHORT);

        JSONObject value = new JSONObject();
        value.put("type", type);
        value.put("content", content);
        value.put("date", shortDateFormat.format(hour));
        value.put("cause", cause);

        return value.toString();

    }

    public JSONObject toJsonObject() {

        DateFormat shortDateFormat = DateFormat.getDateTimeInstance(
                DateFormat.SHORT,
                DateFormat.SHORT);

        JSONObject value = new JSONObject();
        value.put("type", type);
        value.put("content", content);
        value.put("date", shortDateFormat.format(hour));
        value.put("cause", cause);

        return value;

    }
}
