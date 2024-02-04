package org.imsouhay.pokedex.config;

import com.google.gson.Gson;
import org.imsouhay.LavenderMcServerSide.LavenderMcServerSide;
import org.imsouhay.LavenderMcServerSide.util.Utils;
import org.imsouhay.pokedex.PokeDex;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class Config {
	private boolean implementedOnly;
	private boolean isDexLimitEnabled;
	private int dexLimit;
	private String rewardClaimable;
	private String rewardAlreadyClaimed;
	private String claimingMessage;
	private String percentageNeededToClaim;
	private String currentProgress;
	private ArrayList<Reward> rewards;

	public Config() {
		implementedOnly = true;
		isDexLimitEnabled = false;
		dexLimit = 500;
		rewardClaimable="§aYou can claim this reward!";
		rewardAlreadyClaimed="§bYou have already claimed this reward";
		claimingMessage="§c[Pokedex] §2You successfully redeemed the @progress% dex rewards.";
		percentageNeededToClaim="§cYou need @progress to claim this reward";
		currentProgress="§6Current Progress: @progress%";
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

	public boolean isDexLimitEnabled() {
		return isDexLimitEnabled;
	}

	public int getDexLimit() {
		return dexLimit;
	}

	public String getRewardClaimable() {
		return rewardClaimable;
	}

	public String getRewardAlreadyClaimed() {
		return rewardAlreadyClaimed;
	}

	public String getClaimingMessage() {
		return claimingMessage;
	}

	public String getPercentageNeededToClaim() {
		return percentageNeededToClaim;
	}

	public String getCurrentProgress() {
		return currentProgress;
	}

	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync(PokeDex.POKE_DEX_PATH,
				"config.json", el -> {
					Gson gson = Utils.newGson();
					Config cfg = gson.fromJson(el, Config.class);
					implementedOnly = cfg.isImplementedOnly();
					isDexLimitEnabled =cfg.isDexLimitEnabled();
					dexLimit =cfg.getDexLimit();
					rewards = cfg.getRewards();
					percentageNeededToClaim= cfg.getPercentageNeededToClaim();
					currentProgress= cfg.getCurrentProgress();
					rewardClaimable= cfg.getRewardClaimable();
					rewardAlreadyClaimed= cfg.getRewardAlreadyClaimed();
					claimingMessage= cfg.getClaimingMessage();
				});

		if (!futureRead.join()) {
			LavenderMcServerSide.LOGGER.info("No config.json file found for PokeDex. Attempting to generate" +
					" " +
					"one");
			Gson gson = Utils.newGson();
			String data = gson.toJson(this);
			CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(PokeDex.POKE_DEX_PATH,
					"config.json", data);

			if (!futureWrite.join()) {
				LavenderMcServerSide.LOGGER.fatal("Could not write config for PokeDex.");
			}
			return;
		}
		LavenderMcServerSide.LOGGER.info("PokeDex config file read successfully");
	}
}
