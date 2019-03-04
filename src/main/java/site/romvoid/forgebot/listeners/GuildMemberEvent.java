package site.romvoid.forgebot.listeners;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GuildMemberEvent extends ListenerAdapter {
    
    public void onMemberRoleUpdate(GuildMemberRoleAddEvent event) {
        
        System.out.println(event.getRoles().stream().toString());
        Member m = event.getMember();
        Guild g = event.getGuild();
        Role na = g.getRoleById(550826786141896704L);
        Role eu = g.getRoleById(550826830882406411L);
        Role as = g.getRoleById(550826861693894656L);
     
        if (event.getRoles().contains(as)) {
            if (m.getRoles().contains(eu) || m.getRoles().contains(na)) {
                g.getController().removeSingleRoleFromMember(m, eu).queue();
                g.getController().removeSingleRoleFromMember(m, na).queue();
            }
        }
        if (event.getRoles().contains(eu)) {
            if (m.getRoles().contains(na) || m.getRoles().contains(as)) {
                g.getController().removeSingleRoleFromMember(m, na).queue();
                g.getController().removeSingleRoleFromMember(m, as).queue();
            }
        }
        if (event.getRoles().contains(na)) {
            if (m.getRoles().contains(eu) || m.getRoles().contains(as)) {
                g.getController().removeSingleRoleFromMember(m, eu).queue();
                g.getController().removeSingleRoleFromMember(m, as).queue();
            }
        }
    }
}