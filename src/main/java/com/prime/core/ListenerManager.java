package com.prime.core;

import com.prime.listener.AutochannelListener;
import com.prime.listener.AutoroleExecutor;
import com.prime.listener.BotJoinListener;
import com.prime.listener.BotLeaveListener;
import com.prime.listener.ChannelDeleteListener;
import com.prime.listener.MemberLeaveListener;
import com.prime.listener.MessageListener;
import com.prime.listener.PortalListener;
import com.prime.listener.ReactionListener;
import com.prime.listener.RoleListener;
import com.prime.listener.SelfMentionListener;
import com.prime.listener.ServerLogHandler;
import com.prime.listener.UserJoinListener;
import com.prime.listener.VerificationListener;
import com.prime.listener.readyListener;

import net.dv8tion.jda.core.JDABuilder;

public class ListenerManager {

    private JDABuilder b;

    public ListenerManager(JDABuilder builder) {
        this.b = builder;
        initListener();
    }

    private void initListener() {
        b.addEventListener(new SelfMentionListener());
        b.addEventListener(new AutoroleExecutor());
        b.addEventListener(new BotJoinListener());
        b.addEventListener(new ChannelDeleteListener());
        b.addEventListener(new BotLeaveListener());
        b.addEventListener(new PortalListener());
        b.addEventListener(new AutochannelListener());
        b.addEventListener(new UserJoinListener());
        b.addEventListener(new VerificationListener());
        b.addEventListener(new MemberLeaveListener());
        b.addEventListener(new ServerLogHandler());
        b.addEventListener(new ChannelDeleteListener());
        b.addEventListener(new RoleListener());
        b.addEventListener(new MessageListener());
        b.addEventListener(new ReactionListener());
        b.addEventListener(new readyListener());

    }
}
