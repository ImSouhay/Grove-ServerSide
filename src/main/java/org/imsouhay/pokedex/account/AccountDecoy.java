package org.imsouhay.pokedex.account;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import org.imsouhay.pokedex.PokeDex;
import org.imsouhay.pokedex.config.Reward;
import org.imsouhay.pokedex.dex.DexEntry;
import org.imsouhay.pokedex.dex.DexEntryDecoy;
import org.imsouhay.pokedex.dex.RewardProgress;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;


/*
    There once was a genius dev who made a minecraft mod, and it was using dex number as
    a universal identifier in all of its features, but later, stuff changed, and multiple pokemons
    started having the same dex number, so the genius dev fixed all the features and started using
    the pokemons names as identifiers, however, some users still had the config files using
    the dex number, so the genius dev had an idea, make a decoy class, that uses the dex number
    read the configs, and then transform it into the new gud class.
    I am the genius dev :-D
 */
public class AccountDecoy {
    private UUID uuid;
    private HashMap<Double, RewardProgress> rewards;
    private HashMap<Integer, DexEntryDecoy> pokemon;
    private boolean isComplete;

    public AccountDecoy() {
        pokemon=null;
    }

    public boolean isCaught(Integer dexNumber) {
        return pokemon.get(dexNumber).isCaught();
    }

    public UUID getUuid() {
        return uuid;
    }

    public HashMap<Double, RewardProgress> getRewards() {
        return rewards;
    }

    public HashMap<Integer, DexEntryDecoy> getPokemon() {
        return pokemon;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public boolean isNull() {
        return pokemon==null;
    }
}
