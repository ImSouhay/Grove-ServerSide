package org.imsouhay.pokedex.config;

import com.google.gson.Gson;
import org.imsouhay.LavenderMcServerSide.LavenderMcServerSide;
import org.imsouhay.LavenderMcServerSide.util.Utils;
import org.imsouhay.pokedex.PokeDex;

import java.util.concurrent.CompletableFuture;

public class Lang {
	private String title;
	private String fillerMaterial;

	public Lang() {
		title = "PokeDex";
		fillerMaterial = "minecraft:white_stained_glass_pane";
	}

	public String getTitle() {
		return title;
	}

	public String getFillerMaterial() {
		return fillerMaterial;
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
				});

		if (!futureRead.join()) {
			LavenderMcServerSide.LOGGER.info("No lang.json file found for PokeDex. Attempting to " +
					"generate one.");
			Gson gson = Utils.newGson();
			String data = gson.toJson(this);
			CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(PokeDex.POKE_DEX_PATH, "lang.json", data);

			if (!futureWrite.join()) {
				LavenderMcServerSide.LOGGER.fatal("Could not write lang.json for " + LavenderMcServerSide.MOD_ID + ".");
			}
			return;
		}
		LavenderMcServerSide.LOGGER.info(LavenderMcServerSide.MOD_ID + " lang file read successfully.");
	}
}
