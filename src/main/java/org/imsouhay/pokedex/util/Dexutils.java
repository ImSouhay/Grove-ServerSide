package org.imsouhay.pokedex.util;

import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.imsouhay.Grove.util.Utils;
import org.imsouhay.pokedex.PokeDex;
import org.imsouhay.pokedex.account.Account;
import org.imsouhay.pokedex.account.AccountProvider;
import org.imsouhay.pokedex.config.Reward;
import org.imsouhay.pokedex.dex.RewardProgress;

public abstract class Dexutils {
	public static void checkDex(Pokemon pokemon, ServerPlayer player) {
		String pokeName = pokemon.getSpecies().getName();
		Account account = AccountProvider.getAccount(player.getUUID());


			if (!account.getPokemon(pokeName).isCaught()) {
				account.setCaught(pokeName, true);

				double progress = Utils.getDexProgress(account);

				// Checks if any rewards have been met and completes them.
				for (Reward reward : PokeDex.config.getRewards()) {
					if (progress >= reward.getProgress() &&
							!account.getReward(reward.getProgress()).isComplete()) {
						account.completeReward(reward.getProgress());
						player.sendSystemMessage(Component.nullToEmpty(
								"§2You have unlocked a Pokedex reward!"));
					}
				}

				boolean isComplete = true;
				for (RewardProgress rwd : account.getAllRewards()) {
					if (!rwd.isComplete()) {
						isComplete = false;
					}
				}
				if (isComplete) {
					account.setComplete(true);
				}
			}

	}
}
