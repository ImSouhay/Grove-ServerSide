package org.imsouhay.pokedex.command.commands;

import ca.landonjw.gooeylibs2.api.UIManager;
import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.imsouhay.LavenderMcServerSide.config.Permissions;
import org.imsouhay.pokedex.PokeDex;
import org.imsouhay.pokedex.account.AccountProvider;
import org.imsouhay.pokedex.dex.DexEntry;
import org.imsouhay.pokedex.ui.DexMenu;

import java.util.ArrayList;
import java.util.Collections;

public class NeededCommand {
	public static void build(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands
                .literal("needed")
				.requires(sourceStack -> {
					if (sourceStack.isPlayer()) {
						return Permissions.INSTANCE.hasPermission(
								sourceStack.getPlayer(),
								Permissions.INSTANCE.getPermission("PokeDexNeeded"));
					}
					return true;}
				)
                .executes(NeededCommand::run)
        );
	}

	public static int run(CommandContext<CommandSourceStack> context) {
        if (!context.getSource().isPlayer()) {
            context.getSource().sendSystemMessage(Component.nullToEmpty("This command must be ran by a player."));
            return 1;
        }

		ServerPlayer player = context.getSource().getPlayer();

		ArrayList<DexEntry> needed = AccountProvider.getAccount(player.getUUID()).getNeeded();

		ArrayList<Species> species = new ArrayList<>();
		for (DexEntry entry : needed) {
			if (PokeDex.config.isImplementedOnly()) {
				Species specie=PokemonSpecies.INSTANCE.getByPokedexNumber(entry.getDexNumber(), Cobblemon.MODID);
				if (specie!=null && specie.getImplemented()) {
					species.add(PokemonSpecies.INSTANCE.getByPokedexNumber(entry.getDexNumber(), Cobblemon.MODID));
				}
			} else {
				species.add(PokemonSpecies.INSTANCE.getByPokedexNumber(entry.getDexNumber(), Cobblemon.MODID));
			}

		}

		UIManager.openUIForcefully(player, new DexMenu().getPage(player.getUUID(), species));

		return 1;
	}
}
