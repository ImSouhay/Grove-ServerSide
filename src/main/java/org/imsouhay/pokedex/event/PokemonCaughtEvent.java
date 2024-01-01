package org.imsouhay.pokedex.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import kotlin.Unit;
import org.imsouhay.pokedex.util.Dexutils;

public class PokemonCaughtEvent {
	public void registerEvent() {
		CobblemonEvents.POKEMON_CAPTURED.subscribe(Priority.NORMAL, e -> {

			Dexutils.checkDex(e.getPokemon(), e.getPlayer());

			return Unit.INSTANCE;
		});
	}
}
