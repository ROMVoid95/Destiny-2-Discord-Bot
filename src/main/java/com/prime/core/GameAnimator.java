package com.prime.core;

import com.prime.Prime;

import java.util.concurrent.TimeUnit;

import br.com.brjdevs.java.utils.async.Async;
import net.dv8tion.jda.core.entities.Game;

public class GameAnimator {

    private static int currentGame = 0;

    private static final String[] gameAnimations = {
            "Exploring the EDZ",
            "Quickplay",
            "Raid | Nessus Orbit",
            "Comp = Yikes",
            "Strike",
            "@PrimeBot help"
    };

	public static synchronized void changeStatus() {
		if (currentGame == gameAnimations.length - 1)
			currentGame = 0;
		else
			currentGame += 1;
		Prime.getJDA().getPresence().setGame(Game.playing(gameAnimations[currentGame]));

	}

	public static void start() {

		Async.task("GameAnimator", () -> {
			changeStatus();
		}, 1, TimeUnit.MINUTES);
	}
}