package org.imsouhay.pokehunt.hunts;

import net.minecraft.server.level.ServerPlayer;
import org.imsouhay.pokehunt.PokeHunt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class HuntManager {
	private final HashMap<UUID, CurrentHunts> playerHunts;

	public HuntManager() {
		playerHunts = new HashMap<>();
	}

	public CurrentHunts getPlayerHunts(UUID player) {
		return playerHunts.get(player);
	}

	public HashSet<UUID> getPlayers() {
		return new HashSet<>(playerHunts.keySet());
	}

	public void addPlayer(UUID player) {
		if (playerHunts.get(player) == null && PokeHunt.config.isIndividualHunts()) {
			CurrentHunts hunts = new CurrentHunts(player);
			hunts.init();
			playerHunts.put(player, hunts);
		}
	}

	public void init() {
		for (ServerPlayer player : PokeHunt.server.getPlayerList().getPlayers()) {
			addPlayer(player.getUUID());
		}
	}
}
