package org.imsouhay.LavenderMcServerSide;


import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.imsouhay.LavenderMcServerSide.command.CommandHandler;
import org.imsouhay.LavenderMcServerSide.config.GeneralConfig;
import org.imsouhay.pokedex.PokeDex;
import org.imsouhay.pokedex.config.Config;
import org.imsouhay.pokedex.config.Lang;
import org.imsouhay.pokedex.event.*;
import org.imsouhay.poketrainer.PokeTrainer;
import org.imsouhay.poketrainer.account.AccountProvider;


@Mod(LavenderMcServerSide.MOD_ID)
public class LavenderMcServerSide {
    public static final String MOD_ID = "lavenderserverside";
    public static final String BASE_PATH = "/config/" + MOD_ID + "/";
    public static final Logger LOGGER = LogManager.getLogger();
    public static Lang lang;
    public static final GeneralConfig gConfig=new GeneralConfig();

    public LavenderMcServerSide() {
        load();

        MinecraftForge.EVENT_BUS.register(this);


        new EvolutionEvent().registerEvent();
        new PokemonCaughtEvent().registerEvent();
        new StarterEvent().registerEvent();
        new TradeEvent().registerEvent();
    }

    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent event){
        CommandHandler.register(event);
    }


    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if(gConfig.isPokeDexEnabled()) JoinEvent.onPlayerJoinServer(event);
        if(gConfig.isPokeTrainerEnabled()) AccountProvider.registerPlayer(event.getEntity());
    }

    public static void load(){
        gConfig.init();

        if(gConfig.isPokeDexEnabled()) PokeDex.load();
        if(gConfig.isPokeTrainerEnabled()) PokeTrainer.load();
    }
}
