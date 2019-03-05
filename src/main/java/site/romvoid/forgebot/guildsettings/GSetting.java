/*
 * Copyright 2019 github.com/ROMVoid95
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

package site.romvoid.forgebot.guildsettings;

import java.util.Collections;
import java.util.HashSet;
import net.dv8tion.jda.core.entities.Guild;
import site.romvoid.forgebot.core.BotConfig;
import site.romvoid.forgebot.guildsettings.types.EnumSettingType;
import site.romvoid.forgebot.guildsettings.types.StringLengthSettingType;

public enum GSetting {
    //General settings
    AUTO_REPLY("false", GuildSettingType.TOGGLE,
            "use the auto reply feature?\n" +
                    "Looks for patterns in messages and replies to them (with a cooldown)\n" +
                    "true -> enable auto replying to matched messages\n" +
                    "false -> disable auto replying",
            GSettingTag.MODERATION),
    BOT_LANGUAGE("en", new EnumSettingType("en", "nl", "de"),
            "The output language of the bot"),
    BOT_CHANNEL("general", GuildSettingType.TEXT_CHANNEL_MANDATORY,
            "Channel where the bots default output goes to",
            GSettingTag.CHANNEL),
    BOT_ADMIN_ROLE("", GuildSettingType.ROLE_OPTIONAL,
            "Users with this role are considered admins for the bot",
            GSettingTag.ROLE),
    SHOW_TEMPLATES(BotConfig.SHOW_KEYPHRASE ? "true" : "false", GuildSettingType.TOGGLE,
            "Show which templates are being used on places.\n\n" +
                    "valid values: \n" +
                    "true       -> Shows the keyphrases being used\n " +
                    "false      -> Shows normal text \n\n" +
                    "for instance if you don't have permission to access a command:\n\n" +
                    "setting this to true would show:\n" +
                    "no_permission\n\n" +
                    "false would show:\n" +
                    "You don't have permission to use that!",
            GSettingTag.DEBUG),
    DEBUG("false", GuildSettingType.TOGGLE,
            "Show some debug information.\n\n" +
                    "valid values: \n" +
                    "true       -> Show a lot more additional information\n " +
                    "false      -> don't \n\n" +
                    "If you want to check if certain things are (not) working.\n\n",
            GSettingTag.DEBUG),
    BOT_UPDATE_WARNING("playing", new EnumSettingType("always", "playing", "off"),
            "Show a warning that there is an update and that the bot will be updating soon.\n" +
                    "always  -> always show the message in the bot's configured default channel\n" +
                    "playing -> only announce when the bot is playing music and in the bot's configured music channel\n" +
                    "off     -> don't announce when the bot is going down for an update",
            GSettingTag.META),
    CLEANUP_MESSAGES("no", new EnumSettingType("yes", "no", "nonstandard"),
            "Delete messages after a while?\n" +
                    "yes         -> Always delete messages\n" +
                    "no          -> Never delete messages\n" +
                    "nonstandard -> delete messages outside of bot's default channel",
            GSettingTag.MODERATION, GSettingTag.META),
    COMMAND_PREFIX(BotConfig.BOT_COMMAND_PREFIX, new StringLengthSettingType(1, 4),
            "Prefix for commands (between 1 and 4 characters)",
            GSettingTag.COMMAND, GSettingTag.MODERATION),
    CHAT_BOT_ENABLED("false", GuildSettingType.TOGGLE,
            "Setting this to true will make it so that it responds to every message in the configured bot_channel",
            GSettingTag.META),
    COMMAND_LOGGING_CHANNEL("false", GuildSettingType.TEXT_CHANNEL_OPTIONAL,
            "The channel command usage will be logged to\n\n" +
                    "Example output:\n" +
                    "romvoid#1909 has used `say` in #general\n" +
                    "arguments: this is not a test\n" +
                    "output: this is not a test\n\n" +
                    "Setting this to 'false' will disable it (without the quotes)\n" +
                    "To enable it, set this setting to match the channel name where you want the command logging to happen\n" +
                    "If you specify an invalid channel, this setting will disable itself",
            GSettingTag.COMMAND, GSettingTag.LOGGING, GSettingTag.CHANNEL),
    BOT_LOGGING_CHANNEL("false", GuildSettingType.TEXT_CHANNEL_OPTIONAL,
            "The channel where the logging of events happens. Such as users joining/leaving\n\n" +
                    "Setting this to 'false' will disable it (without the quotes)\n\n" +
                    "To enable it, set this setting to match the channel name where you want the logging to happen\n" +
                    "If you specify an invalid channel, this setting will disable itself",
            GSettingTag.CHANNEL, GSettingTag.LOGGING),
    BOT_MODLOG_CHANNEL("false", GuildSettingType.TEXT_CHANNEL_OPTIONAL,
            "The channel where mod-logging happens.\n" +
                    "A case will appear if a user has been banned/kicked/warned/muted\n\n" +
                    "Setting this to 'false' will disable it (without the quotes)\n\n" +
                    "To enable it, set this setting to match the channel name where you want the moderation-cases to go\n" +
                    "If you specify an invalid channel, this setting will disable itself",
            GSettingTag.CHANNEL, GSettingTag.LOGGING),
    BOT_MUTE_ROLE("false", GuildSettingType.ROLE_OPTIONAL,
            "This is the role which is applied to those who you use the mute command on\n\n" +
                    "Setting this value to false will disable the role applied with the mute command",
            GSettingTag.MODERATION, GSettingTag.ROLE),
    HELP_IN_PM("false", GuildSettingType.TOGGLE,
            "show help in a private message?\n" +
                    "true  -> send a message to the user requesting help\n" +
                    "false -> output help to the channel where requested",
            GSettingTag.META),
    PM_USER_EVENTS("false", GuildSettingType.TOGGLE,
            "Send a private message to owner when something happens to a user?\n" +
                    "true  -> sends a private message to guild-owner\n" +
                    "false -> does absolutely nothing",
            GSettingTag.META, GSettingTag.MODERATION),
    SHOW_UNKNOWN_COMMANDS("false", GuildSettingType.TOGGLE,
            "Show message on nonexistent commands and blacklisted commands\n" +
                    "true -> returns a help message\n" +
                    "false -> stays silent",
            GSettingTag.META),
    WELCOME_NEW_USERS("false", GuildSettingType.TOGGLE,
            "Show a welcome message to new users?\n" +
                    "Valid options:\n" +
                    "true  -> shows a welcome when a user joins or leaves the guild\n" +
                    "false -> Disabled, doesn't say anything\n\n" +
                    "The welcome message can be set with the template: \n" +
                    "welcome_new_user\n\n" +
                    "The welcome back message can be set with the template (if the user had joined before): \n" +
                    "welcome_back_user\n\n" +
                    "The leave message can be set with the template: \n" +
                    "message_user_leaves\n\n" +
                    "If multiple templates are set a random one will be chosen\n" +
                    "See the template command for more details",
            GSettingTag.META),;

    private final String defaultValue;
    private final IGuildSettingType settingType;
    private final String description;
    private final HashSet<GSettingTag> tags;

    GSetting(String defaultValue, IGuildSettingType settingType, String description, GSettingTag... tags) {

        this.defaultValue = defaultValue;
        this.settingType = settingType;
        this.description = description;
        this.tags = new HashSet<>();
        Collections.addAll(this.tags, tags);
    }

    public boolean isInternal() {
        return tags.contains(GSettingTag.INTERNAL);
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getDescription() {
        return description;
    }

    public boolean hasTag(String tag) {
        return tags.contains(GSettingTag.valueOf(tag));
    }

    public boolean hasTag(GSettingTag tag) {
        return tags.contains(tag);
    }

    public HashSet<GSettingTag> getTags() {
        return tags;
    }

    /**
     * Checks if the value is a valid setting
     *
     * @param input value to check
     * @return is it a valid value
     */
    public boolean isValidValue(Guild guild, String input) {
        return settingType.validate(guild, input);
    }

    public String getValue(Guild guild, String input) {
        return settingType.fromInput(guild, input);
    }

    public String toDisplay(Guild guild, String value) {
        return settingType.toDisplay(guild, value);
    }

    public IGuildSettingType getSettingType() {
        return settingType;
    }
}
