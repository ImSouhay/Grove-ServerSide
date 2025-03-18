package org.imsouhay.pokehunt.hunts;

import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.network.chat.Component;
import org.imsouhay.pokehunt.PokeHunt;
import org.imsouhay.pokehunt.config.CustomReward;
import org.imsouhay.pokehunt.util.Utils;

import java.util.*;

public class SingleHunt {

	private final UUID id; // Unique ID to reference hunt by.
	private final UUID owner; // Player who owns the hunt
	private ArrayList<String> commands; // Commands for completing the hunt.
	private final CurrentHunts currentHunts;
	private Pokemon pokemon; // Pokemon being hunted.
	private Timer timer; // Timer for hunt.
	private long endTime; // The end date for the hunt.
	private boolean huntEnded;
    private boolean huntCaught;
	private String rarity;

	public SingleHunt(UUID owner, CurrentHunts currentHunts) {
		this.currentHunts = currentHunts;
		id = UUID.randomUUID();
		this.owner = owner;
		huntEnded = false;
		huntCaught = false;

		generatePokemon();
		initializeRewards();
		setupTimer();
	}

	private void generatePokemon() {
		String rawRarityString;
		boolean isLegendary;
		int maxAttempts = 100;
		int attempts = 0;

		do {
			pokemon = new Pokemon();
			rawRarityString = PokeHunt.spawnRates.getRarity(pokemon);
			isLegendary = pokemon.isLegendary();
			this.rarity = Utils.getStringRarity(rawRarityString);
			attempts++;
		} while ((
				isLegendary || this.rarity == null || !isLegal(this.rarity)
				|| currentHunts.speciesListContains(pokemon.getSpecies()) ||
						PokeHunt.config.blacklistContains(pokemon)) && attempts < maxAttempts);

		if(!isLegal(rarity)) {
			throw new RuntimeException("\"Hunt is not Legal and reached the 100 attempt.");
		} else if (attempts == maxAttempts) {
			throw new RuntimeException("\"Unable to find a suitable Pokemon within 100 attempts.");
		}
	}

	private void initializeRewards() {
		boolean hasCustom = false;
		List<CustomReward> customRewards = PokeHunt.config.getCustomPrices();
		for (CustomReward item : customRewards) {
			if (item.getSpecies().trim().equalsIgnoreCase(pokemon.getSpecies().getName().trim())) {
				if (item.getForm().trim().equalsIgnoreCase("") || item.getForm().trim().equalsIgnoreCase(pokemon.getForm().getName().trim())) {
					hasCustom = true;
					commands = item.getCommand();
					break;
				}
			}
		}

		if (!hasCustom) {
			switch (this.rarity) {
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

		pokemon.rollAbility();
		pokemon.checkGender();
	}

	private void setupTimer() {
		int duration = PokeHunt.config.getHuntDurations().get(this.rarity) * 60 * 1000;
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (huntEnded) return;

				if (!PokeHunt.config.isCoolDownEnabled()) {
					if(PokeHunt.config.isIndividualHunts() && PokeHunt.server.getPlayerList().getPlayer(owner) == null) {
						currentHunts.removeHunt(id, false);
						return;
					}
					startNewHunt();
				} else {
					if (PokeHunt.config.isSendHuntEndMessage()) {
						if (PokeHunt.config.isIndividualHunts()) {
							try {
								Objects.requireNonNull(PokeHunt.server.getPlayerList().getPlayer(owner)).sendSystemMessage(
										Component.literal(
												Utils.formatPlaceholders(
														PokeHunt.language.getEndedHuntMessage(), null, pokemon
												)
										)
								);
							} catch (Exception ignored) {
							}
						} else {
							Utils.broadcastMessage(
									Utils.formatPlaceholders(
											PokeHunt.language.getEndedHuntMessage(), null, pokemon
									)
							);
						}
					}

					huntEnded = true;
					addToEndTime(PokeHunt.config.getCoolDown(SingleHunt.this.rarity) * 60 * 1000);
					timer.schedule(new TimerTask() {
						@Override
						public void run() {
							if(PokeHunt.config.isIndividualHunts() && PokeHunt.server.getPlayerList().getPlayer(owner) == null) {
								currentHunts.removeHunt(id, false);
								return;
							}
							startNewHunt();
						}
					}, PokeHunt.config.getCoolDown(SingleHunt.this.rarity) * 60 * 1000);
				}
			}
		}, duration);

		endTime = new Date().getTime() + duration;
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
	public long getEndTime() {
		return endTime;
	}

	public String getRarity() {
		return rarity;
	}

	public ArrayList<String> getCommands() {
		return commands;
	}

	public void addToEndTime(int t) {
		endTime += t;
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
		if (PokeHunt.config.getMatchProperties().isAbility() &&
				!pokemon.getAbility().getName().equalsIgnoreCase(this.pokemon.getAbility().getName())) {

			return false;
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

	public void startNewHunt() {
		if (PokeHunt.config.isIndividualHunts()) {
			try {
				this.currentHunts.replaceHunt(id, true);
			} catch(NullPointerException ignored) {
			}
		} else {
			PokeHunt.hunts.replaceHunt(id, true);
		}
	}

	public boolean isDone() {
		return ended() || isCaught();
	}

    public boolean isCaught() {
        return huntCaught;
    }

    public boolean ended() {
        return huntEnded;
    }

	public void end() {
		huntEnded = true;
	}
    public void setCaught() {huntCaught = true;}

	public boolean isLegal(String strRarity) {
		// Validate the rarity string
		if (strRarity == null || !PokeHunt.config.getLegalHuntCounter().containsKey(strRarity)) {
			PokeHunt.LOGGER.error("Invalid rarity: " + strRarity);
			return false;
		}

		int rarityCounter;
		if (PokeHunt.config.isIndividualHunts()) {
			rarityCounter = currentHunts.getRarityCounter(strRarity);
		} else {
			rarityCounter = PokeHunt.hunts.getRarityCounter(strRarity);
		}

		int legalRarityCounter = PokeHunt.config.getLegalRarityCounter(strRarity);
		boolean isLegal = rarityCounter < legalRarityCounter;

		return isLegal;
	}


}
