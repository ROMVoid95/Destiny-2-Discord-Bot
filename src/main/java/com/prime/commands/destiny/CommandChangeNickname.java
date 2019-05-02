package com.prime.commands.destiny;

import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager.ParsedCommandInvocation;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import com.prime.sql.UserSQL;
import com.prime.util.EmbedUtil;
import com.prime.util.StringUtil;
import com.prime.util.embedSender;
import com.prime.util.destiny.Player;

import java.util.regex.Pattern;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

public class CommandChangeNickname extends CommandHandler {
	
	private static final Pattern discordId = Pattern.compile("(\\d{9,})");

  public CommandChangeNickname() {
    super(new String[] {"bnet", "newbnet"}, CommandCategory.DESTINY,
        new PermsNeeded("command.bnet", false, true), "Change your Battle.net Nickname",
        "<new battle.net name>");

  }

  @Override
  protected Message execute(ParsedCommandInvocation parsedCommandInvocation,
      UserPermissions userPermissions) {
	User user = parsedCommandInvocation.getAuthor();
    String nickname = parsedCommandInvocation.getArgs()[0];
    String parsedName = null;
    Member member = parsedCommandInvocation.getMember();
    User author = parsedCommandInvocation.getAuthor();
    if (parsedCommandInvocation.getArgs().length == 0) {
      return createHelpMessage();
    }
    if (StringUtil.nicknameHasBrackets(nickname)) {
      parsedName = Player.removeBrackets(nickname);
    } else if (!StringUtil.nicknameHasBrackets(nickname)) {
      parsedName = nickname;
    }
//    
//    if (UserSQL.fromUser(user).exist()) {
//		String data = UserSQL.getData(user.getId());
//		
//	}
//    
    
    

    boolean validName = Player.isNicknameValid(parsedName);
    if (!validName) {
      embedSender.playerNotFound(author, parsedName);
      parsedCommandInvocation.getMessage().delete().queue();
    } else {
      parsedCommandInvocation.getGuild().getController().setNickname(member, parsedName).queue();
      parsedCommandInvocation.getTextChannel()
          .sendMessage(
              EmbedUtil
                  .success("Nickname Sucessfully Changed",
                      "Your Battle.net ID is valid, you're all set!" + author.getAsMention())
                  .build())
          .queue();
    }

    return null;



  }

}
