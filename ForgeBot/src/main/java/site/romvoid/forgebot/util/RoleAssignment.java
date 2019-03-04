package site.romvoid.forgebot.util;

import java.io.IOException;
import java.util.regex.Pattern;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import site.romvoid.forgebot.util.destiny.Codes;
import site.romvoid.forgebot.util.destiny.RaidReport;
import site.romvoid.forgebot.util.destiny.UserMemberID;
import site.romvoid.forgebot.util.destiny.clan.ClanTag;
import site.romvoid.forgebot.util.destiny.raids.RaidStats;
import site.romvoid.forgebot.util.destiny.raids.ReqRaids;
import site.romvoid.forgebot.util.exemptions.IndexExemption;

public class RoleAssignment {
    
    public static void action(MessageReactionAddEvent event) throws Exception {
        String name = event.getMember().getEffectiveName();
        String nickname = null;
        if (name.contains("["))
            nickname = name.replaceFirst(
                    Pattern.quote(name.substring(name.indexOf("["), name.indexOf("]") + 1)), "")
                    .replaceAll(" ", "");
        else
            nickname = event.getMember().getEffectiveName();
        Member user = event.getMember();
        String memId = UserMemberID.getMemberId(nickname);
 
        Guild guild = event.getGuild();
        Role veteranRaider;
        Role experiencedRaider;
        Role prestigeRaider;
        Role raider;
        Role guardian;
        Role clanRecruiter;
        Role challenger;
        Role masteryRanks;
        Role raidRoles;
        Role misc;
        Role mColor;

        guardian = guild.getRoleById("384401044987183106");
        raider = guild.getRoleById("385062829784432642");
        prestigeRaider = guild.getRoleById("385050190530347008");
        experiencedRaider = guild.getRoleById("526570953145319424");
        veteranRaider = guild.getRoleById("526571539869597716");
        clanRecruiter = guild.getRoleById("400157331327025152");
        challenger = guild.getRoleById("527067149353877504");
        masteryRanks = guild.getRoleById("530730967657086976");
        raidRoles = guild.getRoleById("530728549762596864");
        misc = guild.getRoleById("530736433665933323");
        mColor = guild.getRoleById("542755267343941642");

        if (memId != null) {
            RaidStats rs = ReqRaids.collectAllRaidStats(nickname);

            if (getDesignatedRaiderRole(rs) == Codes.Raider) {
                guild.getController().addSingleRoleToMember(user, raider).queue();
                guild.getController().addSingleRoleToMember(user, guardian).queue();
                guild.getController().addSingleRoleToMember(user, raidRoles).queue();
            }
            if (getDesignatedRaiderRole(rs) == Codes.ExperiencedRaider) {
                guild.getController().addSingleRoleToMember(user, experiencedRaider).queue();
                guild.getController().addSingleRoleToMember(user, guardian).queue();
                guild.getController().addSingleRoleToMember(user, raidRoles).queue();
            }
            if (getDesignatedRaiderRole(rs) == Codes.VeteranRaider) {
                guild.getController().addSingleRoleToMember(user, veteranRaider).queue();
                guild.getController().addSingleRoleToMember(user, guardian).queue();
                guild.getController().addSingleRoleToMember(user, raidRoles).queue();
            }
            if (getPrestigeRaiderRole(rs) == Codes.PrestigeRaider) {
                guild.getController().addSingleRoleToMember(user, prestigeRaider).queue();
            }
            if (getClanMemberRank(event) == Codes.clanRecruiter) {
                guild.getController().addSingleRoleToMember(user, clanRecruiter).queue();
                guild.getController().addSingleRoleToMember(user, misc).queue();
            }
            if (getRaidReportRank(event) == "HasChallenger") {
                guild.getController().addSingleRoleToMember(user, challenger).queue();
                guild.getController().addSingleRoleToMember(user, masteryRanks).queue();
                guild.getController().addSingleRoleToMember(user, mColor ).queue();
            } else {
                guild.getController().addSingleRoleToMember(user, guardian).queue();
                guild.getController().addSingleRoleToMember(user, misc).queue();
            }
        }
    }

    public static int getDesignatedRaiderRole(RaidStats rs) {

        int totalCompletions = 0;
        totalCompletions += rs.getTotalCompletion();

        if (totalCompletions >= 200) {
            return Codes.VeteranRaider;
        } else if (totalCompletions >= 75) {
            return Codes.ExperiencedRaider;
        } else if (totalCompletions >= 10)
            return Codes.Raider;

        return Codes.Guardian;
    }

    public static int getPrestigeRaiderRole(RaidStats rs) {

        int totalPrestige = 0;
        totalPrestige += rs.getLev().getPrestigeCompletions();
        totalPrestige += rs.getEow().getPrestigeCompletions();
        totalPrestige += rs.getSos().getPrestigeCompletions();

        if (totalPrestige >= 10) {
            return Codes.PrestigeRaider;
        } else {
            return Codes.Guardian;
        }

    }

    public static int getClanMemberRank(MessageReactionAddEvent event)
            throws IndexExemption, IOException {
        String name = event.getMember().getEffectiveName();
        String nickname = null;
        if (name.contains("["))
            nickname = name.replaceFirst(
                    Pattern.quote(name.substring(name.indexOf("["), name.indexOf("]") + 1)), "")
                    .replaceAll(" ", "");
        else
            nickname = event.getMember().getEffectiveName();
        String id = UserMemberID.getMemberId(nickname);
        String rank = ClanTag.getUserRank(id);

        if (rank == "Founder") {
            return Codes.clanRecruiter;
        } else if (rank == "Acting Founder")
            return Codes.clanRecruiter;
        else {
            return Codes.Guardian;
        }

    }

    public static String getRaidReportRank(MessageReactionAddEvent event) throws IOException {
        String name = event.getMember().getEffectiveName();
        String nickname = null;
        if (name.contains("["))
            nickname = name.replaceFirst(
                    Pattern.quote(name.substring(name.indexOf("["), name.indexOf("]") + 1)), "")
                    .replaceAll(" ", "");
        else
            nickname = event.getMember().getEffectiveName();
        String id = UserMemberID.getMemberId(nickname);
        boolean clear = RaidReport.getClearsRank(id).contains("Challenger");
        boolean speed = RaidReport.getSpeedRank(id).contains("Challenger");

        if (speed == true || clear == true) {
            String HasChallenger = "HasChallenger";
            return HasChallenger;
        } else {
            String NoChallenger = "NotChallenger";
            return NoChallenger;
        }

    }

    public static int getServerRoles() {
        return Codes.Guardian;
    }

    public static RaidStats getRaidStats(MessageReactionAddEvent event) {
        return ReqRaids.collectAllRaidStats(event.getMember().getEffectiveName());
    }
}
