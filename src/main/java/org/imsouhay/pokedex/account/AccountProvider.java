package org.imsouhay.pokedex.account;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import com.google.gson.Gson;
import static org.imsouhay.Grove.Grove.LOGGER;
import org.imsouhay.Grove.util.Utils;
import org.imsouhay.pokedex.PokeDex;
import org.imsouhay.pokedex.config.Reward;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public abstract class AccountProvider {
	private static HashMap<UUID, Account> accounts;

	public static Account getAccount(UUID uuid) {
		if (!accounts.containsKey(uuid)) {
			accounts.put(uuid, new Account(uuid));
		}
		return accounts.get(uuid);
	}

	public static void updateAccount(Account account) {
		accounts.put(account.getUuid(), account);
	}

	public static void init() {
		accounts = new HashMap<>();

		File dir = Utils.checkForDirectory(PokeDex.POKE_DEX_PATH + "accounts/");

		String[] files = dir.list();

		if (files == null || files.length == 0) {
			return;
		}
		for (String file : files) {
			Utils.readFileAsync(PokeDex.POKE_DEX_PATH + "accounts/", file, el -> {
				Gson gson = Utils.newGson();
				try {
					Account account = gson.fromJson(el, Account.class);
					accounts.put(account.getUuid(), account);
				} catch(Exception e) {
					LOGGER.error("Something went wrong handling player dex data file: "+file
					+"\nperhaps its not actually a data file?");
					LOGGER.error(e.getLocalizedMessage());
				}
			});
		}

		int totalPokemon = PokemonSpecies.INSTANCE.getSpecies().size();

		// If there are more Pokemon than in the account, add the new ones.
		for (Account acc : accounts.values()) {
			if (acc.getPokemonCount() < totalPokemon) {
				ArrayList<String> newPokemons = new ArrayList<>();
				for(Species specie:PokemonSpecies.INSTANCE.getSpecies()) {
					newPokemons.add(specie.getName());
				}
				acc.addAllPokemon(newPokemons);
			}

			ArrayList<Double> rewardProgresses = new ArrayList<>();
			for (Reward reward : PokeDex.config.getRewards()) {
				if (acc.getReward(reward.getProgress()) == null) {
					rewardProgresses.add(reward.getProgress());
				}
			}
			acc.addAllRewards(rewardProgresses);
		}
	}

//	private static Account translateDecoyAccount(AccountDecoy decoy) {
//		Account account = new Account(decoy.getUuid());
//		account.setRewards(decoy.getRewards());
//		account.setComplete(decoy.isComplete());
//
//		HashMap<String, DexEntry> pokemons = new HashMap<>();
//
//
//		for(Species specie: PokemonSpecies.INSTANCE.getSpecies()) {
//			pokemons.put(specie.getName(), new DexEntry(specie.getName(), decoy.isCaught(specie.getNationalPokedexNumber())));
//		}
//
//		account.setPokemon(pokemons);
//
//		return account;
//	}
}
