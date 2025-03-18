package org.imsouhay.Grove.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import kotlin.Unit;
import org.imsouhay.Grove.Grove;
import org.imsouhay.Grove.util.Utils;
import org.imsouhay.pokedex.account.AccountProvider;
import org.imsouhay.pokedex.collection.ImplementedMonsCollection;
import org.imsouhay.pokedex.collection.MonsCollection;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class PokemonSpeciesEvent {
    public static void registerEvent() {
        PokemonSpecies.INSTANCE.getObservable().subscribe(Priority.NORMAL, e -> {

            ImplementedMonsCollection.init();
            MonsCollection.init();

            Grove.TRIE.load();
            Grove.IMPLEMENTED_TRIE.load();

            AccountProvider.init();

            HashMap<Integer, String> pokeIndexToName = new HashMap<>();

            for(Species specie:PokemonSpecies.INSTANCE.getSpecies()) {
                pokeIndexToName.put(specie.getNationalPokedexNumber(), specie.getName());
            }
            Grove.LOGGER.info(new File("").getAbsolutePath());
            File file = new File(new File("").getAbsolutePath()+ Grove.BASE_PATH+"stuff.json");
            try {
                file.createNewFile();
            } catch (IOException ignored) {}

            Utils.writeFileSync(file,
                    Utils.newGson().toJson(pokeIndexToName));

            return Unit.INSTANCE;
        });
    }
}

