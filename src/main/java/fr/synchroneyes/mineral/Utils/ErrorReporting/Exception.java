package fr.synchroneyes.mineral.Utils.ErrorReporting;

import org.json.JSONObject;

public class Exception {
    public static JSONObject toJson(java.lang.Exception exception) {
        JSONObject json = new JSONObject();

        json.put("exception_type", exception.getClass().toGenericString());
        json.put("exception_name", exception.getClass().toString());
        json.put("message", exception.getMessage());
        json.put("localized_message", exception.getLocalizedMessage());
        json.put("stacktrace", exception.getStackTrace());

        if (exception.getCause() != null) {
            json.put("cause", exception.getCause().getStackTrace());
        }

        return json;
    }
}
