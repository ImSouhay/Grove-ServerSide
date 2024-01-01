package org.imsouhay.pokedex.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import kotlin.Unit;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.imsouhay.pokedex.util.Dexutils;

public class TradeEvent {
	public void registerEvent() {
		CobblemonEvents.TRADE_COMPLETED.subscribe(Priority.NORMAL, e -> {

			ServerPlayer player1 =
                    ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(e.getTradeParticipant1().getUuid());

			ServerPlayer player2 =
                    ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(e.getTradeParticipant2().getUuid());

			if (player1 != null) {
				Dexutils.checkDex(e.getTradeParticipant1Pokemon(), player1);
			}

			if (player2 != null) {
				Dexutils.checkDex(e.getTradeParticipant2Pokemon(), player2);
			}

			return Unit.INSTANCE;
		});
	}
}
