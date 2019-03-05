package site.romvoid.forgebot.util.destiny;

import java.io.IOException;
import com.google.gson.JsonObject;
import site.romvoid.forgebot.util.exemptions.IndexExemption;

public class UserMemberID {

    public static String getMemberId(String name) throws Exception {
        String nickname = name.replace("#", "%23");
        String endpoint = "Destiny2/SearchDestinyPlayer/-1/" + nickname + "/";
        JsonObject json = Request.get(endpoint);
        String set = null;
        String none = null;
        try {
            set = json.getAsJsonArray("Response").get(0).getAsJsonObject().get("membershipId").getAsString();
        } catch (Exception e) {
            return none;
        }
        return set;

    }
}
