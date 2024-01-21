package org.imsouhay.pokedex.command.commands;

import ca.landonjw.gooeylibs2.api.UIManager;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.imsouhay.pokedex.PokeDex;
import org.imsouhay.pokedex.ui.DexMenu;

import java.util.ArrayList;
import java.util.Collection;

public class BaseCommand  {
	public static void build(CommandDispatcher<CommandSourceStack> dispatcher) {

		dispatcher.register(
				Commands.literal("dex")
                		.executes(BaseCommand::run)
                );

        dispatcher.register(
                Commands.literal("pokedex")
                        .executes(BaseCommand::run)
        );
	}

	private static int run(CommandContext<CommandSourceStack> context) {

		if (!context.getSource().isPlayer()) {
			context.getSource().sendSystemMessage(Component.nullToEmpty("This command must be ran by a player."));
            return 1;
		}

		ServerPlayer player = context.getSource().getPlayer();

		Collection<Species> list = new ArrayList<>();
		if (PokeDex.config.isImplementedOnly()) {
			list.addAll(PokemonSpecies.INSTANCE.getImplemented());
		} else {
			list.addAll(PokemonSpecies.INSTANCE.getSpecies());
		}

		UIManager.openUIForcefully(player, new DexMenu().getPage(player.getUUID() , list));
		return 1;
	}
}
