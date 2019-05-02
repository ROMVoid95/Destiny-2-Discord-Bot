package com.prime.commands.botowner;

import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import com.prime.sql.UserSQL;
import com.prime.util.StringUtil;
import com.prime.util.destiny.Player;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

public class CommandTest extends CommandHandler {

    public CommandTest() {
        super(new String[]{"test"}, CommandCategory.GENERAL, new PermsNeeded("command.test", false, true), "", "");
    }

    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
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
        String no = "Try Again";
        
        if (UserSQL.fromUser(user).exist()) {
    		String data = UserSQL.getData(user.getId());
    		
    		parsedCommandInvocation.getTextChannel().sendMessage(data).queue();
    	}
        parsedCommandInvocation.getTextChannel().sendMessage(no).queue();
        return null;
    }
}