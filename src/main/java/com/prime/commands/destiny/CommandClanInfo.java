package com.prime.commands.destiny;

import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager.ParsedCommandInvocation;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import com.prime.util.destiny.Player;
import com.prime.util.destiny.clan.ClanTag;
import java.awt.Color;
import java.io.IOException;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

public class CommandClanInfo extends CommandHandler {

  private static String clanName;
  private static String memCount;
  private static String clanMotto;
  private static String userRank;
  private static String sign;
  private static String join;

  public CommandClanInfo() {
    super(new String[] {"clan"}, CommandCategory.DESTINY,
        new PermsNeeded("command.raidreport", false, false), "Retreive basic clan info",
        "<battle.netID>");
  }

  @Override
  protected Message execute(ParsedCommandInvocation parsedCommandInvocation,
      UserPermissions userPermissions) {
    String[] args = parsedCommandInvocation.getArgs();
    Member member = parsedCommandInvocation.getMember();
    MessageChannel channel = (MessageChannel) parsedCommandInvocation.getTextChannel();

    String nickname = null;

    if (args.length > 0) {
      String name = args[0];
      if (name.contains("["))
        nickname = Player.removeBrackets(name);
      else
        nickname = args[0];

    } else {
      String name = member.getEffectiveName();
      if (name.contains("["))
        nickname = Player.removeBrackets(name);
      else
        nickname = name;
    }
    String memberId = Player.getId(nickname);

    try {
      clanName = ClanTag.getName(memberId);
      memCount = ClanTag.memberCount(memberId);
      clanMotto = ClanTag.clanMoto(memberId);
      userRank = ClanTag.getUserRank(memberId);
      sign = ClanTag.callSign(memberId);
      join = ClanTag.join(memberId);
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (clanName != null) {
      channel.sendMessage(
          new EmbedBuilder().setColor(Color.green).setThumbnail("https://i.imgur.com/gAXSriS.jpg")
              .setTitle(nickname + " Clan Information \n ")
              .addField("**" + clanName + "** [" + sign + "] \n", "*" + clanMotto + "* \n\n "
                  + "**Members**: " + memCount + "\n **Membership**: " + join, true)
              .addField("**Rank**", userRank + " \n", true).build())
          .queue();
    } else {
      channel
          .sendMessage(new EmbedBuilder().setColor(Color.red)
              .setThumbnail("https://i.imgur.com/gAXSriS.jpg")
              .setTitle(nickname + " Doesn't appear to be in a clan \n ")
              .setDescription("If this is incorrect please check your discord nickname").build())
          .queue();
    }


    return null;

  }
}

