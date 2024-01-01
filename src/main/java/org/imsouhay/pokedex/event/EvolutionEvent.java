package org.imsouhay.pokedex.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import kotlin.Unit;
import org.imsouhay.pokedex.util.Dexutils;

public class EvolutionEvent {
	public void registerEvent() {
		CobblemonEvents.EVOLUTION_COMPLETE.subscribe(Priority.NORMAL, e -> {

			Dexutils.checkDex(e.getPokemon(), e.getPokemon().getOwnerPlayer());

			return Unit.INSTANCE;
		});
	}
}
