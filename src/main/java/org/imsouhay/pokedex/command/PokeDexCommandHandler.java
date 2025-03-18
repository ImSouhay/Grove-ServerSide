package org.imsouhay.pokedex.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import org.imsouhay.pokedex.command.commands.*;
import org.imsouhay.pokedex.collection.ImplementedMonsCollection;
import org.imsouhay.pokedex.collection.MonsCollection;


public class PokeDexCommandHandler {
    public static void register(RegisterCommandsEvent event){
        CommandDispatcher<CommandSourceStack> dispatcher=event.getDispatcher();

        BaseCommand.build(dispatcher);
        CaughtCommand.build(dispatcher);
        NeededCommand.build(dispatcher);
        RewardsCommand.build(dispatcher);
    }

}
