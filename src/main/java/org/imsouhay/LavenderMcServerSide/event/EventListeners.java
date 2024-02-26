package org.imsouhay.LavenderMcServerSide.event;


import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.imsouhay.LavenderMcServerSide.command.CommandHandler;
import org.imsouhay.pokedex.event.JoinEvent;
import org.imsouhay.pokehunt.PokeHunt;
import org.imsouhay.pokehunt.util.Utils;
import org.imsouhay.poketrainer.account.AccountProvider;

import static org.imsouhay.LavenderMcServerSide.config.GeneralConfig.INSTANCE;

public class EventListeners {
    public EventListeners() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent event){
        CommandHandler.register(event);
    }


    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if(INSTANCE.isPokeDexEnabled()) JoinEvent.onPlayerJoinServer(event);
        if(INSTANCE.isPokeTrainerEnabled()) AccountProvider.registerPlayer(event.getEntity());
        if(INSTANCE.isPokeHuntIsEnabled()) PokeHunt.manager.addPlayer(event.getEntity().getUUID());
    }

    @SubscribeEvent
    public void serverStartedEvent(ServerStartedEvent event) {
        if(INSTANCE.isPokeHuntIsEnabled()) PokeHunt.load();
    }

    @SubscribeEvent
    public void serverStoppingEvent(ServerStoppingEvent event) {
        Utils.removeAllHunts();
    }

    @SubscribeEvent
    public void worldLoadEvent(LevelEvent.Load event) {
        if(INSTANCE.isPokeHuntIsEnabled()) PokeHunt.server = event.getLevel().getServer();
    }


}
