package site.romvoid.forgebot.util;

import site.romvoid.forgebot.core.Configuration;

public class SECRETS {

    public static final Configuration SECRET = new Configuration("secrets.json");
    public static String token = SECRET.getValue("token").toString();
    public static String password = SECRET.getValue("db_pass").toString();
    
}
