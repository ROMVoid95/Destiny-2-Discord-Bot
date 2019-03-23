package site.romvoid.forgebot.util.destiny;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import site.romvoid.forgebot.util.exemptions.NicknameExcemption;
import site.romvoid.forgebot.util.exemptions.PlayerNotFound;

public class Player {

    /**
     * Retreive the MembershipID for the discord user. Requires the user to have nickname set to a
     * valid Battle.net ID.
     * 
     * @param nickname (String)
     * @return MembershipID (String)
     * @throws NicknameExcemption
     * @throws PlayerNotFound
     */
    public static String getId(String nickname) throws NicknameExcemption, PlayerNotFound {
        String endpoint = "Destiny2/SearchDestinyPlayer/-1/" + nickname.replace("#", "%23") + "/";
        JsonObject json = null;
        try {
            json = Request.get(endpoint);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String id = null;
        JsonArray set = null;

        // Throw Exemption if Nickname doesn't contain "#"
        if (!nickname.contains("#")) {
            throw new NicknameExcemption("Your current Nickname **" + nickname
                    + "** does not match a valid Battle.net ID");
        } else {
            set = json.getAsJsonArray("Response").getAsJsonArray();
            if (set.size() > 0) {
                id = json.getAsJsonArray("Response").get(0).getAsJsonObject().get("membershipId").getAsString();
                // Return Membership ID
                return id;
            } else {
                throw new PlayerNotFound(
                        "Cannot find a Destiny 2 account with the nickname **" + nickname + "**");
            }
        }
    }
    
    public static String[] getCharIDs(String memberID) throws Exception {
        String endpoint = "Destiny2/4/Account/" + memberID + "/Stats/";
        JsonArray jray;
        String[] charIds;
        JsonObject json = Request.get(endpoint);
        json = json.getAsJsonObject("Response");
        jray = json.getAsJsonArray("characters");
        //Made possible by Clowie
        charIds = new String[jray.size()];
        for (int i = 0; i < jray.size(); i++) {
            charIds[i] = ((JsonObject) jray.get(i)).get("characterId").toString().replace("\"", "");
            }
        return charIds;
    }
        
}
