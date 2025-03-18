package org.imsouhay.Grove.data;

import org.imsouhay.pokedex.collection.ImplementedMonsCollection;

public class ImplementedPokemonTrie extends PokemonTrie {
    @Override
    public void load() {
        ImplementedMonsCollection.getList().forEach(specie -> insert(specie.getName().toLowerCase(), specie));
    }
}
