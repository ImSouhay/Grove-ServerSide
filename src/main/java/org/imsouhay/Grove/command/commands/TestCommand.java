package org.imsouhay.Grove.command.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.HashMap;

public class TestCommand {

    public static void build(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands
                .literal("testoo")
                .executes(TestCommand::run)
        );
    }

    public static int run(CommandContext<CommandSourceStack> context) {
        HashMap<Integer, String> pokeIndexToName = new HashMap<>();


        return 1;
    }
}
