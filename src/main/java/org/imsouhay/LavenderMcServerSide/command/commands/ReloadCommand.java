package org.imsouhay.LavenderMcServerSide.command.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.imsouhay.LavenderMcServerSide.LavenderMcServerSide;

public class ReloadCommand {

    public static void build(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands
                .literal("reload")
                .executes(ReloadCommand::run)
        );
    }

	public static int run(CommandContext<CommandSourceStack> context) {

		LavenderMcServerSide.load();

		context.getSource().sendSystemMessage(Component.nullToEmpty("Config reloaded."));

		return 1;
	}
}
