package com.prime.listener;

import com.prime.Prime;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.Webhook;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.webhook.WebhookClient;
import net.dv8tion.jda.webhook.WebhookClientBuilder;
import net.dv8tion.jda.webhook.WebhookMessage;
import net.dv8tion.jda.webhook.WebhookMessageBuilder;

public class PortalListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        if (!e.getAuthor().isBot()) {
            if (e.getChannel().getId().equals(Prime.getMySQL().getPortalValue(e.getGuild(), "channelid"))) {
                String status = Prime.getMySQL().getGuildValue(e.getGuild(), "portal");
                if (status.contains("open")) {
                    TextChannel otherChannel = e.getJDA().getTextChannelById(Prime.getMySQL().getPortalValue(e.getJDA().getGuildById(Prime.getMySQL().getPortalValue(e.getGuild(), "partnerid")), "channelid"));
                    try {
                        Webhook webhook = null;

                        for (Webhook hook : otherChannel.getWebhooks().complete()) {
                            if (hook.getName().equals("thevoid-portal-hook")) {
                                webhook = hook;
                                break;
                            }
                        }
                        if (webhook == null) {
                            webhook = otherChannel.createWebhook("rubicon-portal-hook").complete();
                        }
                        WebhookClientBuilder clientBuilder = webhook.newClient();
                        WebhookClient client = clientBuilder.build();

                        WebhookMessageBuilder builder = new WebhookMessageBuilder();
                        builder.setContent(e.getMessage().getContentDisplay().replace("@here", "@ here").replace("@everyone", "@ everyone"));
                        builder.setAvatarUrl(e.getAuthor().getAvatarUrl());
                        builder.setUsername(e.getAuthor().getName());
                        WebhookMessage message = builder.build();
                        client.send(message);
                        client.close();

                    } catch (NullPointerException fuck) {
                        fuck.printStackTrace();
                    }
                }
            }
        }
    }
}
