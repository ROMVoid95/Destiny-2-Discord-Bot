/*
 * Copyright 2017 github.com/ROMVoid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package site.romvoid.forgebot.core;

public class BotConfig {

    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36";
    //the bot/users ratio for guilds
    public static final double GUILD_MAX_USER_BOT_RATIO = 0.75D;
    //the minimum age of the guild owner's account in days
    public static final long GUILD_OWNER_MIN_ACCOUNT_AGE = 7;
    //if a guild has less users it will be marked as a test guild
    public static final int GUILD_MIN_USERS = 5;
    public static final int MAX_MESSAGE_SIZE = 2000;
    //the default time to delete messages after milliseconds
    public static long DELETE_MESSAGES_AFTER = 120_000;

    public static String TEMPLATE_QUOTE = "%";
    //bot enabled? must be set to true in order to run
    public static boolean BOT_ENABLED = true;

    public static boolean SUBSCRIBE_UNSUB_ON_NOT_FOUND = false;

    public static boolean BOT_RESTART_INACTIVE_SHARDS = false;
    
    //the website of the bot
    public static String BOT_WEBSITE = "";
    public static String BOT_ENV = "test";

    //if you want to use graylog
    public static boolean BOT_GRAYLOG_ACTIVE = false;
    public static String BOT_GRAYLOG_HOST = "10.120.34.139";
    public static int BOT_GRAYLOG_PORT = 12202;

    public static boolean BOT_AUTO_UPDATE = false;

    //display name of the bot
    public static String BOT_NAME = "LFGBot";

    //Bot's own discord server
    public static String BOT_GUILD_ID = "538530739017220107";

    //Bot's own channel on its own server
    public static String BOT_CHANNEL_ID = "551698160435593256";

    //Bot's error channel id
    public static String BOT_ERROR_CHANNEL_ID = "551698900759871489";

    //Bot's status update
    public static String BOT_STATUS_CHANNEL_ID = "551776849181802508";

    //token used to login to discord
    public static String BOT_TOKEN = "NTQzNDg5NzIyODQwOTA3Nzky.D118ig.PMqIcOEX6o4hn73RUuVuTSi5iiA";

    //prefix for all commands !help etc.
    public static boolean BOT_CHATTING_ENABLED = true;

    //default prefix to mark messages as commands
    public static String BOT_COMMAND_PREFIX = ">";

    //save the usage of commands?
    public static boolean BOT_COMMAND_LOGGING = true;

    //show keyphrases?
    public static boolean SHOW_KEYPHRASE = true;

    //Reply to non existing commands?
    public static boolean BOT_COMMAND_SHOW_UNKNOWN = true;
    
    //mysql hostname
    public static String DB_HOST = "localhost";

    //mysql port
    public static int DB_PORT = 3306;

    //mysql user
    public static String DB_USER = "root";

    //mysql password
    public static String DB_PASS = "";

    //mysql database name
    public static String DB_NAME = "lfgbot";

    public static boolean TRELLO_ACTIVE = true;

    //Use trello integration
    public static String TRELLO_API_KEY = "335a7df700768e6515fa9d01b55b6d13";

    public static String TRELLO_BOARD_ID = "5c00bb3dd56ec982338e905d";

    public static String TRELLO_LIST_BUGS = "5c00bb3dd56ec982338e905e";

    public static String TRELLO_LIST_IN_PROGRESS = "5c00bb3dd56ec982338e905f";

    public static String TRELLO_LIST_PLANNED = "5c00bb3dd56ec982338e9060";

    //the trello token
    public static String TRELLO_TOKEN = "a1b45c322c1d83447686f7944a99818c97898cfee6094500681c587be4bd71e1";

    public static long CREATOR_ID = 393847930039173131L;
}
