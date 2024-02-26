package org.imsouhay.LavenderMcServerSide;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.imsouhay.LavenderMcServerSide.config.GeneralConfig;
import org.imsouhay.LavenderMcServerSide.config.Permissions;
import org.imsouhay.LavenderMcServerSide.event.EventListeners;
import org.imsouhay.pokedex.PokeDex;
import org.imsouhay.pokedex.event.*;
import org.imsouhay.pokehunt.PokeHunt;
import org.imsouhay.poketrainer.PokeTrainer;


@Mod(LavenderMcServerSide.MOD_ID)
public class LavenderMcServerSide {
    public static final String MOD_ID = "lavenderserverside";
    public static final String BASE_PATH = "/config/" + MOD_ID + "/";
    public static final Logger LOGGER = LogManager.getLogger("LavenderMcServerSide");

    public LavenderMcServerSide() {
        init();

        MinecraftForge.EVENT_BUS.register(this);

        new EventListeners();

        new EvolutionEvent().registerEvent();
        new PokemonCaughtEvent().registerEvent();
        new StarterEvent().registerEvent();
        new TradeEvent().registerEvent();
    }

    public static void init(){
        GeneralConfig.INSTANCE.init();
        Permissions.INSTANCE.init();

        if(GeneralConfig.INSTANCE.isPokeDexEnabled()) PokeDex.load();
        if(GeneralConfig.INSTANCE.isPokeTrainerEnabled()) PokeTrainer.load();
        if(GeneralConfig.INSTANCE.isPokeHuntIsEnabled()) PokeHunt.init();
    }
}
