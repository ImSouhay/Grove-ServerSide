package org.imsouhay.pokedex.ui;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.imsouhay.LavenderMcServerSide.LavenderMcServerSide;
import org.imsouhay.pokedex.account.Account;
import org.imsouhay.pokedex.account.AccountProvider;
import org.imsouhay.pokedex.config.Reward;
import org.imsouhay.pokedex.util.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class RewardsMenu {
	public Page getPage(UUID player, CommandSourceStack sourceStack) {
		Account account = AccountProvider.getAccount(player);
		double progress = Utils.getDexProgress(account);

		int highestSlot = -1;
		HashMap<Integer, Button> rewards = new HashMap<>();
		for (Reward reward : LavenderMcServerSide.config.getRewards()) {
			// Finds the highest slot for the row amount.
			if (reward.getSlotNumber() > highestSlot) {
				highestSlot = reward.getSlotNumber();
			}

			GooeyButton.Builder button = GooeyButton.builder()
					.display(Utils.parseItemId(reward.getItemMaterial()))
					.title("§3" + (int) reward.getProgress() + "%");

			ArrayList<String> lore = new ArrayList<>();

			if (progress >= reward.getProgress()) {
				if (account.getReward(reward.getProgress()).isRedeemed()) {
					lore.add("§bYou have already claimed this reward");
				} else {
					lore.add("§aYou can claim this reward!");
					button.onClick(e -> {
						CommandSourceStack sourceStack1=new CommandSourceStack(
								CommandSource.NULL,
								Vec3.ZERO,
								Vec2.ZERO,
								(ServerLevel)null,
								4,
								"PokeDex",
								Component.nullToEmpty("PokeDex"),
								sourceStack.getServer(),
								null);

						for(String literalCommand:reward.getCommands()){
							String finalCommand=Utils.formatCommand(literalCommand, e.getPlayer().getName().getString());
							e.getPlayer().getServer().getCommands()
									.performPrefixedCommand(sourceStack1,
															finalCommand
							);
						}



						account.redeemReward(reward.getProgress());
						UIManager.closeUI(e.getPlayer());
						e.getPlayer().sendSystemMessage(Component.nullToEmpty("§c[Pokedex] §2You successfully redeemed the "
								+ (int) reward.getProgress() + "% dex rewards."));
					});
				}
			} else {
				lore.add("§cYou need " + reward.getProgress() + " to claim this reward");
				lore.add("§6Current Progress: " +
						new BigDecimal(progress).setScale(2, RoundingMode.HALF_EVEN).floatValue()
				+ "%");
			}
			button.lore(lore);
			rewards.put(reward.getSlotNumber(), button.build());
		}

		int rows = (int) Math.ceil((double) highestSlot / 9);

		Button filler = GooeyButton.builder()
				.display(Utils.parseItemId(LavenderMcServerSide.lang.getFillerMaterial()))
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
				.title(LavenderMcServerSide.lang.getTitle()+" - Rewards")
				.template(template.build())
				.build();
	}


}
