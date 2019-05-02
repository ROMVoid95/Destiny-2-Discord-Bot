package com.prime.util.destiny;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class UserCharID {
    
    public static String[] getCharID(String memberID) throws Exception {
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
