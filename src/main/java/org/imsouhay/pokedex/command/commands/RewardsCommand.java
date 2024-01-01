package org.imsouhay.pokedex.command.commands;

import ca.landonjw.gooeylibs2.api.UIManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.imsouhay.pokedex.ui.RewardsMenu;


public class RewardsCommand {

    public static void build(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands
                .literal("rewards")
                .executes(RewardsCommand::run)
        );
    }

	public static int run(CommandContext<CommandSourceStack> context) {
        if (!context.getSource().isPlayer()) {
            context.getSource().sendSystemMessage(Component.nullToEmpty("This command must be ran by a player."));
            return 1;
        }
		ServerPlayer player = context.getSource().getPlayer();

        assert player != null;
        UIManager.openUIForcefully(player, new RewardsMenu().getPage(player.getUUID(), context.getSource()));

		return 1;
	}
}
