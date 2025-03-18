package org.imsouhay.pokehunt.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kotlin.Unit;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.imsouhay.pokehunt.PokeHunt;
import org.imsouhay.pokehunt.api.event.HuntEvents;
import org.imsouhay.pokehunt.api.event.events.CompletedEvent;
import org.imsouhay.pokehunt.hunts.ReplacedHunt;
import org.imsouhay.pokehunt.hunts.SingleHunt;
import org.imsouhay.pokehunt.util.Utils;

import java.util.TimerTask;
import java.util.UUID;

public abstract class EventHandler {
	public static void registerEvents() {
		// Capture event checks the current hunts.
		CobblemonEvents.POKEMON_CAPTURED.subscribe(Priority.NORMAL, e -> {

			ServerPlayer player = e.getPlayer();
			Pokemon pokemon = e.getPokemon();

			UUID matchedUUID;

			if (PokeHunt.config.isIndividualHunts()) {
				matchedUUID = PokeHunt.manager.getPlayerHunts(player.getUUID()).matches(pokemon);
			} else {
				matchedUUID = PokeHunt.hunts.matches(pokemon);
			}

			// If the pokemon doesn't match a hunt pokemon, matchedUUID will be null, otherwise it'll be a UUID.
			// ^ wtf was the guy who first wrote this was thinking about? like bro, that's obvious, go explain the actual logic
			// ^ naaah stfu
			if (matchedUUID != null) {
				SingleHunt hunt;

				if (PokeHunt.config.isIndividualHunts()) {
					hunt = PokeHunt.manager.getPlayerHunts(player.getUUID()).getHunt(matchedUUID);
				} else {
					hunt = PokeHunt.hunts.getHunt(matchedUUID);
				}

				if(hunt.isDone()) {
					return Unit.INSTANCE;
				}


				if (!PokeHunt.config.isIndividualHunts()) {
					Utils.broadcastMessage(Utils.formatPlaceholders(
							PokeHunt.language.getCaptureHuntBroadcast(), player, pokemon
					));
				} else {
					player.sendSystemMessage(
							Component.literal(Utils.formatPlaceholders(
									PokeHunt.language.getCaptureHuntBroadcast(), player, pokemon
					)));
				}


				if (!hunt.getCommands().isEmpty()) {
					try {

						Utils.runCommands(
								hunt.getCommands(),
								player,
								pokemon
						);

					} catch (NullPointerException ex) {
						// If any errors occur, send log to console.
						PokeHunt.LOGGER.error("Could not process hunt " + matchedUUID + " for " + player.getName().getString());
						// Just in case player list is empty for some random reason.
						ex.printStackTrace();
					}
				}
				if(PokeHunt.config.isCoolDownEnabled()) {
					hunt.end();
					hunt.getTimer().schedule(new TimerTask() {
						@Override
						public void run() {
							hunt.startNewHunt();
						}
					}, PokeHunt.config.getCoolDown(hunt.getRarity())*60*1000);
					hunt.setCaught();
					hunt.addToEndTime(PokeHunt.config.getCoolDown(hunt.getRarity())*60*1000);
					HuntEvents.COMPLETED.trigger(new CompletedEvent(hunt, player.getUUID()));
				} else if(PokeHunt.config.isStartNewHuntOnCatch()) {
					ReplacedHunt replacedHunt;
					if (PokeHunt.config.isIndividualHunts()) {
						replacedHunt = PokeHunt.manager.getPlayerHunts(player.getUUID())
								.replaceHunt(matchedUUID, false);
					} else {
						replacedHunt = PokeHunt.hunts.replaceHunt(matchedUUID, false);
					}

					if (replacedHunt != null) {
						HuntEvents.COMPLETED.trigger(new CompletedEvent(replacedHunt.getOldHunt(), player.getUUID()));
					}
				} else {
					hunt.setCaught();
				}

				return Unit.INSTANCE;
			}
			return Unit.INSTANCE;
		});
	}
}
