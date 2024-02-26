package org.imsouhay.pokehunt.command.subcommand;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.imsouhay.LavenderMcServerSide.config.Permissions;
import org.imsouhay.pokehunt.PokeHunt;
import org.imsouhay.pokehunt.hunts.CurrentHunts;
import org.imsouhay.pokehunt.util.Subcommand;
import org.imsouhay.pokehunt.util.Utils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RefreshCommand extends Subcommand {

	public RefreshCommand() {
		super("§9Usage:\\n§3- hunt refresh <player>");
	}

	/**
	 * Method used to add to the base command for this subcommand.
	 * @return source to complete the command.
	 */
	@Override
	public LiteralCommandNode<CommandSourceStack> build() {
		return Commands.literal("refresh")
				.requires(ctx -> {
					if (ctx.isPlayer()) {
						return Permissions.INSTANCE.hasPermission(ctx.getPlayer(),
								Permissions.INSTANCE.getPermission("HuntRefresh"));
					} else {
						return true;
					}
				})
				.executes(this::showUsage)
				.then(Commands.argument("player", StringArgumentType.string())
						.suggests((ctx, builder) -> {
							for (String player : ctx.getSource().getServer().getPlayerNames()) {
								builder.suggest(player);
							}

							return builder.buildFuture();
						})
						.executes(this::run)
				)
				.build();
	}

	/**
	 * Method to perform the logic when the command is executed.
	 * @param context the source of the command.
	 * @return integer to complete command.
	 */
	@Override
	public int run(CommandContext<CommandSourceStack> context) {

		try {
			if (!PokeHunt.config.isIndividualHunts()) {
				context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage(
						"§cIndividual Hunts are not active.", context.getSource().isPlayer()
				)));
				return 1;
			}


			String playerName = StringArgumentType.getString(context, "player");

			if (playerName == null) {
				context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage(
						"§cNo player was given.", context.getSource().isPlayer()
				)));
				return 1;
			}

			ServerPlayer player = PokeHunt.server.getPlayerList().getPlayerByName(playerName);

			if (player == null) {
				context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage(
						"§cPlayer " + playerName + " could not be found.", context.getSource().isPlayer()
				)));
				return 1;
			}

			CurrentHunts playerHunts = PokeHunt.manager.getPlayerHunts(player.getUUID());

			PokeHunt.manager.addPlayer(player.getUUID()); // Adds the player if they dont exist.

			Set<UUID> huntIds = new HashSet<>(playerHunts.getHunts().keySet()); // Gets the players current hunt ids.

			for (UUID hunt : huntIds) {
				playerHunts.removeHunt(hunt, false); // Removes all of the players hunts.
			}

			playerHunts.init(); // Initializes the player again.

			context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage(
					"§2Successfully refreshed " + player.getName().getString() + " hunts.",
					context.getSource().isPlayer())));
		} catch (Exception e) {
			e.printStackTrace();
			context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage(
					"§cSomething went wrong.",
					context.getSource().isPlayer())));
		}

		return 1;
	}
}
