package org.imsouhay.pokedex.account;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import com.google.gson.Gson;
import org.imsouhay.Grove.Grove;
import org.imsouhay.Grove.util.Utils;
import org.imsouhay.pokedex.PokeDex;
import org.imsouhay.pokedex.config.Reward;
import org.imsouhay.pokedex.dex.DexEntry;
import org.imsouhay.pokedex.dex.RewardProgress;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class Account {

	private final UUID uuid;
	private HashMap<Double, RewardProgress> rewards;
	private HashMap<String, DexEntry> pokemon;
	private boolean isComplete;

	public Account(UUID uuid) {
		this.uuid = uuid;
		isComplete = false;

		pokemon = new HashMap<>();
		for (Species species : PokemonSpecies.INSTANCE.getSpecies()) {
			pokemon.put(species.getName(),
					new DexEntry(species.getName(), false));
		}

		rewards = new HashMap<>();
		for (Reward reward : PokeDex.config.getRewards()) {
			rewards.put(reward.getProgress(), new RewardProgress(reward.getProgress(), false, false));
		}
		writeToFile();
	}

	public RewardProgress getReward(double progress) {
		return rewards.get(progress);
	}

	public ArrayList<RewardProgress> getAllRewards() {
		return new ArrayList<>(rewards.values());
	}

	public void completeReward(double progress) {
		rewards.put(progress, new RewardProgress(progress, true, false));
		writeToFile();
	}

	public void redeemReward(double progress) {
		rewards.put(progress, new RewardProgress(progress, true, true));
		writeToFile();
	}

	public void addAllRewards(List<Double> rewardList) {
		double progress = Utils.getDexProgress(this);
		for (double number : rewardList) {
			rewards.put(number, new RewardProgress(number, progress >= number, false));
		}
		writeToFile();
	}

	public void setRewards(HashMap<Double, RewardProgress> rewards) {
		this.rewards = rewards;
		writeToFile();
	}

	public Collection<DexEntry> getAllPokemons() {
		return pokemon.values();
	}


	public DexEntry getPokemon(String pokeName) {
		if(!pokemon.containsKey(pokeName)) {
			addPokemon(pokeName);
		}

		return pokemon.get(pokeName);
	}

	public void setCaught(String pokeName, boolean isCaught) {
		pokemon.put(pokeName, new DexEntry(pokeName, isCaught));
		writeToFile();
	}

	public void addPokemon(String pokeName) {
		pokemon.put(pokeName, new DexEntry(pokeName, false));
		writeToFile();
	}

	public void addAllPokemon(List<String> pokeNames) {
		for (String pokeName : pokeNames) {
			if(!pokemon.containsKey(pokeName)) {
				pokemon.put(pokeName, new DexEntry(pokeName, false));
			}
		}
		writeToFile();
	}

	public ArrayList<DexEntry> getCaught() {
		ArrayList<DexEntry> caught = new ArrayList<>();
		for (DexEntry entry : pokemon.values()) {
			if (entry.isCaught()) {
				caught.add(entry);
			}
		}
		return caught;
	}

	public ArrayList<DexEntry> getNeeded() {
		ArrayList<DexEntry> needed = new ArrayList<>();
		for (DexEntry entry : pokemon.values()) {
			if (!entry.isCaught()) {
				needed.add(entry);
			}
		}
		return needed;
	}

	public void setPokemon(HashMap<String, DexEntry> pokemon) {
		this.pokemon = pokemon;
		writeToFile();
	}

	public UUID getUuid() {
		return uuid;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean complete) {
		isComplete = complete;
		writeToFile();
	}

	public int getPokemonCount() {
		return pokemon.size();
	}

	public void writeToFile() {
		AccountProvider.updateAccount(this);
		Gson gson = Utils.newGson();

		CompletableFuture<Boolean> future = Utils.writeFileAsync(PokeDex.POKE_DEX_PATH + "accounts/",
				uuid + ".json", gson.toJson(this));

		if (!future.join()) {
			Grove.LOGGER.error("Unable to write account for " + uuid);
		}
	}
}
