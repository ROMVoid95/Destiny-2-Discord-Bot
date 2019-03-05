package site.romvoid.forgebot.util;

import java.util.regex.Pattern;

public class Parse {

    public static String nickname(String name) {
        return name.replaceFirst(Pattern.quote(name.substring(name.indexOf("["), name.indexOf("]") + 1)),
                "").replaceAll(" ", "");
    }

}
