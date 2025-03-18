package org.imsouhay.pokehunt.command.subcommand;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.imsouhay.Grove.config.Permissions;
import org.imsouhay.pokehunt.PokeHunt;
import org.imsouhay.pokehunt.util.Subcommand;
import org.imsouhay.pokehunt.util.Utils;

import java.util.ArrayList;
import java.util.UUID;

public class ReloadCommand extends Subcommand {

	public ReloadCommand() {
		super("§9Usage:\\n§3- hunt reload");
	}

	/**
	 * Method used to add to the base command for this subcommand.
	 * @return source to complete the command.
	 */
	@Override
	public LiteralCommandNode<CommandSourceStack> build() {
		return Commands.literal("reload")
				.requires(ctx -> {
					if (ctx.isPlayer()) {
						return Permissions.INSTANCE.hasPermission(ctx.getPlayer(),
								Permissions.INSTANCE.getPermission("HuntReload"));
					} else {
						return true;
					}
				})
				.executes(this::run)
				.build();
	}

	/**
	 * Method to perform the logic when the command is executed.
	 * @param context the source of the command.
	 * @return integer to complete command.
	 */
	@Override
	public int run(CommandContext<CommandSourceStack> context) {

		// Removes current hunts, the case the config size changes.
		ArrayList<UUID> uuids = new ArrayList<>(PokeHunt.hunts.getHunts().keySet());
		for (UUID hunt : uuids) {
			PokeHunt.hunts.removeHunt(hunt, true);
		}

		PokeHunt.load();

		context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage(
				PokeHunt.language.getReloadMessage(),
				context.getSource().isPlayer())));

		return 1;
	}
}
