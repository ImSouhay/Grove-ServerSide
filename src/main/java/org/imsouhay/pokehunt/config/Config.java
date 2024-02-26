package org.imsouhay.pokehunt.config;

import com.google.gson.Gson;
import org.imsouhay.pokehunt.PokeHunt;
import org.imsouhay.pokehunt.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

/**
 * Config file.
 */
public class Config {
	private boolean individualHunts; // if hunts should be individual for each player.
	private boolean sendHuntEndMessage; // Should the mod send a message when a hunt ends.
	private boolean sendHuntBeginMessage; // Should the mod send a message when a hunt begins.
	private HashMap<String, Integer> huntDurations; // How long each hunt should last, in minutes.
	private int huntAmount; // How many hunts should there be at once.
	private RarityConfig rarity; // The rarity borders.
	private RewardsConfig rewards; // The rewards for the hunts.
	private Properties matchProperties; // What properties should be checked to complete the hunt.
	private ArrayList<CustomReward> customRewards; // List of custom prices.
	private ArrayList<String> blacklist; // List if Pokemon that shouldn't be added to PokeHunt.
	private HashMap<String, ArrayList<String>> defaultLore;

	public Config() {
		individualHunts = false;
		sendHuntEndMessage = true;
		sendHuntBeginMessage = true;

		huntDurations= new HashMap<>();

		huntDurations.put("Common", 60);
		huntDurations.put("Uncommon", 60);
		huntDurations.put("Rare", 60);
		huntDurations.put("UltraRare", 60);

		defaultLore= new HashMap<>();
		defaultLore.put("Common", new ArrayList<>());
		defaultLore.put("Uncommon", new ArrayList<>());
		defaultLore.put("Rare", new ArrayList<>());
		defaultLore.put("UltraRare", new ArrayList<>());

		huntAmount = 7;
		rarity = new RarityConfig();
		rewards = new RewardsConfig();
		matchProperties = new Properties();
		customRewards = new ArrayList<>();
		customRewards.add(new CustomReward());
		blacklist = new ArrayList<>();
	}

	/**
	 * Reads the config or writes one if a config doesn't exist.
	 */
	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync(PokeHunt.POKEHUNT_PATH, "config.json",
				el -> {
					PokeHunt.LOGGER.error(PokeHunt.POKEHUNT_PATH);
					Gson gson = Utils.newGson();
					Config cfg = gson.fromJson(el, Config.class);
					huntDurations = cfg.getHuntDurations();

					if (cfg.getHuntAmount() > 28) {
						huntAmount = 28;
						PokeHunt.LOGGER.error("PokeHunt amount can not be higher than 28");
					} else {
						huntAmount = cfg.getHuntAmount();
					}
					individualHunts = cfg.isIndividualHunts();
					sendHuntEndMessage = cfg.isSendHuntEndMessage();
					sendHuntBeginMessage = cfg.isSendHuntBeginMessage();
					matchProperties = cfg.getMatchProperties();
					customRewards = cfg.getCustomPrices();
					blacklist = cfg.getBlacklist();
					rarity = cfg.getRarity();
					rewards = cfg.getRewards();
					defaultLore= cfg.getDefaultLore();
				});

		// If the config couldn't be read, write a new one.
		if (!futureRead.join()) {
			PokeHunt.LOGGER.info("No config.json file found for PokeHunt. Attempting to generate one.");
			Gson gson = Utils.newGson();
			String data = gson.toJson(this);
			CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(PokeHunt.POKEHUNT_PATH, "config.json",
					data);

			// If the write failed, log fatal.
			if (!futureWrite.join()) {
				PokeHunt.LOGGER.fatal("Could not write config for PokeHunt.");
			}
			return;
		}
		PokeHunt.LOGGER.info("PokeHunt config file read successfully.");
	}


	/**
	 * Bunch of Getters
	 */

	public boolean isIndividualHunts() {
		return individualHunts;
	}

	public HashMap<String, Integer> getHuntDurations() {
		return huntDurations;
	}

	public int getHuntAmount() {
		return huntAmount;
	}


	public Properties getMatchProperties() {
		return matchProperties;
	}

	public ArrayList<CustomReward> getCustomPrices() {
		return customRewards;
	}

	public boolean isSendHuntEndMessage() {
		return sendHuntEndMessage;
	}

	public boolean isSendHuntBeginMessage() {
		return sendHuntBeginMessage;
	}

	public ArrayList<String> getBlacklist() {
		return blacklist;
	}

	public RarityConfig getRarity() {
		return rarity;
	}

	public RewardsConfig getRewards() {
		return rewards;
	}

	public boolean blacklistContains(String pokemon) {
		for (String name : blacklist) {
			if (name.equalsIgnoreCase(pokemon)) return true;
		}
		return false;
	}

	public HashMap<String, ArrayList<String>> getDefaultLore() {
		return defaultLore;
	}
}
