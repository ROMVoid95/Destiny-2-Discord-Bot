package com.prime.features;

import com.prime.Prime;
import com.prime.sql.MySQL;
import com.prime.sql.VerificationKickSQL;
import com.prime.util.Logger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

public class VerificationKickHandler {

    static Map<Member, VerifyKick> verifyKicks = new HashMap<>();


    public static class VerifyKick {
        private final long guildid;
        private final long userid;
        private final String kickText;
        private final long kickDate;
        private final long messageId;
        @SuppressWarnings("unused")
        private final boolean silent;
        @SuppressWarnings("unused")
        private final boolean save;
        private final TimerTask resolveTask = new TimerTask() {
            @Override
            public void run() {
                schedule();
            }
        };

        public VerifyKick(Guild guild, Member user, Date kickDate, String kicktext, long messageid, boolean silent, boolean save) {
            this.guildid = guild.getIdLong();
            this.userid = user.getUser().getIdLong();
            this.messageId = messageid;
            this.kickDate = kickDate.getTime();
            this.kickText = kicktext;
            this.silent = silent;
            this.save = save;
            if (user.getUser().isBot())
                return;

            if (save)
                this.save();

            if (silent) return;
            Date now = new Date();
            verifyKicks.put(user, this);
            if (now.after(kickDate)) {
                this.schedule();
            } else {
                Prime.getTimer().schedule(resolveTask, new Date(this.kickDate));
            }
            //System.out.println(new SimpleDateFormat("HH:mm").format(this.kickDate));
        }

        public static VerifyKick fromMember(Member member, boolean silent) {
            return verifyKicks.get(member);
        }

        @SuppressWarnings("unlikely-arg-type")
        private void schedule() {
            if (!verifyKicks.containsValue(this)) return;
            Guild guild = Prime.getJDA().getGuildById(this.guildid);
            Member member = guild.getMemberById(this.userid);
            if (member.getUser().isBot()) {
                verifyKicks.remove(this);
                return;
            }
            if (guild.getSelfMember().canInteract(member)) {
                member.getUser().openPrivateChannel().queue(c -> c.sendMessage(this.kickText.replace("%invite%", guild.getTextChannelById(Prime.getMySQL().getVerificationValue(guild, "channelid")).createInvite().setMaxUses(1).complete().getURL())).queue());
                guild.getController().kick(member).reason(this.kickText).queue();
            }
            VerificationKickSQL sql = new VerificationKickSQL(member.getUser(), member.getGuild());
            Prime.getJDA().getGuildById(guildid).getTextChannelById(Prime.getMySQL().getVerificationValue(guild, "channelid")).getMessageById(Long.parseLong(sql.get("message"))).complete().delete().queue();
            remove();
            verifyKicks.remove(this);
        }

        boolean save() {
            try {
                PreparedStatement saveStatement = MySQL.getCon().prepareStatement("INSERT INTO `verifykicks` (`guildid`,`userid`, `kickText`, `kicktime`, `message`) VALUES (?,?,?,?,?)");
                saveStatement.setLong(1, this.guildid);
                saveStatement.setLong(2, this.userid);
                saveStatement.setString(3, this.kickText);
                saveStatement.setLong(4, this.kickDate);
                saveStatement.setLong(5, this.messageId);
                saveStatement.execute();
            } catch (SQLException e) {
                Logger.error(e);
                return false;
            }
            return true;
        }

        public boolean remove() {
            try {
                PreparedStatement deleteStatement = MySQL.getCon().prepareStatement("DELETE FROM `verifykicks` WHERE `userid` = ? AND `guildid` = ?");
                deleteStatement.setLong(1, this.userid);
                deleteStatement.setLong(2, this.guildid);
                deleteStatement.execute();
                PreparedStatement deleteStatement2 = MySQL.getCon().prepareStatement("DELETE FROM `verifyusers` WHERE `userid` = ? AND `guildid` = ?");
                deleteStatement2.setLong(1, this.userid);
                deleteStatement2.setLong(2, this.guildid);
                deleteStatement2.execute();
                verifyKicks.remove(Prime.getJDA().getGuildById(this.guildid).getMemberById(this.userid));
            } catch (SQLException e) {
                Logger.error(e);
                return false;
            }
            return true;
        }

        public boolean exists() {
            try {
                PreparedStatement ps = MySQL.getCon().prepareStatement("SELECT * FROM `verifykicks` WHERE `userid` = ? AND `guildidd` = ?");
                ps.setLong(1, this.userid);
                ps.setLong(2, this.guildid);
                ResultSet rs = ps.executeQuery();
                return rs.next();
            } catch (SQLException e) {
                Logger.error(e);
                return false;
            }
        }

        public static boolean exists(Member member) {
            try {
                PreparedStatement ps = MySQL.getCon().prepareStatement("SELECT * FROM `verifykicks` WHERE `userid` = ? AND `guildid` = ?");
                ps.setLong(1, member.getUser().getIdLong());
                ps.setLong(2, member.getGuild().getIdLong());
                ResultSet rs = ps.executeQuery();
                return rs.next();
            } catch (SQLException e) {
                Logger.error(e);
                return false;
            }
        }

        public long getMessageId() {
            return messageId;
        }
    }

    public static void loadVerifyKicks() {
        try {
            PreparedStatement selectStatement = MySQL.getCon()
                    .prepareStatement("SELECT * FROM `verifykicks` ");
            ResultSet channelResult = selectStatement.executeQuery();
            while (channelResult.next()) {
                Guild guild = Prime.getJDA().getGuildById(channelResult.getString("guildid"));
                Member member = guild.getMember(Prime.getJDA().getUserById(channelResult.getString("userid")));
                Date date = new Date(Long.parseLong(channelResult.getString("kicktime")));
                String text = channelResult.getString("kickText");
                long messageId = Long.parseLong(channelResult.getString("message"));
                if (!member.getUser().isBot())
                    new VerifyKick(guild, member, date, text, messageId, false, false);

            }
        } catch (SQLException | NullPointerException e) {
            Logger.error("Could not load verifykicks.");
            Logger.error(e);
        }
    }
}
