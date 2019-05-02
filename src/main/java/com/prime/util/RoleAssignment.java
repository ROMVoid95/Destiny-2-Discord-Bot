package com.prime.util;

import com.prime.sql.MySQL;
import com.prime.util.destiny.Codes;
import com.prime.util.destiny.Player;
import com.prime.util.destiny.RaidReport;
import com.prime.util.destiny.clan.ClanTag;
import com.prime.util.destiny.raids.RaidStats;
import com.prime.util.destiny.raids.ReqRaids;
import com.prime.util.excemptions.PlayerNotFound;
import java.io.IOException;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.managers.GuildController;

public class RoleAssignment {

	private static GuildController control;
	private static RaidStats rs;
	private static Member member;
	static Role guardian;
	static Role raider;
	static Role prestigeRaider;
	static Role experiencedRaider;
	static Role veteranRaider;
	static Role clanRecruiter;
	static Role challenger;
	static Role masteryRanks;
	static Role raiderRoles;
	static Role misc;

	public static void action(MessageReactionAddEvent event) {

		// define class constants
		Guild guild = event.getGuild();
		control = guild.getController();
		member = event.getMember();
		// define user for PrivateMessage
		User user = event.getUser();
		String discordid = user.getId();
		// nickname to parse
		String nickname = member.getEffectiveName();
		String id = null;
		String parsedName;

		if (!nickname.contains("#")) {
			System.out.println(nickname);
			embedSender.nicknameExcemption(user, nickname);
		}

		if (nickname.contains("#")) {
			if (Player.containsBrackets(nickname)) {
				parsedName = Player.removeBrackets(nickname);
				try {
					id = Player.getId(parsedName);
					if (!MySQL.ifMemberExists(user)) {
						MySQL.createUser(discordid, parsedName, id);
					}
					rs = ReqRaids.collectAllRaidStats(parsedName);
					getRaidReportRank(id);
					giveRoles(rs, id, guild);
				} catch (PlayerNotFound pnf) {
					embedSender.playerNotFound(user, parsedName);
				}
			}
		}
		if (!Player.containsBrackets(nickname)) {
			parsedName = nickname;
			try {
				id = Player.getId(parsedName);
				if (!MySQL.ifMemberExists(user)) {
					MySQL.createUser(discordid, parsedName, id);
				}
				rs = ReqRaids.collectAllRaidStats(parsedName);
				getRaidReportRank(id);
				giveRoles(rs, id, guild);
			} catch (PlayerNotFound pnf) {
				embedSender.playerNotFound(user, parsedName);
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
		} else if (totalCompletions >= 10) {
			return Codes.Raider;
		} else
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
			return Codes.ReturnNone;
		}

	}

	public static int getClanMemberRank(String id) {

		String rank = null;
		try {
			rank = ClanTag.getUserRank(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (rank == "Founder") {
			return Codes.clanRecruiter;
		} else if (rank == "Acting Founder") {
			return Codes.clanRecruiter;
		} else if (rank == "Admin")
			return Codes.clanRecruiter;
		else {
			return Codes.ReturnNone;
		}
	}

	public static String getRaidReportRank(String id) {

		boolean clear = false;
		boolean speed = false;
		try {
			clear = RaidReport.getClearsRank(id).contains("Challenger");
			speed = RaidReport.getSpeedRank(id).contains("Challenger");

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (speed == true || clear == true) {
			String HasChallenger = "HasChallenger";
			return HasChallenger;
		} else {
			String NoChallenger = "NotChallenger";
			return NoChallenger;
		}

	}

	private static void giveRoles(RaidStats rs, String id, Guild guild) {

		// Server Roles
		guardian = guild.getRoleById("384401044987183106");
		raider = guild.getRoleById("385062829784432642");
		prestigeRaider = guild.getRoleById("385050190530347008");
		experiencedRaider = guild.getRoleById("526570953145319424");
		veteranRaider = guild.getRoleById("526571539869597716");
		clanRecruiter = guild.getRoleById("400157331327025152");
		challenger = guild.getRoleById("527067149353877504");

		// Role Dividers
		masteryRanks = guild.getRoleById("530730967657086976");
		raiderRoles = guild.getRoleById("530728549762596864");
		misc = guild.getRoleById("531873048660541471");

		if (getDesignatedRaiderRole(rs) == Codes.Raider) {
			control.addSingleRoleToMember(member, raider).queue();
			control.addSingleRoleToMember(member, guardian).queue();
			control.addSingleRoleToMember(member, raiderRoles).queue();
		}
		if (getDesignatedRaiderRole(rs) == Codes.ExperiencedRaider) {
			control.addSingleRoleToMember(member, experiencedRaider).queue();
			control.addSingleRoleToMember(member, guardian).queue();
			control.addSingleRoleToMember(member, raiderRoles).queue();
		}
		if (getDesignatedRaiderRole(rs) == Codes.VeteranRaider) {
			control.addSingleRoleToMember(member, veteranRaider).queue();
			control.addSingleRoleToMember(member, guardian).queue();
			control.addSingleRoleToMember(member, raiderRoles).queue();
		}
		if (getPrestigeRaiderRole(rs) == Codes.PrestigeRaider) {
			control.addSingleRoleToMember(member, prestigeRaider).queue();
			control.addSingleRoleToMember(member, raiderRoles).queue();
		}
		if (getClanMemberRank(id) == Codes.clanRecruiter) {
			control.addSingleRoleToMember(member, clanRecruiter).queue();
			control.addSingleRoleToMember(member, misc).queue();
		}
		if (getRaidReportRank(id) == "HasChallenger") {
			control.addSingleRoleToMember(member, challenger).queue();
			control.addSingleRoleToMember(member, masteryRanks).queue();
		} else {
			control.addSingleRoleToMember(member, guardian).queue();
			control.addSingleRoleToMember(member, misc).queue();
		}
	}
}
