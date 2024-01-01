package org.imsouhay.LavenderMcServerSide;


import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.imsouhay.pokedex.account.AccountProvider;
import org.imsouhay.pokedex.command.CommandHandler;
import org.imsouhay.pokedex.config.Config;
import org.imsouhay.pokedex.config.Lang;
import org.imsouhay.pokedex.event.*;


@Mod(LavenderMcServerSide.MOD_ID)
public class LavenderMcServerSide {
    public static final String MOD_ID = "lavenderserverside";
    public static final String BASE_PATH = "/config/" + MOD_ID + "/";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final Config config = new Config();
    public static final Lang lang = new Lang();

    public LavenderMcServerSide() {
        MinecraftForge.EVENT_BUS.register(this);


        new EvolutionEvent().registerEvent();
        new PokemonCaughtEvent().registerEvent();
        new StarterEvent().registerEvent();
        new TradeEvent().registerEvent();

        load();
    }

    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent event){
        CommandHandler.register(event);
    }
    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {JoinEvent.onPlayerJoinServer(event);}

    public static void load(){
        config.init();
        lang.init();
        AccountProvider.init();
    }
}
