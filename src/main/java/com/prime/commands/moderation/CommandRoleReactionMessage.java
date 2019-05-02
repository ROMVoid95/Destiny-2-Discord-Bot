package com.prime.commands.moderation;

import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager.ParsedCommandInvocation;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;

import java.awt.Color;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

public class CommandRoleReactionMessage extends CommandHandler{
	
	private static String holder = "⁣        ";
	private static String sHolder = "⁣    ";

	public CommandRoleReactionMessage() {
		super(new String[] {"reactmsg"}, CommandCategory.MODERATION, new PermsNeeded("command.react", false, false), "Just a special use message", "");
	}

	@Override
	protected Message execute(ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
		
		EmbedBuilder msg = new EmbedBuilder()
	    .setTitle("▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀:regional_indicator_r::regional_indicator_e::regional_indicator_g::regional_indicator_i::regional_indicator_o::regional_indicator_n::regional_indicator_s:▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄")
	    .setDescription(EmbedBuilder.ZERO_WIDTH_SPACE)
	    .addField(sHolder + sHolder + "In order to receive any roles you need to pick your playing region", EmbedBuilder.ZERO_WIDTH_SPACE, false)
	    .setColor(Color.YELLOW)
	    .addField("🤔  𝙿𝚕𝚎𝚊𝚜𝚎 𝙴𝚡𝚙𝚕𝚊𝚒𝚗", "sure, when you log into Battle.net you choose a region, the region you play in the most should be the region you choose. But more than one can be selected if needed  ", false)
	    .addField("😱  𝙸 𝚜𝚎𝚕𝚎𝚌𝚝𝚎𝚍 𝚘𝚗𝚎 𝚊𝚗𝚍 𝚗𝚘𝚝𝚑𝚒𝚗𝚐 𝚑𝚊𝚙𝚙𝚎𝚗𝚎𝚍", "Your nickname may not be set to your battle.net name, if you did change it double check to be sure it's correct. The bot will message you with information.\n\nIt must be in the format:  `name#numbers` -> romvoid#1909", false)
	    .addField("🙄 𝙸𝚝 𝚒𝚜 𝚊𝚗𝚍 𝚜𝚝𝚒𝚕𝚕 𝚗𝚘𝚝𝚑𝚒𝚗𝚐", "It's no problem, just message on of our staff and we'll get it taken care of for you", false);

	    parsedCommandInvocation.getTextChannel().sendMessage(msg.build()).queue();
		return null;
	}

}
