package site.romvoid.forgebot.util.destiny;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Request {

    private static String apiKey = "101ac0efa4d74ce883ffe3126713b339";
    
    public static String getApiKey() {
        return apiKey;
    }

    public static String getDomain() {
        return domain;
    }

    private static String domain = "https://www.bungie.net/Platform/";

    public static JsonObject get(String endPoint) throws IOException {
        URL getRequestURL = new URL(domain + endPoint);
        HttpURLConnection con = (HttpURLConnection) getRequestURL.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("X-API-KEY", apiKey);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        String response = "";
        while ((inputLine = in.readLine()) != null) {
            response += inputLine;
            in.close();
            JsonParser parser = new JsonParser();
            JsonObject json = (JsonObject) parser.parse(response);
            return json;
        }
        return null;
    }
}
