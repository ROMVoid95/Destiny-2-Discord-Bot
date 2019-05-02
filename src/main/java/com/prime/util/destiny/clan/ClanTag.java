package com.prime.util.destiny.clan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.prime.util.Info;

public class ClanTag {
    
    private static String apiKey = Info.BUNGIE_API_KEY;
    private static String domain = "https://www.bungie.net/Platform/";
    
    private static JsonObject get(String endPoint) throws IOException {
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
            System.out.println(response);
            JsonParser parser = new JsonParser();
            JsonObject json = (JsonObject) parser.parse(response);
            return json;
        }
        return null;
    }

    public static String getName(String memberID) throws IOException {
        String endpoint = "GroupV2/User/2/" + memberID + "/0/1/";
        JsonObject json;
        JsonObject group;
        JsonArray jray;
        String name;
        //collect
        json = get(endpoint);
                json = json.getAsJsonObject("Response");
                jray = json.getAsJsonArray("results");
                String none = null;
                if (jray.size()  == 0) 
                    return none;
                        group = (JsonObject) jray.get(0).getAsJsonObject().get("group");
                        name = group.get("name").getAsString();
                            return name;   
    }

    public static String memberCount(String memberID) throws IOException {
        String endpoint = "GroupV2/User/2/" + memberID + "/0/1/";
        JsonObject json;
        JsonObject group;
        JsonArray jray;
        String memberCount;
        //collect
        json = get(endpoint);
                json = json.getAsJsonObject("Response");
                jray = json.getAsJsonArray("results");
                String none = null;
                if (jray.size()  == 0) 
                    return none;
                        group = (JsonObject) jray.get(0).getAsJsonObject().get("group");
                        memberCount = group.get("memberCount").getAsString();
                            return memberCount;
    }

    public static String clanMoto(String memberID)  throws IOException {
        String endpoint = "GroupV2/User/2/" + memberID + "/0/1/";
        JsonObject json;
        JsonObject group;
        JsonArray jray;
        String motto;
        //collect
        json = get(endpoint);
                json = json.getAsJsonObject("Response");
                jray = json.getAsJsonArray("results");
                String none = null;
                if (jray.size()  == 0) 
                    return none;
                        group = (JsonObject) jray.get(0).getAsJsonObject().get("group");
                        motto = group.get("motto").getAsString();
                            return motto;
    }

    public static String getUserRank(String memberID)  throws IOException {
        String endpoint = "GroupV2/User/2/" + memberID + "/0/1/";
        JsonObject json;
        JsonObject member;
        JsonArray jray;
        String memberType;
        String rank = null;
        //collect
        json = get(endpoint);
                json = json.getAsJsonObject("Response");
                jray = json.getAsJsonArray("results");
                String none = null;
                if (jray.size()  == 0) 
                    return none;
                        member = (JsonObject) jray.get(0).getAsJsonObject().get("member");
                        memberType = member.get("memberType").getAsString();
                        if (memberType.equals("1")) 
                            rank = "Beginner";
                        if (memberType.equals("2")) 
                            rank = "Member";
                        if (memberType.equals("3")) 
                            rank = "Admin";
                        if (memberType.equals("4")) 
                            rank = "Acting Founder";
                        if (memberType.equals("5")) 
                            rank = "Founder";
                        
                        return rank;
                            
    }

    public static String callSign(String memberID)  throws IOException {
        String endpoint = "GroupV2/User/2/" + memberID + "/0/1/";
        JsonObject json;
        JsonObject group;
        JsonObject claninfo;
        JsonArray jray;
        String callsign;
        //collect
        json = get(endpoint);
                json = json.getAsJsonObject("Response");
                jray = json.getAsJsonArray("results");
                String none = null;
                if (jray.size()  == 0) 
                    return none;
                        group = (JsonObject) jray.get(0).getAsJsonObject().get("group");
                        claninfo = (JsonObject) group.getAsJsonObject().get("clanInfo");
                        callsign = claninfo.get("clanCallsign").getAsString();
                            return callsign;
    }

    public static String join(String memberID)  throws IOException {
        String endpoint = "GroupV2/User/2/" + memberID + "/0/1/";
        JsonObject json;
        JsonObject group;
        JsonArray jray;
        String join;
        String option = null;
        //collect
        json = get(endpoint);
                json = json.getAsJsonObject("Response");
                jray = json.getAsJsonArray("results");
                String none = null;
                if (jray.size()  == 0) 
                    return none;
                        group = (JsonObject) jray.get(0).getAsJsonObject().get("group");
                        join = group.get("membershipOption").getAsString();
                        if (join.equals("0")) 
                            option = "Requires Approval";
                        if (join.equals("1")) 
                            option = "Open Membership";
                        if (join.equals("2")) 
                            option = "Invite Only";
                        
                            return option;
    }

}
