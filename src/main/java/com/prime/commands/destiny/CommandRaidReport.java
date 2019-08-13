package com.prime.commands.destiny;

import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager.ParsedCommandInvocation;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import com.prime.util.Colors;
import com.prime.util.embedSender;
import com.prime.util.destiny.Player;
import com.prime.util.destiny.RaidReport;
import java.awt.Color;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

public class CommandRaidReport extends CommandHandler{

  private static String ch = "Challenger";
  private static String clears;
  private static String speed;

    
	public CommandRaidReport() {
		super(new String[] {"rreport", "rr"}, CommandCategory.DESTINY, new PermsNeeded("command.raidreport", false, false), "Retreive current raid.report Ranks", "<battle.netID>");	
	}

	@Override
	protected Message execute(ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
      String[] args = parsedCommandInvocation.getArgs();
      Member member = parsedCommandInvocation.getMember();
      MessageChannel channel = (MessageChannel)parsedCommandInvocation.getTextChannel();
	  
	  String nickname = null;
      Color color = null;

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
      String id = Player.getId(nickname);

          if (id == null) {
              embedSender.noId(parsedCommandInvocation);
          }
          
          if (id != null) {
              if (id != null) {
                  try {
                    clears = RaidReport.getClearsRank(id);
                    speed = RaidReport.getSpeedRank(id);
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                  if (clears.contains("Challenger") || speed.contains("Challenger"))
                      color = Colors.Challenger;
                  if (clears.contains("Diamond") || speed.contains("Diamond"))
                      color = Colors.Diamond;
                  if (clears.contains("Platinum") || speed.contains("Platinum"))
                      color = Colors.Platinum;
                  if (clears.contains("Gold") || speed.contains("Gold"))
                      color = Colors.Gold;
                  if (clears.contains("Silver") || speed.contains("Silver"))
                      color = Colors.Silver;
                  if (clears.contains("Bronze") || speed.contains("Bronze"))
                      color = Colors.Bronze;

              if (clears.contains(ch) && !speed.contains(ch)) {
                  String[] splitClears = clears.split(",", 2);
                  String clearsRank = splitClears[0];
                  String clearsBody = splitClears[1];
                  channel.sendMessage(new EmbedBuilder()
                          .setColor(color)
                          .setTitle("**Raid.Report Ranks For**")
                          .setDescription("*" + nickname + "*  ")
                          .appendDescription("  [**Link**](https://raid.report/pc/" + id + ")")
                          .addBlankField(false).setThumbnail("https://i.imgur.com/JWCUxyx.png")
                          .addField("Full Clears Rank #" + clearsRank, clearsBody, true)
                          .addField("Speed Rank", speed, true).addBlankField(false)
                          .setFooter("Provided by © 2019 DestinyRaidReport",
                                  "https://i.imgur.com/JWCUxyx.png")
                          .build()).queue();

              } else if (speed.contains(ch) && !clears.contains(ch)) {
                  String[] splitSpeed = speed.split(",", 2);
                  String speedRank = splitSpeed[0];
                  String speedBody = splitSpeed[1];
                  channel.sendMessage(new EmbedBuilder()
                          .setColor(color)
                          .setTitle("**Raid.Report Ranks For**")
                          .setDescription("*" + nickname + "*  ")
                          .appendDescription("  [**Link**](https://raid.report/pc/" + id + ")")
                          .addBlankField(false).setThumbnail("https://i.imgur.com/JWCUxyx.png")
                          .addField("Full Clears Rank", clears, true)
                          .addField("Speed Rank #" + speedRank, speedBody, true).addBlankField(false)
                          .setFooter("Provided by © 2019 DestinyRaidReport",
                                  "https://i.imgur.com/JWCUxyx.png")
                          .build()).queue();
                  
              } else if (speed.contains(ch) && (clears.contains(ch))) {
                  String[] splitSpeed = speed.split(",", 2);
                  String speedRank = splitSpeed[0];
                  String speedBody = splitSpeed[1];
                  String[] splitClears = clears.split(",", 2);
                  String clearsRank = splitClears[0];
                  String clearsBody = splitClears[1];
                  channel.sendMessage(new EmbedBuilder()
                          .setColor(color)
                          .setTitle("**Raid.Report Ranks For**")
                          .setDescription("*" + nickname + "*  ")
                          .appendDescription("  [**Link**](https://raid.report/pc/" + id + ")")
                          .addBlankField(false).setThumbnail("https://i.imgur.com/JWCUxyx.png")
                          .addField("Full Clears Rank #" + clearsRank, clearsBody, true)
                          .addField("Speed Rank #" + speedRank, speedBody, true).addBlankField(false)
                          .setFooter("Provided by © 2019 DestinyRaidReport",
                                  "https://i.imgur.com/JWCUxyx.png")
                          .build()).queue();
                  
              } else {
                channel.sendMessage(new EmbedBuilder()
                          .setColor(color)
                          .setTitle("**Raid.Report Ranks For**")
                          .setDescription("*" + nickname + "*  ")
                          .appendDescription("  [**Link**](https://raid.report/pc/" + id + ")")
                          .addBlankField(false).setThumbnail("https://i.imgur.com/JWCUxyx.png")
                          .addField("Full Clears Rank", clears, true)
                          .addField("Speed Rank", speed, true).addBlankField(false)
                          .setFooter("Provided by © 2019 DestinyRaidReport",
                                  "https://i.imgur.com/JWCUxyx.png")
                          .build()).queue();
              }
          }
      }


		return null;
	}

}
