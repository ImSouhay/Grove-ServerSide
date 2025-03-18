package org.imsouhay.pokedex.command.commands;

import ca.landonjw.gooeylibs2.api.UIManager;
import com.cobblemon.mod.common.command.argument.PokemonArgumentType;
import com.cobblemon.mod.common.pokemon.Species;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.imsouhay.Grove.config.Permissions;
import org.imsouhay.pokedex.PokeDex;
import org.imsouhay.pokedex.collection.ImplementedMonsCollection;
import org.imsouhay.pokedex.collection.MonsCollection;
import org.imsouhay.pokedex.ui.DexMenu;

import java.util.List;

public class BaseCommand  {
	public static void build(CommandDispatcher<CommandSourceStack> dispatcher) {

		dispatcher.register(
				Commands.literal("dex")
						.requires(sourceStack -> {
							if (sourceStack.isPlayer()) {
								return Permissions.INSTANCE.hasPermission(
										sourceStack.getPlayer(),
										Permissions.INSTANCE.getPermission("PokeDexBase"));
							}
							return true;}
						)
                		.executes(BaseCommand::run)
                );

        dispatcher.register(
                Commands.literal("pokedex")
						.requires(sourceStack -> {
							if (sourceStack.isPlayer()) {
								return Permissions.INSTANCE.hasPermission(
										sourceStack.getPlayer(),
										Permissions.INSTANCE.getPermission("PokeDexBase"));
							}
							return true;}
						)
                        .executes(BaseCommand::run)
        );
	}

	private static int run(CommandContext<CommandSourceStack> context) {

		if (!context.getSource().isPlayer()) {
			context.getSource().sendSystemMessage(Component.nullToEmpty("This command must be ran by a player."));
            return 1;
		}

		ServerPlayer player = context.getSource().getPlayer();

        UIManager.openUIForcefully(player, DexMenu.getPage(player.getUUID() ,
				PokeDex.config.isImplementedOnly()?
				ImplementedMonsCollection.getList() : MonsCollection.getList()));
		return 1;
	}

	private static int runSearch(CommandContext<CommandSourceStack> context) {
		Species species= PokemonArgumentType.Companion.getPokemon(context, "Pokemon");

		ServerPlayer player = context.getSource().getPlayer();

		UIManager.openUIForcefully(
				player,
				DexMenu.getPage(player.getUUID(), List.of(species)));

		return 1;
	}
}
