package org.imsouhay.LavenderMcServerSide.command.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.imsouhay.LavenderMcServerSide.LavenderMcServerSide;
import org.imsouhay.LavenderMcServerSide.config.Permissions;
import org.imsouhay.pokehunt.PokeHunt;

public class ReloadCommand {

    public static void build(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands
                .literal("LavenderReload")
				.requires(sourceStack -> {
					if (sourceStack.isPlayer()) {
						return Permissions.INSTANCE.hasPermission(
								sourceStack.getPlayer(),
								Permissions.INSTANCE.getPermission("Reload"));
					}
					return true;}
				)
                .executes(ReloadCommand::run)
        );
    }

	public static int run(CommandContext<CommandSourceStack> context) {

		LavenderMcServerSide.init();
		PokeHunt.load();

		context.getSource().sendSystemMessage(Component.nullToEmpty("Config reloaded."));

		return 1;
	}
}
