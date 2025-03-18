package org.imsouhay.Grove;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.imsouhay.Grove.config.GeneralConfig;
import org.imsouhay.Grove.config.Permissions;
import org.imsouhay.Grove.data.ImplementedPokemonTrie;
import org.imsouhay.Grove.data.PokemonTrie;
import org.imsouhay.Grove.event.EventListeners;
import org.imsouhay.pokedex.PokeDex;
import org.imsouhay.pokedex.event.*;
import org.imsouhay.pokehunt.PokeHunt;
import org.imsouhay.poketrainer.PokeTrainer;


@Mod(Grove.MOD_ID)
public class Grove {
    public static final String MOD_ID = "grove";
    public static final String BASE_PATH = "/config/" + MOD_ID + "/";
    public static final Logger LOGGER = LogManager.getLogger("grove");
    public static final PokemonTrie TRIE=new PokemonTrie();
    public static final ImplementedPokemonTrie IMPLEMENTED_TRIE=new ImplementedPokemonTrie();

    public Grove() {
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
