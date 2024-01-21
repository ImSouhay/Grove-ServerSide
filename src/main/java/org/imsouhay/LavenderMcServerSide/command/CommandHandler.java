package org.imsouhay.LavenderMcServerSide.command;


import net.minecraftforge.event.RegisterCommandsEvent;
import org.imsouhay.LavenderMcServerSide.LavenderMcServerSide;
import org.imsouhay.LavenderMcServerSide.command.commands.ReloadCommand;
import org.imsouhay.pokedex.command.PokeDexCommandHandler;
import org.imsouhay.poketrainer.command.PokeTrainerCommandHandler;

public class CommandHandler {

    public static void register(RegisterCommandsEvent event){

        if(LavenderMcServerSide.gConfig.isPokeDexEnabled()) PokeDexCommandHandler.register(event);
        if(LavenderMcServerSide.gConfig.isPokeTrainerEnabled()) PokeTrainerCommandHandler.register(event);


        ReloadCommand.build(event.getDispatcher());
    }

}
