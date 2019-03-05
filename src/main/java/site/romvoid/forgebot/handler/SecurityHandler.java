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

package site.romvoid.forgebot.handler;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.utils.MiscUtil;
import net.dv8tion.jda.core.utils.PermissionUtil;
import site.romvoid.forgebot.core.BotConfig;
import site.romvoid.forgebot.db.controllers.CGuild;
import site.romvoid.forgebot.db.controllers.CUser;
import site.romvoid.forgebot.db.controllers.CUserRank;
import site.romvoid.forgebot.db.model.OGuild;
import site.romvoid.forgebot.db.model.OUserRank;
import site.romvoid.forgebot.guildsettings.GSetting;
import site.romvoid.forgebot.util.RankSystem;

/**
 * Manages permissions/bans for discord
 */
public class SecurityHandler {
    private final static HashSet<Long> bannedGuilds = new HashSet<>();
    private final static HashSet<Long> bannedUsers = new HashSet<>();
    private final static HashSet<Long> interactionBots = new HashSet<>();
    private final static HashSet<Long> contributors = new HashSet<>();
    private final static HashSet<Long> botAdmins = new HashSet<>();
    private final static HashSet<Long> systemAdmins = new HashSet<>();

    public SecurityHandler() {
    }

    public static synchronized void initialize() {
        bannedGuilds.clear();
        bannedUsers.clear();
        interactionBots.clear();
        contributors.clear();
        botAdmins.clear();
        systemAdmins.clear();
        List<OGuild> bannedList = CGuild.getBannedGuilds();
        bannedGuilds.addAll(bannedList.stream().map(guild -> guild.discord_id).collect(Collectors.toList()));
        CUser.addBannedUserIds(bannedUsers);

        List<OUserRank> interaction_bots = CUserRank.getUsersWith(CRank.findBy("INTERACTION_BOT").id);
        List<OUserRank> contributor = CUserRank.getUsersWith(CRank.findBy("CONTRIBUTOR").id);
        List<OUserRank> bot_admin = CUserRank.getUsersWith(CRank.findBy("BOT_ADMIN").id);
        List<OUserRank> system_admin = CUserRank.getUsersWith(CRank.findBy("SYSTEM_ADMIN").id);
        contributors.addAll(contributor.stream().map(oUserRank -> CUser.getCachedDiscordId(oUserRank.userId)).collect(Collectors.toList()));
        interactionBots.addAll(interaction_bots.stream().map(oUserRank -> CUser.getCachedDiscordId(oUserRank.userId)).collect(Collectors.toList()));
        botAdmins.addAll(bot_admin.stream().map(oUserRank -> CUser.getCachedDiscordId(oUserRank.userId)).collect(Collectors.toList()));
        systemAdmins.addAll(system_admin.stream().map(oUserRank -> CUser.getCachedDiscordId(oUserRank.userId)).collect(Collectors.toList()));
    }

    public boolean isInteractionBot(long userId) {
        return interactionBots.contains(userId);
    }

    public boolean isBanned(User user) {
        return bannedUsers.contains(user.getIdLong());
    }

    public synchronized void addUserBan(long discordId) {
        if (!bannedUsers.contains(discordId)) {
            bannedUsers.add(discordId);
        }
    }

    public synchronized void removeUserBan(long discordId) {
        if (bannedUsers.contains(discordId)) {
            bannedUsers.remove(discordId);
        }
    }

    public boolean isBanned(Guild guild) {
        return bannedGuilds.contains(guild.getIdLong());
    }

    public RankSystem getRankSystem(User user) {
        return getRankSystemForGuild(user, null);
    }

    public RankSystem getRankSystem(User user, MessageChannel channel) {
        if (channel instanceof TextChannel) {
            return getRankSystemForGuild(user, ((TextChannel) channel).getGuild());
        }
        return getRankSystemForGuild(user, null);
    }

    /**
     * Try and figure out what type of guild it is
     *
     * @param guild the guild to check
     * @return what category the guild is labeled as
     */
    public GuildCheckResult checkGuild(Guild guild) {

        int bots = 0;
        int users = 0;
        if (MiscUtil.getCreationTime(guild.getOwner().getUser()).isBefore(OffsetDateTime.now().minusDays(BotConfig.GUILD_OWNER_MIN_ACCOUNT_AGE))) {
            return GuildCheckResult.OWNER_TOO_NEW;
        }
        for (Member user : guild.getMembers()) {
            if (user.getUser().isBot()) {
                bots++;
            }
            users++;
        }
        if ((double) bots / users > BotConfig.GUILD_MAX_USER_BOT_RATIO) {
            return GuildCheckResult.BOT_GUILD;
        }
        if (users < BotConfig.GUILD_MIN_USERS) {
            return GuildCheckResult.TEST_GUILD;
        }
        return GuildCheckResult.OKE;
    }

    public RankSystem getRankSystemForGuild(User user, Guild guild) {
        long userId = user.getIdLong();
        if (user.getIdLong() == BotConfig.CREATOR_ID) {
            return RankSystem.CREATOR;
        }
        if (user.isBot()) {
            if (interactionBots.contains(userId)) {
                return RankSystem.INTERACTION_BOT;
            }
            return RankSystem.BOT;
        }
        if (systemAdmins.contains(userId)) {
            return RankSystem.SYSTEM_ADMIN;
        }
        if (botAdmins.contains(userId)) {
            return RankSystem.BOT_ADMIN;
        }
        if (contributors.contains(userId)) {
            return RankSystem.CONTRIBUTOR;
        }
        if (bannedUsers.contains(userId)) {
            return RankSystem.BANNED_USER;
        }
        if (guild != null) {
            if (guild.getOwner().equals(user)) {
                return RankSystem.GUILD_OWNER;
            }
            if (PermissionUtil.checkPermission(guild.getMember(user), Permission.ADMINISTRATOR)) {
                return RankSystem.GUILD_ADMIN;
            }
            Role role = GuildSettings.get(guild).getRoleValue(GSetting.BOT_ADMIN_ROLE, guild);
            if (role != null && guild.getMember(user).getRoles().contains(role)) {
                return RankSystem.GUILD_BOT_ADMIN;
            }
        }
        return RankSystem.USER;
    }

    public boolean isBotAdmin(long discordUserId) {
        return botAdmins.contains(discordUserId);
    }
}
