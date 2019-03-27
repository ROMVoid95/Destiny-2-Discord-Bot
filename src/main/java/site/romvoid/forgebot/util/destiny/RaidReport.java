package site.romvoid.forgebot.util.destiny;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RaidReport {

    private static String domain =
            "https://b9bv2wd97h.execute-api.us-west-2.amazonaws.com/prod/api/player/";

    public static String Speedtier;
    public static String ClearsTier;

    private static JsonObject getRequest(String memberId) throws Exception {
        URL getRequestURL = new URL(domain + memberId + "/");
        HttpURLConnection con = (HttpURLConnection) getRequestURL.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        String response = "";
        while ((inputLine = in.readLine()) != null) 
            response += inputLine;
            in.close();
            JsonParser parser = new JsonParser();
            JsonObject json = (JsonObject) parser.parse(response);
            return json;

    }

    public static String getClearsRank(String memberId) throws Exception {
        JsonObject json = getRequest(memberId);
        JsonElement clearsRank;
        String value;
        String tier;
        String subtier;

        json = json.getAsJsonObject("response");
        clearsRank = json.getAsJsonObject().get("clearsRank");
        value = clearsRank.getAsJsonObject().get("value").getAsString();
        tier = clearsRank.getAsJsonObject().get("tier").getAsString();
        RaidReport.setClearsTier(tier);
        try {
            subtier = clearsRank.getAsJsonObject().get("rank").getAsString();
            String stats = String.format("%s, **%s** \n %s",subtier, tier, value);
            return stats;
        } catch (Exception e) {
            subtier = clearsRank.getAsJsonObject().get("subtier").getAsString();
            String stats = String.format("**%s   %s** \n %s", tier, subtier, value);
            return stats;
        }

    }



    public static String getSpeedRank(String memberId) throws Exception {
        JsonObject json = getRequest(memberId);
        JsonElement speed;
        int timeSec;
        String tier;
        String subtier;;
        json = json.getAsJsonObject("response");
        speed = json.getAsJsonObject().get("speedRank");
        timeSec = speed.getAsJsonObject().get("value").getAsInt();
        tier = speed.getAsJsonObject().get("tier").getAsString();
        RaidReport.setSpeedTier(tier);
        int p1 = timeSec % 60;
        int p2 = timeSec / 60;
        int p3 = p2 % 60;
        p2 = p2 / 60;
        String time = String.format("%sh %sm %ss ", p2, p3, p1);
        try {
            subtier = speed.getAsJsonObject().get("rank").getAsString();
            String stats = String.format("%s, **%s** \n %s",subtier, tier, time);
            return stats;

        } catch (Exception e) {
            subtier = speed.getAsJsonObject().get("subtier").getAsString();
            String stats = String.format("**%s   %s** \n %s", tier, subtier, time);
            return stats;
        }
    }


    /**
     * @return the Speed stier
     */
    public static String getSpeedTier() {
        return Speedtier;
    }

    /**
     * @return the clears Tier
     */
    public static String getClearsTier() {
        return ClearsTier;
    }

    public static void setClearsTier(String clearsTier) {
        ClearsTier = clearsTier;
    }

    public static void setSpeedTier(String tier) {
        RaidReport.Speedtier = tier;
    }
}
