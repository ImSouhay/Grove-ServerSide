package org.imsouhay.pokedex.event;


import net.minecraftforge.event.entity.player.PlayerEvent;
import org.imsouhay.pokedex.account.AccountProvider;

public class JoinEvent{
    public static void onPlayerJoinServer(PlayerEvent.PlayerLoggedInEvent event){
        AccountProvider.getAccount(event.getEntity().getUUID());
    }
}
