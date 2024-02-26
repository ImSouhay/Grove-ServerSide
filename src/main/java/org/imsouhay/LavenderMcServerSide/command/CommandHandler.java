package org.imsouhay.LavenderMcServerSide.command;


import net.minecraftforge.event.RegisterCommandsEvent;
import org.imsouhay.LavenderMcServerSide.command.commands.ReloadCommand;
import org.imsouhay.LavenderMcServerSide.config.GeneralConfig;
import org.imsouhay.pokedex.command.PokeDexCommandHandler;
import org.imsouhay.pokehunt.util.CommandsRegistry;
import org.imsouhay.poketrainer.command.PokeTrainerCommandHandler;

public class CommandHandler {

    public static void register(RegisterCommandsEvent event){

        if(GeneralConfig.INSTANCE.isPokeDexEnabled()) PokeDexCommandHandler.register(event);
        if(GeneralConfig.INSTANCE.isPokeTrainerEnabled()) PokeTrainerCommandHandler.register(event);
        if(GeneralConfig.INSTANCE.isPokeHuntIsEnabled()) CommandsRegistry.registerCommands(event.getDispatcher());

        ReloadCommand.build(event.getDispatcher());
    }

}
