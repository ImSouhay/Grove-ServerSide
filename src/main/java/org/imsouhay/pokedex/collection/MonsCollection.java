package org.imsouhay.pokedex.collection;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MonsCollection {

    private static final List<Species> list = new ArrayList<>();

    public static synchronized void init() {
        list.addAll(PokemonSpecies.INSTANCE.getSpecies());

        Comparator<Species> comparator = Comparator.comparingInt(Species::getNationalPokedexNumber);

        list.sort(comparator);
    }

    public static List<Species> getList() {
        return new ArrayList<>(list);
    }

}
