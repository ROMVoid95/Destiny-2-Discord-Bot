package site.romvoid.forgebot.util.destiny.pvp;

import java.io.IOException;
import com.google.gson.JsonObject;
import site.romvoid.forgebot.util.destiny.Request;
import site.romvoid.forgebot.util.destiny.UserMemberID;
import site.romvoid.forgebot.util.exemptions.IndexExemption;

public class BaseStats {
    
    public static String getKd(String nickname) throws IndexExemption, IOException {
        String memberID = UserMemberID.getMemberId(nickname);
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
    
    public static String getKda(String nickname) throws IndexExemption, IOException {
        String memberID = UserMemberID.getMemberId(nickname);
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
