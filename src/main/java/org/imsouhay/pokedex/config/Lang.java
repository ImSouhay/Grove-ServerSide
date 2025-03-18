package org.imsouhay.pokedex.config;

import com.google.gson.Gson;
import org.imsouhay.Grove.Grove;
import org.imsouhay.Grove.util.Utils;
import org.imsouhay.pokedex.PokeDex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Lang {
	private String title;
	private String fillerMaterial;
	private String searchButtonTitle;
	private List<String> searchButtonLore;

	public Lang() {
		title = "PokeDex";
		fillerMaterial = "minecraft:white_stained_glass_pane";
		searchButtonTitle = "§eSearch for pokemon";
		searchButtonLore = new ArrayList<>(Arrays.asList(
				"§6§lRight-Click §r§6to search by dex-number",
				"§6§lLeft-Click §r§6to search by pokemon name"));
	}

	public String getTitle() {
		return title;
	}

	public String getFillerMaterial() {
		return fillerMaterial;
	}

	public String getSearchButtonTitle() {
		return searchButtonTitle;
	}

	public List<String> getSearchButtonLore() {
		return searchButtonLore;
	}

	/**
	 * Method to initialize the config.
	 */
	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync(PokeDex.POKE_DEX_PATH, "lang.json",
				el -> {
					Gson gson = Utils.newGson();
					Lang lang = gson.fromJson(el, Lang.class);
					title = lang.getTitle();
					fillerMaterial = lang.getFillerMaterial();
					searchButtonLore = lang.getSearchButtonLore();
					searchButtonTitle = lang.getSearchButtonTitle();
				});

		if (!futureRead.join()) {
			Grove.LOGGER.info("No lang.json file found for PokeDex. Attempting to " +
					"generate one.");}
			Gson gson = Utils.newGson();
			String data = gson.toJson(this);
			CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(PokeDex.POKE_DEX_PATH, "lang.json", data);

			if (!futureWrite.join()) {
				Grove.LOGGER.fatal("Could not write lang.json for " + Grove.MOD_ID + ".");

			return;
		}
		Grove.LOGGER.info(Grove.MOD_ID + " lang file read successfully.");
	}
}
