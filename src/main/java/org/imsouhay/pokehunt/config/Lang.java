package org.imsouhay.pokehunt.config;

import com.google.gson.Gson;
import org.imsouhay.pokehunt.PokeHunt;
import org.imsouhay.pokehunt.util.Utils;

import java.util.concurrent.CompletableFuture;

/**
 * Class for customization of language.
 */
public class Lang {
	private String title; // Title of the menu
	private String fillerMaterial; // The material used as the border in the UI.
	private String reloadMessage; // Reload message.
	private String captureHuntBroadcast; // Message broadcast to players when a hunt has been caught.
	private String endedHuntMessage; // The message sent when a hunt ends.
	private String newHuntMessage; // The message sent when a new hunt begins.
	private String reward;
	private String timeRemaining;

	public Lang() {
		title = "PokeHunt";
		fillerMaterial = "minecraft:white_stained_glass_pane";
		reloadMessage = "§aReloaded Configs!";
		captureHuntBroadcast = "§5[PokeHunt] §e{player} §ahas successfully hunted §e{pokemon}";
		endedHuntMessage = "§5[PokeHunt] §3The hunt for {pokemon} has ended!";
		newHuntMessage = "§5[PokeHunt] §bThe hunt for {pokemon} has begun!";
		reward = "§6Reward: §e";
		timeRemaining = "§9Time Remaining: §b";
	}

	public String getTitle() {
		return title;
	}
	public String getFillerMaterial() {
		return fillerMaterial;
	}
	public String getReloadMessage() {
		return reloadMessage;
	}
	public String getCaptureHuntBroadcast() {
		return captureHuntBroadcast;
	}
	public String getEndedHuntMessage() {
		return endedHuntMessage;
	}
	public String getNewHuntMessage() {
		return newHuntMessage;
	}
	public String getReward() {
		return reward;
	}
	public String getTimeRemaining() {
		return timeRemaining;
	}

	/**
	 * Reads or creates the lang file.
	 */
	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync(PokeHunt.POKEHUNT_PATH, "lang.json",
				el -> {
					Gson gson = Utils.newGson();
					Lang lang = gson.fromJson(el, Lang.class);
					title = lang.getTitle();
					fillerMaterial = lang.getFillerMaterial();
					reloadMessage = lang.getReloadMessage();
					captureHuntBroadcast = lang.getCaptureHuntBroadcast();
					endedHuntMessage = lang.getEndedHuntMessage();
					newHuntMessage = lang.getNewHuntMessage();
					reward = lang.getReward();
					timeRemaining = lang.getTimeRemaining();
				});

		if (!futureRead.join()) {
			PokeHunt.LOGGER.info("No lang.json file found for PokeHunt. Attempting to generate one.");}

		Gson gson = Utils.newGson();
			String data = gson.toJson(this);
			CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(PokeHunt.POKEHUNT_PATH, "lang.json",
					data);

			if (!futureWrite.join()) {
				PokeHunt.LOGGER.fatal("Could not write lang.json file for PokeHunt.");
			return;
		}
			PokeHunt.LOGGER.info("PokeHunt lang file read successfully.");
	}
}
