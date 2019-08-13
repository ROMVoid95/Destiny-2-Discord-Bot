package com.prime.commands.botowner;

import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager.ParsedCommandInvocation;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import com.prime.util.LFGChannelRULES;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

public class CommandLFGRules extends CommandHandler{

	public CommandLFGRules() {
		super(new String[] {"rules"}, CommandCategory.BOT_OWNER, new PermsNeeded("command.rules", true, false), "another special use command", "");
	}

	@Override
	protected Message execute(ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
        TextChannel channel = parsedCommandInvocation.getTextChannel();
		
		InputStream file = null;
        try {
            file = new URL("https://i.imgur.com/0uYQspL.png").openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        channel.sendFile(file, "ogmznP1.png", null).queue();
        channel.sendMessage("```md\nThis server is provided as a public service to all Discord account holders and users on a as-is basis. Our staff (Owner, Admins and Moderators) are not liable for any direct, indirect, special, incidental or consequential damages arising out of or related to the use of this server or presence in the server. All rules that are listed here apply to all server users, members and staff. These rules are\nnon-negotiable and do not hold any exceptions under any circumstance\n```").queue();
        channel.sendMessage(LFGChannelRULES.holder).queue();;
        try {
            file = new URL("https://i.imgur.com/eplSOvY.png").openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        channel.sendFile(file, "nQxCs97.png", null).queue();
        LFGChannelRULES.rules02(channel);
        try {
            file = new URL("https://i.imgur.com/FZDqX1J.png").openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        channel.sendFile(file, "t9DtKJj.png", null).queue();
        LFGChannelRULES.rules03(channel);
        try {
            file = new URL("https://i.imgur.com/UO1mevT.png").openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        channel.sendFile(file, "UO1mevT.png", null).queue();
        LFGChannelRULES.rules04(channel);
		
		return null;
	}

}
