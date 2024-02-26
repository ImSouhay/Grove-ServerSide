package org.imsouhay.pokehunt.config;

/**
 * Stores all rewards.
 */
public class RewardsConfig {
	private final RewardConfig common; // Common rewards.
	private final RewardConfig uncommon; // Uncommon rewards.
	private final RewardConfig rare; // Rare rewards.
	private final RewardConfig ultraRare; // UltraRare rewards.

	public RewardsConfig() {
		common = new RewardConfig("give {player} minecraft:diamond 1");
		uncommon = new RewardConfig("give {player} minecraft:diamond 2");
		rare = new RewardConfig("give {player} minecraft:diamond 3");
		ultraRare = new RewardConfig("give {player} minecraft:diamond 4");
	}

	public RewardConfig getCommon() {
		return common;
	}

	public RewardConfig getUncommon() {
		return uncommon;
	}

	public RewardConfig getRare() {
		return rare;
	}

	public RewardConfig getUltraRare() {
		return ultraRare;
	}
}
