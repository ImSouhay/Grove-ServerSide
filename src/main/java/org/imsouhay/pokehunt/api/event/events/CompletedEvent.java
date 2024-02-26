package org.imsouhay.pokehunt.api.event.events;

import org.imsouhay.pokehunt.hunts.SingleHunt;

import java.util.UUID;

public class CompletedEvent {
	private final SingleHunt hunt;
	private final UUID player;

	public CompletedEvent(SingleHunt hunt, UUID player) {
		this.hunt = hunt;
		this.player = player;
	}

	public SingleHunt getHunt() {
		return hunt;
	}

	public UUID getPlayer() {
		return player;
	}
}
