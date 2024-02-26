package org.imsouhay.pokehunt.command.basecommand;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.LinkedPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.item.PokemonItem;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import org.imsouhay.LavenderMcServerSide.LavenderMcServerSide;
import org.imsouhay.LavenderMcServerSide.config.Permissions;
import org.imsouhay.pokehunt.PokeHunt;
import org.imsouhay.pokehunt.command.subcommand.RefreshCommand;
import org.imsouhay.pokehunt.command.subcommand.ReloadCommand;
import org.imsouhay.pokehunt.hunts.SingleHunt;
import org.imsouhay.pokehunt.ui.PokeHuntMenu;
import org.imsouhay.pokehunt.util.BaseCommand;
import org.imsouhay.pokehunt.util.Utils;

import java.util.*;

/**
 * Creates the mods base command.
 */
public class HuntCommand extends BaseCommand {

	/**
	 * Constructor to create a new comand.
	 */
	public HuntCommand() {
		// Super needs the command, a list of aliases, permission and array of subcommands.
		super("pokehunt", List.of("hunt"),
				Permissions.INSTANCE.getPermission("HuntBase"), Arrays.asList(new ReloadCommand(),
						new RefreshCommand()));
	}

	// Runs when the base command is run with no subcommands.
	@Override
	public int run(CommandContext<CommandSourceStack> context) {

		if (!context.getSource().isPlayer()) {
			context.getSource().sendSystemMessage(Component.literal("A player must run this command!"));
			return 1;
		}

		ServerPlayer sender = context.getSource().getPlayer();

        assert sender != null;
        UIManager.openUIForcefully(sender, PokeHuntMenu.getPage(sender.getUUID()));

		return 1;
	}
}
