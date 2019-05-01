package site.romvoid.forgebot.util;

import java.util.Date;
import net.dv8tion.jda.core.entities.Game;

public class STATIC {

	public static final String BOTNAME = "PrimeBot";
	
                      //SET THE BOTS PREFIX
    public static final String PREFIX = "~";
    
                      //CURRENT VERSION
    public static final String VERSION = "2.1.1";
    
                      //CUSTOM DISCORD PRESENCE MESSAGE
    public static String CUSTOM_MESSAGE = "";
    
                      //SET THE BOTS PRESENCEE
    public static Game GAME = Game.playing("v" + VERSION);
    
                      //DISCORD SERVER STAFF ROLES BY NAME
    public static final String[] STAFF = {"Founder", "Admins", "Head Admin", "Admin", "Head Moderator", "Moderator", "Retired Staff"};
                      //BOTS OWNER BY DISCORD ID
    public static final String[] OWNERID = {"393847930039173131"};
    public static final String OWNER = "ROMVoid";
    public static final String GITHUB_LINK = "https://github.com/ROMVoid95/Destiny-2-Discord-Bot";
    
    public static String input;
    public static int reconnectCount = 0;
    public static Date lastRestart;
    public static final String HOST = SECRETS.SECRET.getValue("db_host").toString();
    public static final String PORT = SECRETS.SECRET.getValue("db_port").toString();
    public static final String PASSWORD = SECRETS.SECRET.getValue("db_pass").toString();
    public static final String DATABASE = SECRETS.SECRET.getValue("db_name").toString();
    public static final String USERNAME = SECRETS.SECRET.getValue("db_user").toString();




}
