package com.prime.util;

import java.awt.Color;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;

public class LFGChannelRULES {
	
	public static String holder = "⁣        ";
	private static String sHolder = "⁣    ";
	
    public static void rules01(MessageChannel channel) {
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.blue)
                .setTitle("Rules, Useage & Moderation")
                .setDescription("This server is provided as a public service to all Discord account holders and users on a as-is basis. Our staff (Owner, Admins and Moderators) are not liable for any direct, indirect, special, incidental or consequential damages arising out of or related to the use of this server or presence in the server.  All rules that are listed here apply to all server users, members and staff. These rules are non-negotiable and do not hold any exceptions under any circumstance.");
      channel.sendMessage(embed.build()).complete();
    }
    
    public static void rules02(MessageChannel channel) {
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.RED)
                .addField("Any post or voice chat that contains the following will result in an immediate and permanent non-negotiable ban", holder + "\n"
                        + ":small_red_triangle_down: Content that violates state or federal law\n "
                        + ":small_red_triangle_down: Content discussing illegal activities with the intent to commit\n" + 
                        ":small_red_triangle_down: Content that is libelous or that defames or threatens others\n" + 
                        ":small_red_triangle_down: Make unproven/unsupported accusations against anything\n" + 
                        ":small_red_triangle_down: Hate speech\n" + 
                        ":small_red_triangle_down: Advertising or any form of commercial solicitation\n" + 
                        ":small_red_triangle_down: Offering any type of paid or compensation style services \n"
                         +sHolder+":small_blue_diamond: *This is a violation of* [Bungie ToS](https://www.bungie.net/en/Bungie/Terms) *and* [Discord ToS](https://discordapp.com/terms).\n" + 
                        ":small_red_triangle_down: Content that is objectionable in a manner to be a Discord ToS violation" , false);

        channel.sendMessage(embed.build()).complete();
        
    }
    
    
    public static void rules03(MessageChannel channel) {
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.YELLOW)
        
        .addField("Soft ban is removal (kick) from the server, the user is not banned. Mutable is controlled by the ServerBot and not role dependant", holder + "\n" +
                ":small_orange_diamond: Excessive text or pingable role spamming \n" + 
                ":small_orange_diamond: Discriminate against members if they lack experience \n "+
                sHolder+":small_blue_diamond: (**Severity can lead to permanent ban**)\n" + 
                ":small_orange_diamond: Excessively loud noise and/or general nuisances in voice chat\n" + 
                ":small_orange_diamond: Issues that arise in-game that result in but not limited too: \n" + 
                sHolder+":small_blue_diamond: *Degregation of Destiny 2 Stats*\n" + 
                sHolder+":small_blue_diamond: *Loss of any possible in-game loot item(s)*\n" + 
                sHolder+":small_blue_diamond: *Clan member defermation*\n" + 
                sHolder+":small_blue_diamond: *Arguments that continue/start in any text channel*\n" + 
                sHolder+":small_blue_diamond: (**Severity can lead to permanent ban**)\n" + 
                ":small_orange_diamond:Editing any prior messages to circumvent the servers automod system\n" + 
                 sHolder+":small_blue_diamond: *adding another severs discord invite*", false);
        
        channel.sendMessage(embed.build()).complete();
    }
    
    public static void rules04(MessageChannel channel) {
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.WHITE)
        
        .addField("The Staff reserves the right to:", holder + "\n" +
                ":small_orange_diamond: Restrict, suspend or terminate your access to all or part of the server if you are in\n" + sHolder + "breach of any rule(s) or Discord/Bungie ToS. \n" + 
                ":small_orange_diamond: Delete, edit, move or remove any content, message(s), media and/or LFG post\n" + sHolder + "you send in any channel of the server if it: \n "+
                sHolder+":small_blue_diamond: Breaches any rule(s) or [Bungie ToS](https://www.bungie.net/en/Bungie/Terms) */* [Discord ToS](https://discordapp.com/terms)\n" + 
                ":small_orange_diamond: Rights stated above are also applicable as a result of any before\n" + sHolder + "mentioned violations \n" + 
                ":small_orange_diamond: This is determined by a Staff Member at their sole discretion \n", false);
        
        channel.sendMessage(embed.build()).complete();
    }

}
