package site.romvoid.forgebot.util.destiny.pvp;

import com.google.gson.JsonObject;
import site.romvoid.forgebot.util.destiny.Request;

public class BaseStats {
    
    public static String getKd(String memberID) throws Exception {
        String endpoint = "/Destiny2/4/Account/" + memberID + "/Stats/?groups=general";
        JsonObject json;
        JsonObject allChar;
        JsonObject results;
        JsonObject kdr;
        String value;
        //collect
        json = Request.get(endpoint);
                json = json.getAsJsonObject("Response");
                allChar = json.getAsJsonObject("mergedAllCharacters");
                results = allChar.getAsJsonObject("results");
                kdr = results.getAsJsonObject("allPvP").getAsJsonObject("allTime").getAsJsonObject("killsDeathsRatio").getAsJsonObject("basic");
                value = kdr.get("displayValue").getAsString();
                            return value;   

    }
    
    public static String getKda(String memberID) throws Exception {
        String endpoint = "/Destiny2/4/Account/" + memberID + "/Stats/?groups=general";
        JsonObject json;
        JsonObject allChar;
        JsonObject results;
        JsonObject kdr;
        String value;
        //collect
        json = Request.get(endpoint);
                json = json.getAsJsonObject("Response");
                allChar = json.getAsJsonObject("mergedAllCharacters");
                results = allChar.getAsJsonObject("results");
                kdr = results.getAsJsonObject("allPvP").getAsJsonObject("allTime").getAsJsonObject("efficiency").getAsJsonObject("basic");
                value = kdr.get("displayValue").getAsString();
                            return value;   

    }

}
