package org.imsouhay.pokedex.command.commands;

import ca.landonjw.gooeylibs2.api.UIManager;
import com.cobblemon.mod.common.pokemon.Species;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.imsouhay.Grove.config.Permissions;
import org.imsouhay.pokedex.PokeDex;
import org.imsouhay.pokedex.account.Account;
import org.imsouhay.pokedex.account.AccountProvider;
import org.imsouhay.pokedex.collection.ImplementedMonsCollection;
import org.imsouhay.pokedex.collection.MonsCollection;
import org.imsouhay.pokedex.ui.DexMenu;

import java.util.ArrayList;

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
		Account playerAccount = AccountProvider.getAccount(player.getUUID());


		ArrayList<Species> needed = new ArrayList<>();
		for(Species specie:PokeDex.config.isImplementedOnly()?
				ImplementedMonsCollection.getList(): MonsCollection.getList()) {
			if(!playerAccount.getPokemon(specie.getName()).isCaught()) {
				needed.add(specie);
			}
		}

		UIManager.openUIForcefully(player, DexMenu.getPage(player.getUUID(), needed));

		return 1;
	}
}
