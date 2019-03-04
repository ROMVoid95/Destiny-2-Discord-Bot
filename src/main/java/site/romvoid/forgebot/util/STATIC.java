package site.romvoid.forgebot.util;

import java.util.Date;
import net.dv8tion.jda.core.entities.Game;

public class STATIC {

                      //SET THE BOTS PREFIX
    public static final String prefix = "PREFIX";
    
                      //CURRENT VERSION
    public static final String VERSION = "version";
    
                      //CUSTOM DISCORD PRESENCE MESSAGE
    public static String CUSTOM_MESSAGE = "message";
    
                      //SET THE BOTS PRESENCEE
    public static Game GAME = Game.playing(CUSTOM_MESSAGE + " | v." + VERSION);
    
                      //DISCORD SERVER STAFF ROLES BY NAME
    public static final String[] STAFF = {"ROLE1", "ROLE2", "ROLE3", "ROLE4", "ETC"};
                      //BOTS OWNER BY DISCORD ID
    public static final String[] OWNERS = {"OWNER ID"};
    public static String input;
    public static int reconnectCount = 0;
    public static Date lastRestart;
    public static final String HOST = SECRETS.SECRET.getValue("db_host").toString();
    public static final String PORT = SECRETS.SECRET.getValue("db_port").toString();
    public static final String PASSWORD = SECRETS.SECRET.getValue("db_pass").toString();
    public static final String DATABASE = SECRETS.SECRET.getValue("db_name").toString();
    public static final String USERNAME = SECRETS.SECRET.getValue("db_user").toString();




}
