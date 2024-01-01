package org.imsouhay.pokedex.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import org.imsouhay.pokedex.command.commands.*;

public class CommandHandler {
    public static void register(RegisterCommandsEvent event){
        CommandDispatcher<CommandSourceStack> dispatcher=event.getDispatcher();

        BaseCommand.build(dispatcher);
        CaughtCommand.build(dispatcher);
        NeededCommand.build(dispatcher);
        ReloadCommand.build(dispatcher);
        RewardsCommand.build(dispatcher);
    }

}
