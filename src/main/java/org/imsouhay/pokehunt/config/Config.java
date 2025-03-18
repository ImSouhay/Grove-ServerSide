package org.imsouhay.pokehunt.config;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.Gson;
import org.imsouhay.pokehunt.PokeHunt;
import org.imsouhay.pokehunt.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Config file.
 */
public class Config {
	private boolean individualHunts; // if hunts should be individual for each player.
	private boolean sendHuntEndMessage; // Should the mod send a message when a hunt ends.
	private boolean sendHuntBeginMessage; // Should the mod send a message when a hunt begins.
	private boolean startNewHuntOnCatch; // Should a new hunt start just after a previous one has ended
	private boolean coolDownEnabled;
	private final HashMap<String, Integer> huntDurations; // How long each hunt should last, in minutes.
	private int huntAmount; // How many hunts should there be at once.
	private RewardsConfig rewards; // The rewards for the hunts.
	private Properties matchProperties; // What properties should be checked to complete the hunt.
	private ArrayList<CustomReward> customRewards; // List of custom prices.
	private ArrayList<String> blacklist; // List if Pokemon that shouldn't be added to PokeHunt.
	private final HashMap<String, ArrayList<String>> defaultLore;
	private final HashMap<String, Integer> coolDownDurations;
	private final HashMap<String, Integer> legalHuntCounter;

	public Config() {
		individualHunts = false;
		sendHuntEndMessage = true;
		sendHuntBeginMessage = true;
		coolDownEnabled = true;
		startNewHuntOnCatch = false;

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
		defaultLore.put("onCooldown", new ArrayList<>(List.of("§7This hunt is on cooldown.")));
		
		coolDownDurations = new HashMap<>();
		coolDownDurations.put("Common", 5);
		coolDownDurations.put("Uncommon", 10);
		coolDownDurations.put("Rare", 25);
		coolDownDurations.put("UltraRare", 35);

		legalHuntCounter = new HashMap<>();
		legalHuntCounter.put("Common", 3);
		legalHuntCounter.put("Uncommon", 2);
		legalHuntCounter.put("Rare", 1);
		legalHuntCounter.put("UltraRare", 1);

		huntAmount = 7;
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
					Gson gson = Utils.newGson();
					Config cfg = gson.fromJson(el, Config.class);
					huntDurations.putAll(cfg.getHuntDurations());

					if (cfg.getHuntAmount() > 28) {
						huntAmount = 28;
						PokeHunt.LOGGER.error("PokeHunt amount can not be higher than 28");
					} else {
						huntAmount = cfg.getHuntAmount();
					}
					individualHunts = cfg.isIndividualHunts();
					sendHuntEndMessage = cfg.isSendHuntEndMessage();
					coolDownEnabled = cfg.isCoolDownEnabled();
					sendHuntBeginMessage = cfg.isSendHuntBeginMessage();
					matchProperties = cfg.getMatchProperties();
					customRewards = cfg.getCustomPrices();
					blacklist = cfg.getBlacklist();
					rewards = cfg.getRewards();
					defaultLore.putAll(cfg.getDefaultLore());
					startNewHuntOnCatch= cfg.isStartNewHuntOnCatch();
					coolDownDurations.putAll(cfg.getCoolDownDurations());
					legalHuntCounter.putAll(cfg.getLegalHuntCounter());
					if(legalHuntCounter.values().stream().mapToInt(Integer::intValue).sum() > huntAmount) {
						PokeHunt.LOGGER.error("Legal hunts can't be higher than the hunt amount");
						legalHuntCounter.put("Common", huntAmount - 4);
						legalHuntCounter.put("Uncommon", 2);
						legalHuntCounter.put("Rare", 1);
						legalHuntCounter.put("UltraRare", 1);
					}
				});

		// If the config couldn't be read, write a new one.
		if (!futureRead.join()) {
			PokeHunt.LOGGER.info("No config.json file found for PokeHunt. Attempting to generate one.");}
			Gson gson = Utils.newGson();
			String data = gson.toJson(this);
			CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(PokeHunt.POKEHUNT_PATH, "config.json",
					data);

			// If the write failed, log fatal.
			if (!futureWrite.join()) {
				PokeHunt.LOGGER.fatal("Could not write config for PokeHunt.");

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

	public boolean isCoolDownEnabled() {
		return coolDownEnabled;
	}
	public Integer getCoolDown(String rarity) {
		return coolDownDurations.get(rarity);
	}

	public HashMap<String, Integer> getCoolDownDurations() {
		return coolDownDurations;
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

	public RewardsConfig getRewards() {
		return rewards;
	}

	public HashMap<String, Integer> getLegalHuntCounter() {
		return legalHuntCounter;
	}

	public int getLegalRarityCounter(String strRarity) {return legalHuntCounter.get(strRarity);}


	public boolean blacklistContains(Pokemon pokemon) {
		for (String name : blacklist) {
			if (name.equalsIgnoreCase(pokemon.getDisplayName().getString().trim())
			    || name.equalsIgnoreCase(pokemon.getSpecies().getName().trim())) return true;
		}
		return false;
	}

	public boolean blacklistContains(String pokemon) {
		for (String name : blacklist) {
			if (name.equalsIgnoreCase(pokemon.trim())) return true;
		}
		return false;
	}

	public boolean isStartNewHuntOnCatch() {
		return startNewHuntOnCatch;
	}

	public HashMap<String, ArrayList<String>> getDefaultLore() {
		return defaultLore;
	}
}
