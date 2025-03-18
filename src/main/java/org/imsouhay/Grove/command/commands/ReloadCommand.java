package org.imsouhay.Grove.command.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.imsouhay.Grove.Grove;
import org.imsouhay.Grove.config.Permissions;
import org.imsouhay.pokehunt.PokeHunt;

public class ReloadCommand {

	public static void build(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands
				.literal("LavenderReload")
				.requires(ReloadCommand::checkPermissions) // Extract permission check logic
				.executes(ReloadCommand::run)
		);
	}

	/**
	 * Checks if the command source has permission to execute the reload command.
	 *
	 * @param sourceStack The command source stack.
	 * @return True if the source has permission, otherwise false.
	 */
	private static boolean checkPermissions(CommandSourceStack sourceStack) {
		if (sourceStack.isPlayer()) {
			return Permissions.INSTANCE.hasPermission(
					sourceStack.getPlayer(),
					Permissions.INSTANCE.getPermission("Reload")
			);
		}
		return true; // Non-player sources (e.g., console) always have permission
	}

	/**
	 * Executes the reload command.
	 *
	 * @param context The command context.
	 * @return 1 if successful.
	 */
	public static int run(CommandContext<CommandSourceStack> context) {
		reloadConfigurations(); // Extract reload logic
		notifySuccess(context); // Extract success notification logic
		return 1;
	}

	/**
	 * Reloads configurations for Grove and PokeHunt.
	 */
	private static void reloadConfigurations() {
		Grove.init();
		PokeHunt.load();
	}

	/**
	 * Sends a success message to the command source.
	 *
	 * @param context The command context.
	 */
	private static void notifySuccess(CommandContext<CommandSourceStack> context) {
		context.getSource().sendSystemMessage(Component.nullToEmpty("Config reloaded."));
	}
}