package org.imsouhay.pokedex.config;

import com.google.gson.Gson;
import org.imsouhay.LavenderMcServerSide.LavenderMcServerSide;
import org.imsouhay.pokedex.util.Utils;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class Config {
	private boolean implementedOnly;
	private ArrayList<Reward> rewards;

	public Config() {
		implementedOnly = true;
		rewards = new ArrayList<>();
		rewards.add(new Reward(10, 10, "cobblemon:poke_ball"));
		rewards.add(new Reward(20, 12, "cobblemon:great_ball"));
		rewards.add(new Reward(30, 14, "cobblemon:ultra_ball"));
		rewards.add(new Reward(40, 16, "cobblemon:dusk_ball"));
		rewards.add(new Reward(50, 28, "cobblemon:quick_ball"));
		rewards.add(new Reward(60, 30, "cobblemon:sport_ball"));
		rewards.add(new Reward(70, 32, "cobblemon:love_ball"));
		rewards.add(new Reward(80, 34, "cobblemon:moon_ball"));
		rewards.add(new Reward(90, 38, "cobblemon:park_ball"));
		rewards.add(new Reward(100, 42, "cobblemon:master_ball"));
	}

	public boolean isImplementedOnly() {
		return implementedOnly;
	}

	public ArrayList<Reward> getRewards() {
		return rewards;
	}

	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync(LavenderMcServerSide.BASE_PATH,
				"config.json", el -> {
					Gson gson = Utils.newGson();
					Config cfg = gson.fromJson(el, Config.class);
					implementedOnly = cfg.isImplementedOnly();
					rewards = cfg.getRewards();
				});

		if (!futureRead.join()) {
			LavenderMcServerSide.LOGGER.info("No config.json file found for " + LavenderMcServerSide.MOD_ID + ". Attempting to generate" +
					" " +
					"one");
			Gson gson = Utils.newGson();
			String data = gson.toJson(this);
			CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(LavenderMcServerSide.BASE_PATH,
					"config.json", data);

			if (!futureWrite.join()) {
				LavenderMcServerSide.LOGGER.fatal("Could not write config for " + LavenderMcServerSide.MOD_ID + ".");
			}
			return;
		}
		LavenderMcServerSide.LOGGER.info(LavenderMcServerSide.MOD_ID + " config file read successfully");
	}
}
