package site.romvoid.forgebot.core;

import java.util.Arrays;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import site.romvoid.forgebot.util.STATIC;


public class permissionHandler {
    
    public static Boolean checkStaffRole(MessageReceivedEvent event){
        for(Role r : event.getGuild().getMember(event.getAuthor()).getRoles()){
            if(Arrays.stream(STATIC.STAFF).parallel().anyMatch(r.getName()::contains))
                return false;
        }
        return true;
    }

    public static Boolean isOwner(MessageReceivedEvent event){
        User a = event.getAuthor();
        if(Arrays.stream(STATIC.OWNERID).parallel().anyMatch(a.getId()::contains)){
            return false;
        }
        return true;
    }



}
