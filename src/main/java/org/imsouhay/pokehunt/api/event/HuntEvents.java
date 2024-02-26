package org.imsouhay.pokehunt.api.event;

import org.imsouhay.pokehunt.api.event.events.CompletedEvent;

public abstract class HuntEvents {
	public static Event<CompletedEvent> COMPLETED = new Event<>();
}
