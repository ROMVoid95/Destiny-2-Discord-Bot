package site.romvoid.forgebot.handler.discord;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.dv8tion.jda.core.requests.RestAction;
import site.romvoid.forgebot.core.DiscordBot;

/**
 * wrapper around message add with a timeout to prevent deadlocks
 * <p>
 * Yes we have to make our own 'RestAction' wrapper around jda because they decided that its a good idea to accept the Consumer in the same Runnable as the RestAction
 * Yey another layer of abstraction isn't java great /s
 */
public class RestQueue {
    private final ExecutorService executor;

    public RestQueue(DiscordBot bot) {
        ThreadFactoryBuilder threadBuilder = new ThreadFactoryBuilder();
        threadBuilder.setNameFormat(String.format("shard-%02d-message-add-%%d", bot.getShardId()));
        executor = Executors.newFixedThreadPool(10, threadBuilder.build());
    }

    public <T> void add(RestAction<T> action) {
        executor.submit(new RestTask<>(action));
    }

    public <T> void add(RestAction<T> action, Consumer<T> complete) {
        executor.submit(new RestTask<>(executor, action, complete));
    }
}
