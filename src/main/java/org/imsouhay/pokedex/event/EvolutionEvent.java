package org.imsouhay.pokedex.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import kotlin.Unit;
import net.minecraft.server.level.ServerPlayer;
import org.imsouhay.pokedex.util.Dexutils;

public class EvolutionEvent {
	public void registerEvent() {
		CobblemonEvents.EVOLUTION_COMPLETE.subscribe(Priority.NORMAL, e -> {
			ServerPlayer player=e.getPokemon().getOwnerPlayer();
			if(player!=null) {
				Dexutils.checkDex(e.getPokemon(), player);
			}

			return Unit.INSTANCE;
		});
	}
}
