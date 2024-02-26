package org.imsouhay.pokehunt.hunts;

import com.cobblemon.mod.common.pokemon.Pokemon;
import org.imsouhay.pokehunt.PokeHunt;
import org.imsouhay.pokehunt.config.CustomReward;
import org.imsouhay.pokehunt.util.Utils;

import java.text.DecimalFormat;
import java.util.*;

public class SingleHunt {

	private final UUID id; // Unique ID to reference hunt by.
	private final UUID owner; // Player who owns the hunt
	private ArrayList<String> commands; // Commands for completing the hunt.
	private Pokemon pokemon; // Pokemon being hunted.
	private final Timer timer; // Timer for hunt.
	private final long endtime; // The end date for the hunt.
//	private final String nature;

	public SingleHunt(UUID owner) {
		// Creates unique ID and generates random pokemon.
		id = UUID.randomUUID();
		this.owner = owner;

		pokemon = new Pokemon();

		float rarity = PokeHunt.spawnRates.getRarity(pokemon);

		boolean isLegendary = pokemon.isLegendary();

		// Will keep regenerating a Pokemon until one found in the rarity table that isn't a legendary is found.
		while (rarity == -1 || isLegendary) {
			pokemon = new Pokemon();
			rarity = PokeHunt.spawnRates.getRarity(pokemon);
			isLegendary = pokemon.isLegendary();
		}

		boolean hasCustom = false;

		// Checks for a custom price.
		List<CustomReward> customRewards = PokeHunt.config.getCustomPrices();
		for (CustomReward item : customRewards) {
			// If species match
			if (item.getSpecies().trim().equalsIgnoreCase(pokemon.getSpecies().getName().trim())) {
				// If no form is given or the form matches, use price.
				if (item.getForm().trim().equalsIgnoreCase("") ||
						item.getForm().trim().equalsIgnoreCase(pokemon.getForm().getName().trim())) {
					hasCustom = true;
					commands = item.getCommand();
					break;
				}
			}
		}

		String strRarity = Utils.getStringRarity(rarity);

		// If a custom price is found, don't run this.
		if (!hasCustom) {
			switch (strRarity) {
				case "Common":
					commands = PokeHunt.config.getRewards().getCommon().getCommands();
					break;
				case "Uncommon":
					commands = PokeHunt.config.getRewards().getUncommon().getCommands();
					break;
				case "Rare":
					commands = PokeHunt.config.getRewards().getRare().getCommands();
					break;
				default:
					commands = PokeHunt.config.getRewards().getUltraRare().getCommands();
			}
		}

		pokemon.checkAbility();
		pokemon.checkGender();

		// Creates the timer to replace the hunt once it is over.
		int duration = PokeHunt.config.getHuntDurations().get(strRarity) * 60 * 1000;
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (PokeHunt.config.isIndividualHunts()) {
					try {
						PokeHunt.manager.getPlayerHunts(owner).replaceHunt(id, true);
					} catch(NullPointerException e) {
						timer.cancel();
					}
				} else {
					PokeHunt.hunts.replaceHunt(id, true);
				}
			}
		}, duration);

		// Adds the endtime as the current time + the duration.
		endtime = new Date().getTime() + duration;
	}

	/**
	 * Getters
	 */

	public UUID getId() {
		return id;
	}

	public Pokemon getPokemon() {
		return pokemon;
	}

	public Timer getTimer() {
		return timer;
	}
	public long getEndtime() {
		return endtime;
	}

	public ArrayList<String> getCommands() {
		return commands;
	}

	/**
	 * Checks that a given pokemon matches the one in the listing.
	 * @param pokemon The pokemon to check.
	 * @return true if the pokemon matches the hunt, or false if it doesn't.
	 */

	public boolean matches(Pokemon pokemon) {
		// Checks the species and form match.
		if (!pokemon.getSpecies().getName().equalsIgnoreCase(this.pokemon.getSpecies().getName())) {
			return false;
		}
		if (!pokemon.getForm().getName().equalsIgnoreCase(this.getPokemon().getForm().getName())) {
			return false;
		}

		// Checks for ability, if enabled.
		if (PokeHunt.config.getMatchProperties().isAbility()) {
			if (!pokemon.getAbility().getName().equalsIgnoreCase(this.pokemon.getAbility().getName())) {
				return false;
			}
		}

		// Checks gender, if enabled.
		if (PokeHunt.config.getMatchProperties().isGender()) {
			if (!pokemon.getGender().name().equalsIgnoreCase(this.pokemon.getGender().name())) {
				return false;
			}
		}

		// Checks nature, if enabled.
		if (PokeHunt.config.getMatchProperties().isNature()) {
			if (!pokemon.getNature().getName().getPath().equalsIgnoreCase(this.pokemon.getNature().getName().getPath())) {
				return false;
			}
		}

		// Checks shiny, if enabled.
		if (PokeHunt.config.getMatchProperties().isShiny()) {
			return pokemon.getShiny() == this.pokemon.getShiny();
		}
		return true;
	}

}
