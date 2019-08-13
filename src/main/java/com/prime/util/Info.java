package com.prime.util;

import com.prime.Prime;
import java.util.Date;

public class Info {

    public final static String BOT_DEFAULT_PREFIX = Prime.getConfiguration().getString("prefix");
    public final static String BOT_NAME = Prime.getConfiguration().getString("name");
    public final static String BOT_VERSION = Prime.getConfiguration().getString("version");
    public final static String BOT_GITHUB = Prime.getConfiguration().getString("github");
    public final static String COMMUNITY_SERVER = Prime.getConfiguration().getString("support_server");
    public final static String COMMUNITY_STAFF_ROLE = Prime.getConfiguration().getString("support_staff");
    public final static String PREMIUM_ROLE = Prime.getConfiguration().getString("premium_role");
    public final static String CONFIG_FILE = "config.json";
    public final static String BUNGIE_API_KEY = Prime.getConfiguration().getString("bungie_api_key");
    public final static String CMD_LOG_CHANNEL = Prime.getConfiguration().getString("cmdLogChannel");
    public final static String DEV_CHANNEL_ID = Prime.getConfiguration().getString("dev_channel_id");

    public static final String GITHUB_TOKEN = Prime.getConfiguration().getString("git_token");
    public static Date lastRestart;

    /**
     * Bot author long ids.
     */
    public final static Long[] BOT_AUTHOR_IDS = {
            393847930039173131L  // ROMVoid
    };

    /* MySQL login */
    public final static String MYSQL_HOST = Prime.getConfiguration().getString("mysql_host");
    public final static String MYSQL_PORT = Prime.getConfiguration().getString("mysql_port");
    public final static String MYSQL_USER = Prime.getConfiguration().getString("mysql_user");
    public final static String MYSQL_PASSWORD = Prime.getConfiguration().getString("mysql_password");
    public final static String MYSQL_DATABASE = Prime.getConfiguration().getString("mysql_database");
}
