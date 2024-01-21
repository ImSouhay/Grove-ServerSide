package org.imsouhay.poketrainer.command.commands;

import ca.landonjw.gooeylibs2.api.UIManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.imsouhay.poketrainer.ui.MainMenu;

public class BaseCommand {

    public static void build(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(Commands.literal("poketrainer")
                .executes(BaseCommand::run));

    }

    public static int run(CommandContext<CommandSourceStack> context) {

        if(!context.getSource().isPlayer()){
            context.getSource().sendSystemMessage(Component.nullToEmpty("Only Players are allowed to use this command!"));
            return 1;
        }

        UIManager.openUIForcefully(context.getSource().getPlayer(), MainMenu.getPage(context.getSource().getPlayer()));

        return 1;
    }

}
