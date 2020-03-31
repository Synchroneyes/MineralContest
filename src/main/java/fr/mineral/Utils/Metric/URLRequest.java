package fr.mineral.Utils.Metric;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class URLRequest {

    private URL url;
    private Map<String, Object> parameters;
    private String queryResult;
    private String method;

    public URLRequest(String method) {
        this.parameters = new LinkedHashMap<>();
        this.queryResult = "";
        this.method = method;
    }

    public URL setUrl(String u) throws Exception{
        this.url = new URL(u);
        return this.url;
    }

    public URL getUrl() { return this.url; }

    public Map<String, Object> addParameters(String key, Object value) {
        this.parameters.put(key, value);
        return this.parameters;
    }

    public Map<String, Object> getParameters(String key, Object value) {
        return this.parameters;
    }

    public String getQueryResult() throws IOException {
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : this.parameters.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod(this.method);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

        for (int c; (c = in.read()) >= 0;)
            this.queryResult += (char) c;

        return this.queryResult;
    }


}