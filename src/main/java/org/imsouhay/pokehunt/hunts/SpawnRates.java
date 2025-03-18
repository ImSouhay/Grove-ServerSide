package org.imsouhay.pokehunt.hunts;

import com.cobblemon.mod.common.api.spawning.CobblemonSpawnPools;
import com.cobblemon.mod.common.api.spawning.detail.SpawnDetail;
import com.cobblemon.mod.common.pokemon.Pokemon;

import java.util.HashMap;

/**
 * Loads and stores the rarity for each Pokemon to be referenced.
 */
public class SpawnRates {

	// Stores all rarities to easily reference for a price.
	HashMap<String, String> rarity;

	public SpawnRates() {
		rarity = new HashMap<>();
	}

	public void init() {
		for(SpawnDetail detail:CobblemonSpawnPools.WORLD_SPAWN_POOL.getDetails()) {
			rarity.put(detail.getName().toString().split("\\.")[2].toLowerCase(), detail.getBucket().getName());
		}
	}

	/**
	 * Gets the rarity hashmap.
	 * @return rarity of given pokemon as a float.
	 */
	public String getRarity(Pokemon pokemon) {
		return rarity.get(pokemon.getSpecies().getName().toLowerCase());
	}
}
