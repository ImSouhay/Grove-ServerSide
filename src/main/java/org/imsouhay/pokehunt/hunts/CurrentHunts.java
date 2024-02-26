package org.imsouhay.pokehunt.hunts;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.imsouhay.LavenderMcServerSide.config.Permissions;
import org.imsouhay.pokehunt.PokeHunt;
import org.imsouhay.pokehunt.util.Utils;

import java.util.HashMap;
import java.util.UUID;

public class CurrentHunts {
	private final UUID owner; // The owner of the current hunts.
	private final HashMap<UUID, SingleHunt> hunts; // List of current hunts.
	private final HashMap<UUID, Species> species; // List of species.

	/**
	 * Constructor that generates a bunch of hunts when the server starts.
	 */
	public CurrentHunts(UUID owner) {
		this.owner = owner;
		hunts = new HashMap<>();
		species = new HashMap<>();
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

	/**
	 * Adds a new hunt to the current hunts.
	 * @return true if successful.
	 */
	public SingleHunt addHunt() {
		// If the maximum hunt amount is reached, don't add another.
		if (hunts.size() < PokeHunt.config.getHuntAmount()) {
			SingleHunt hunt = new SingleHunt(owner);

			// If a species matches, add hunt again.
			if (species.containsValue(hunt.getPokemon().getSpecies()) ||
			PokeHunt.config.blacklistContains(hunt.getPokemon().getSpecies().getName())) {
				return addHunt();
			}

			// If the config setting is enabled, send the broadcast.

			if (PokeHunt.config.isIndividualHunts()) {
				ServerPlayer player = owner == null ? null : PokeHunt.server.getPlayerList().getPlayer(owner);
				if (owner != null &&
						player != null &&
				Permissions.INSTANCE.hasPermission(player,
						Permissions.INSTANCE.getPermission("HuntNotify"))) {
					PokeHunt.server.getPlayerList().getPlayer(owner).sendSystemMessage(
							Component.literal(
									Utils.formatPlaceholders(
											PokeHunt.language.getNewHuntMessage(), null, hunt.getPokemon()
									)
							)
					);
				}

			} else {
				if (PokeHunt.config.isSendHuntBeginMessage()) {
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

	/**
	 * Removes a hunt from the list.
	 * @param id the ID of the hunt to remove.
	 * @return true if successfully removed.
	 */
	public SingleHunt removeHunt(UUID id, boolean broadcast) {
		SingleHunt removedHunt = hunts.remove(id);
		if (removedHunt != null) {
			species.remove(id);
			removedHunt.getTimer().cancel(); // Cancel timer on hunt.

			// If broadcasts are enabled and the method call wants it broadcast, send it.
			if (PokeHunt.config.isIndividualHunts()) {
				if (owner != null &&
						PokeHunt.server.getPlayerList().getPlayer(owner) != null &&
						Permissions.INSTANCE.hasPermission(PokeHunt.server.getPlayerList().getPlayer(owner),
								Permissions.INSTANCE.getPermission("HuntNotify"))) {
					PokeHunt.server.getPlayerList().getPlayer(owner).sendSystemMessage(
							Component.literal(
									Utils.formatPlaceholders(
											PokeHunt.language.getEndedHuntMessage(), null, removedHunt.getPokemon()
									)
							)
					);
				}
			} else {
				if (PokeHunt.config.isSendHuntEndMessage() && broadcast) {
					Utils.broadcastMessage(Utils.formatPlaceholders(
							PokeHunt.language.getEndedHuntMessage(), null, removedHunt.getPokemon()
					));
				}
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


	/**
	 * Gets all of the hunts.
	 * @return
	 */
	public HashMap<UUID, SingleHunt> getHunts() {
		return hunts;
	}
}
