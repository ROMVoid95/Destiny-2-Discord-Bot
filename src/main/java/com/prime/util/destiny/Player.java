package com.prime.util.destiny;

import java.util.regex.Pattern;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.prime.util.excemptions.NicknameExcemption;
import com.prime.util.excemptions.PlayerNotFound;

public class Player {
  
  private static boolean isNicknameValid = false;

    /**
     * Retreive the MembershipID for the discord user. Requires the user to have nickname set to a
     * valid Battle.net ID.
     * 
     * @param nickname (String)
     * @return MembershipID (String)
     * @throws NicknameExcemption
     * @throws PlayerNotFound
     */
    public static String getId(String nickname) throws PlayerNotFound {
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
            }
            return id;
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
    
    public static boolean isNicknameValid(String nickname) {
      if (getId(nickname) != null) {
        isNicknameValid = true;

      } 
      return isNicknameValid;
      
    }
    
    public static String removeBrackets(String nickname) {
    	String parsedNickname = nickname.replaceFirst(Pattern.quote(nickname.substring(nickname.indexOf("["), nickname.indexOf("]") + 1)), "").replaceAll(" ", "");
        return parsedNickname;
        
    }
    
    public static boolean containsBrackets(String nickname) {
    	if (nickname.contains("[")) {
    		return true;
    	} else {
			return false;
		}
    }
}
