package site.romvoid.forgebot.util.destiny.raids;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import site.romvoid.forgebot.util.destiny.Player;
import site.romvoid.forgebot.util.destiny.Request;
import site.romvoid.forgebot.util.destiny.UserCharID;
import site.romvoid.forgebot.util.destiny.Utils;

public class ReqRaids {
	
	static String apiKey = Request.getApiKey();
	static String domainBase = Request.getDomain();
	

	/**
	 * Retreive Raid Completions for a player
	 * 
	 * @param name : Players display name
	 * @return Players Raid Completions
	 */
	public static RaidStats collectAllRaidStats(String name) {
		String memberID;
		String[] characterIDs;
		RaidStats rs = new RaidStats();

		try {
			memberID = getMemberID(name);
			characterIDs = getCharacterIDs(memberID);
			for (int i = 0; i < characterIDs.length; i++) {
				rs.combineRaidStats(collectCharacterActivityCompletions(memberID, characterIDs[i]));
			}
		} catch (Exception e) {
			System.out.println("Unexpected error: " + e.getMessage());
		}
		return rs;
	}

	private static RaidStats collectCharacterActivityCompletions(String memberID, String characterID) throws Exception {
		// Init
		String endpoint = "Destiny2/4/Account/" + memberID + "/Character/" + characterID + "/Stats/AggregateActivityStats/";
		JsonObject json;
		JsonArray jray;
		Raids lev = new Raids();
		Raids eow = new Raids();
		Raids sos = new Raids();
		Raids lw = new Raids();
		Raids sp = new Raids();
		RaidStats r = new RaidStats();
		// Collect
		json = Request.get(endpoint);
		jray = json.getAsJsonObject("Response").getAsJsonArray("activities");
		// Print
		Utils.printJSon(json);

		// Leviathan
		lev.setGuidedHashes(new String[] { "89727599", "287649202", "1875726950", "3916343513", "4039317196" });
		lev.setNormalHashes(new String[] { "2693136600", "2693136601", "2693136602", "2693136603", "2693136604", "2693136605" });
		lev.setPrestigeHashes(new String[] { "417231112", "508802457", "757116822", "771164842", "1685065161", "1800508819", "2449714930", "3446541099", "3857338478", "3879860661", "3912437239", "4206123728"});		
			lev.setGuidedCompletions(searchJsonActivities(jray, lev.getGuidedHashes()));
			lev.setNormalCompletions(searchJsonActivities(jray, lev.getNormalHashes()));
			lev.setPrestigeCompletions(searchJsonActivities(jray, lev.getPrestigeHashes()));
				r.setLev(lev);

		// Eater of Worlds
		eow.setGuidedHashes(new String[] { "2164432138" });
		eow.setNormalHashes(new String[] { "3089205900" });
		eow.setPrestigeHashes(new String[] { "809170886" });	
			eow.setGuidedCompletions(searchJsonActivities(jray, eow.getGuidedHashes()));
			eow.setNormalCompletions(searchJsonActivities(jray, eow.getNormalHashes()));
			eow.setPrestigeCompletions(searchJsonActivities(jray, eow.getPrestigeHashes()));
				r.setEow(eow);

		// Spire of Stars
		sos.setGuidedHashes(new String[] { "3004605630" });
		sos.setNormalHashes(new String[] { "119944200" });
		sos.setPrestigeHashes(new String[] { "3213556450" });
			sos.setGuidedCompletions(searchJsonActivities(jray, sos.getGuidedHashes()));
			sos.setNormalCompletions(searchJsonActivities(jray, sos.getNormalHashes()));
			sos.setPrestigeCompletions(searchJsonActivities(jray, sos.getPrestigeHashes()));
				r.setSos(sos);
		
		// Last Wish ((Last Wish Level 58 Not Released))		
		lw.setGuidedHashes(new String[] { "1661734046"});	
		lw.setNormalHashes(new String[] { "2214608157", "2122313384" });
		//lw.setPrestigeHashes(new String[] { "2214608156" }) LEVEL 58
			lw.setGuidedCompletions(searchJsonActivities(jray, lw.getGuidedHashes()));
        	lw.setNormalCompletions(searchJsonActivities(jray, lw.getNormalHashes()));
        	//lw.setPrestigeCompletions(searchJsonActivities(jray, lw.getPrestigeHashes()));  LEVEL 58
				r.setLw(lw);
		
		// Scourge of the Past
		sp.setGuidedHashes(new String[] { "2812525063" });
		sp.setNormalHashes(new String[] {"548750096", "2812525063" });
			sp.setGuidedCompletions(searchJsonActivities(jray, sp.getGuidedHashes()));
			sp.setNormalCompletions(searchJsonActivities(jray, sp.getNormalHashes()));
				r.setSp(sp);
			
		return r;
	}

	private static int searchJsonActivities(JsonArray jray, String[] hashes) {
		// Init
		String hash;
		int count = 0;
		int completions = 0;
		JsonObject tmpJson;

		// Loop, Lookup, and Commulate
		for (int i = 0; i < jray.size(); i++) {
			tmpJson = jray.get(i).getAsJsonObject();
			hash = tmpJson.get("activityHash").getAsString();
			for (int j = 0; j < hashes.length; j++) {
				if (hash.equals(hashes[j])) {
					completions = tmpJson.getAsJsonObject("values").getAsJsonObject("activityCompletions").getAsJsonObject("basic").get("value").getAsInt();
					count += completions;
				}
			}
		}
		return count;
	}

	public static String getMemberID(String name) throws Exception {
	    return Player.getId(name);
	}

	public static String[] getCharacterIDs(String memberID) throws Exception {
	    return UserCharID.getCharID(memberID);
	}
}