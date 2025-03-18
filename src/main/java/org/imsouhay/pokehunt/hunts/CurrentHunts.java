package org.imsouhay.pokehunt.hunts;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.imsouhay.Grove.Grove;
import org.imsouhay.pokehunt.PokeHunt;
import org.imsouhay.pokehunt.util.Utils;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class CurrentHunts {
	private final UUID owner; // The owner of the current hunts.
	private final HashMap<UUID, SingleHunt> hunts; // List of current hunts.
	private final HashMap<UUID, Species> species; // List of species.
	private final HashMap<String, Integer> huntsCounter;

	/**
	 * Constructor that generates a bunch of hunts when the server starts.
	 */
	public CurrentHunts(UUID owner) {
		this.owner = owner;
		hunts = new HashMap<>();
		species = new HashMap<>();
		huntsCounter = new HashMap<>();
	}

	/**
	 * Initializes hashmap with hunts.
	 */
	public void init() {
		if (owner == null && PokeHunt.config.isIndividualHunts()) {
			return;
		}

		for (int x = 0; x < PokeHunt.config.getHuntAmount(); x++) {
			addHunt();
		}
	}

	/**
	 * Gets a hunt with the given UUID.
	 * @param uuid the UUID of the hunt to fetch.
	 * @return the hunt or null.
	 */
	public SingleHunt getHunt(UUID uuid) {
		return hunts.get(uuid);
	}

	public SingleHunt addHunt() {
		// If the maximum hunt amount is reached, don't add another.
		if (hunts.size() < PokeHunt.config.getHuntAmount()) {
			SingleHunt hunt = new SingleHunt(owner, this);

			huntsCounter.put(
					hunt.getRarity(),
					huntsCounter.getOrDefault(hunt.getRarity(), 0)+1);

			// If the config setting is enabled, send the broadcast.
			if(PokeHunt.config.isSendHuntBeginMessage()) {
				if (PokeHunt.config.isIndividualHunts()) {
					ServerPlayer player = owner == null ? null : PokeHunt.server.getPlayerList().getPlayer(owner);
					if (owner != null &&
							player != null) {
						Objects.requireNonNull(PokeHunt.server.getPlayerList().getPlayer(owner)).sendSystemMessage(
								Component.literal(
										Utils.formatPlaceholders(
												PokeHunt.language.getNewHuntMessage(), null, hunt.getPokemon()
										)
								)
						);
					}
				} else {
					Utils.broadcastMessage(Utils.formatPlaceholders(
							PokeHunt.language.getNewHuntMessage(), null, hunt.getPokemon()
					));
				}
			}


			species.put(hunt.getId(), hunt.getPokemon().getSpecies());

			// Add the hunt to the list.
			return hunts.put(hunt.getId(), hunt);
		}
		return null;
	}

	public SingleHunt removeHunt(UUID id, boolean broadcast) {
		SingleHunt removedHunt = hunts.remove(id);
		if (removedHunt != null) {
			species.remove(id);
			removedHunt.getTimer().cancel(); // Cancel timer on hunt.

			// If broadcasts are enabled and the method call wants it broadcast, send it.
			if (PokeHunt.config.isIndividualHunts()) {
				if (owner != null && PokeHunt.server.getPlayerList().getPlayer(owner)!=null && !removedHunt.ended() &&
						PokeHunt.config.isSendHuntEndMessage()) {
					Objects.requireNonNull(PokeHunt.server.getPlayerList().getPlayer(owner)).sendSystemMessage(
							Component.literal(
									Utils.formatPlaceholders(
											PokeHunt.language.getEndedHuntMessage(), null, removedHunt.getPokemon()
									)
							)
					);
				}
			} else if (PokeHunt.config.isSendHuntEndMessage() && broadcast && !removedHunt.ended()) {
					Utils.broadcastMessage(Utils.formatPlaceholders(
							PokeHunt.language.getEndedHuntMessage(), null, removedHunt.getPokemon()
					));
			}

		}

		try {
			huntsCounter.put(removedHunt.getRarity(), Math.max(huntsCounter.get(removedHunt.getRarity()) - 1, 0));
		} catch(Exception e) {
			Grove.LOGGER.fatal("SOMETHING WENT WRONG DEALING WITH STUFF AFTER PLAYER CAUGHT A WANTED POKEMON FOR HUNT");
			Grove.LOGGER.fatal(e.getMessage());
			Grove.LOGGER.fatal(e.getLocalizedMessage());
			Grove.LOGGER.fatal(removedHunt==null? removedHunt.getRarity():"eh no rarity found cause the hunt is null"+ "<-Pokemon hunt rarity - -> the hunts counter:");
			for(String str:huntsCounter.keySet()) {
				Grove.LOGGER.fatal(str+" - "+huntsCounter.get(str));
			}
		}

		return removedHunt;
	}

	/**
	 * Removes a hunt with the ID and adds a new one.
	 * @param id the ID of the hunt to remove.
	 * @return the new PokeHunt replacement.
	 */
	public ReplacedHunt replaceHunt(UUID id, boolean broadcast) {
		SingleHunt oldHunt = removeHunt(id, broadcast);


		if (oldHunt != null) {
			return new ReplacedHunt(oldHunt, addHunt());
		}
		return null;
	}

	/**
	 * Checks that a given pokemon matches one from the current hunt pool.
	 * @param pokemon The pokemon to check.
	 * @return UUID of the hunt that matches the hunt.
	 */
	public UUID matches(Pokemon pokemon) {
		for (UUID id : hunts.keySet()) {
			if (hunts.get(id).matches(pokemon)) {
				return id;
			}
		}
		return null;
	}

	public boolean speciesListContains(Species specie) {
		return this.species.containsValue(specie);
	}

	public HashMap<UUID, SingleHunt> getHunts() {
		return hunts;
	}


	public int getRarityCounter(String strRarity) {
		return huntsCounter.getOrDefault(strRarity, 0);
	}

    public void clearCounter() {
        huntsCounter.clear();
    }
}
