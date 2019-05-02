package com.prime.commands.moderation;

import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager.ParsedCommandInvocation;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;

import java.awt.Color;
import java.awt.List;
import java.lang.reflect.Array;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

	public class CommandMakeEmbed extends CommandHandler {
		
		String field2Title;
		String field2body;
		String field3Title;
		String field3body;
		String field4Title;
		String field4body;
		Color c;
		String thumbnail;

		public CommandMakeEmbed() {
			super(new String[] {"embed"}, CommandCategory.MODERATION, new PermsNeeded("command.embed", false, false), "Generate a custom Embed In Discord", "Working on a Help file for this");
		}

		@Override
		protected Message execute(ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
			String[] args = parsedCommandInvocation.getArgs();
			if (parsedCommandInvocation.getArgs().length == 0) {
				return createHelpMessage();		
			}
			//first row should not be null ( field args start with - )
			String title = args[0];
			String field1Title = args[1];
			String field1body = args[2];
			if (args[3].contains("blank")){
				field2Title = EmbedBuilder.ZERO_WIDTH_SPACE;}
			field2Title = args[3];
			if (args[4].contains("blank")){
				field3Title = EmbedBuilder.ZERO_WIDTH_SPACE;}
			field3Title = args[4];
			if (args[5].contains("blank")){
				field4Title = EmbedBuilder.ZERO_WIDTH_SPACE;}
			field4Title = args[5];
			
			// set color ( start with .black | .red )
			switch (args[6]) {
			case ".red":
				c = Color.red;
				break;
			case ".yellow":
				c = Color.yellow;
				break;
			case ".blue":
				c = Color.blue;
				break;
			}
			
//			if (args[7].startsWith("`") && args[7].endsWith("`")) {
//				thumbnail = args[7];
//			}
			
			String url = thumbnail.replaceAll("`", "");
			EmbedBuilder custom = new EmbedBuilder()
					.setTitle(title)
					.addField(field1Title, field1body, false)
					.addField(field2Title+"", field2body+"", false)
					.addField(field3Title+"", field3body+"", false)
					.addField(field4Title+"", field4body+"", false)
					.setColor(c);
					//.setThumbnail(url);
						parsedCommandInvocation.getTextChannel().sendMessage(custom.build()).queue();

			
			return null;
		}

	}
