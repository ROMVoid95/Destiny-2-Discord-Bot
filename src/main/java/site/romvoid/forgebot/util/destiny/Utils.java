package site.romvoid.forgebot.util.destiny;

import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class Utils {
	public static boolean printJson = false;

	// Prints Json content in a pretty way
	public static void printJSon(JsonObject json) {
		if (printJson) {
			Gson gson = new Gson();
			gson = new GsonBuilder().setPrettyPrinting().create();
			String str = gson.toJson(json);
			System.out.println("\n" + str);
		}
	}

	// Passes the nickname through various rules to check that it's suitable
	public static boolean isValidateNickname(String name) {
		// Refuse empty input
	    
		if (name.length() < 1) {
		        
		        System.out.println("No name found");
			return false;
		}

		// Battle.net need #
		if (!name.contains("#")) {
			System.out.println("Doesn't contain a '#'");
			return false;
		}

		// Needs correct regional information
		if (countCharOccurances(name, "[") + countCharOccurances(name, "]") != 2) {
			System.out.println("Square Brackets are not as expected");
			return false;
		}

		// All above have passed
		return true;
	}

	public static int countCharOccurances(String text, String c) {
		return text.length() - text.replaceAll(Pattern.quote(c), "").length();
	}

}
