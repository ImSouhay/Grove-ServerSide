package org.imsouhay.pokehunt.config;

import java.util.ArrayList;

/**
 * Data for a single rarity value.
 */
public class RewardConfig {
	private final ArrayList<String> commands;

	public RewardConfig(String command) {
		this.commands = new ArrayList<>();
		this.commands.add(command);
	}

	public ArrayList<String> getCommands() {
		return commands;
	}
}
