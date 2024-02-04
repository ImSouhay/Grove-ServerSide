package org.imsouhay.pokedex.ui;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import org.imsouhay.LavenderMcServerSide.util.Utils;
import org.imsouhay.pokedex.PokeDex;
import org.imsouhay.pokedex.account.Account;
import org.imsouhay.pokedex.account.AccountProvider;
import org.imsouhay.pokedex.config.Reward;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class RewardsMenu {
	public Page getPage(UUID player, CommandSourceStack sourceStack) {
		Account account = AccountProvider.getAccount(player);
		double progress = Utils.getDexProgress(account);

		int highestSlot = -1;
		HashMap<Integer, Button> rewards = new HashMap<>();
		for (Reward reward : PokeDex.config.getRewards()) {
			// Finds the highest slot for the row amount.
			if (reward.getSlotNumber() > highestSlot) {
				highestSlot = reward.getSlotNumber();
			}

			GooeyButton.Builder button = GooeyButton.builder()
					.display(Utils.parseItemId(reward.getItemMaterial()))
					.title("§3" + (int) reward.getProgress() + "%");

            ArrayList<String> lore = new ArrayList<>(reward.getLore());

			if (progress >= reward.getProgress()) {
				if (account.getReward(reward.getProgress()).isRedeemed()) {
					lore.add(PokeDex.config.getRewardAlreadyClaimed());
				} else {
					lore.add(PokeDex.config.getRewardClaimable());
					button.onClick(e -> {

						for(String literalCommand:reward.getCommands()){
							String finalCommand=Utils.formatCommand(literalCommand, e.getPlayer().getName().getString());
							Objects.requireNonNull(e.getPlayer().getServer()).getCommands()
									.performPrefixedCommand(e.getPlayer().getServer().createCommandSourceStack(),
															finalCommand
							);
						}



						account.redeemReward(reward.getProgress());
						UIManager.closeUI(e.getPlayer());
						e.getPlayer().sendSystemMessage(Component.nullToEmpty(
								PokeDex.config.getClaimingMessage().replace("@progress", String.valueOf(reward.getProgress()))
						));
					});
				}
			} else {
				lore.add(PokeDex.config.getPercentageNeededToClaim().replace("@progress", String.valueOf(reward.getProgress())));
				lore.add(PokeDex.config.getCurrentProgress().replace("@progress", String.valueOf(new BigDecimal(progress).setScale(2, RoundingMode.HALF_EVEN).floatValue())));
			}
			button.lore(lore);
			rewards.put(reward.getSlotNumber(), button.build());
		}

		int rows = (int) Math.ceil((double) highestSlot / 9);

		Button filler = GooeyButton.builder()
				.display(Utils.parseItemId(PokeDex.lang.getFillerMaterial()))
				.title("")
				.lore(new ArrayList<>())
				.hideFlags(FlagType.All)
				.build();

		ChestTemplate.Builder template = ChestTemplate.builder(Math.min(rows + 1, 6))
				.fill(filler);

		for (int slotNumber : rewards.keySet()) {
			template.set(slotNumber, rewards.get(slotNumber));
		}

		return GooeyPage.builder()
				.title(PokeDex.lang.getTitle()+" - Rewards")
				.template(template.build())
				.build();
	}


}
