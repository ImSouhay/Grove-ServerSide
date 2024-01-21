package org.imsouhay.poketrainer.account;

import com.google.gson.Gson;
import net.minecraft.world.entity.player.Player;
import org.imsouhay.LavenderMcServerSide.LavenderMcServerSide;
import org.imsouhay.LavenderMcServerSide.util.Utils;
import org.imsouhay.poketrainer.PokeTrainer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AccountProvider {
    private static Map<UUID, Account> accounts;

    public static void registerPlayer(Player player) {
        UUID playerUUID=player.getUUID();

        if(accounts.containsKey(playerUUID)) return;
        accounts.put(playerUUID,  new Account(playerUUID, PokeTrainer.config.getDefaultBalance()));
    }

    public static Account getAccount(UUID uuid) {
        if (!accounts.containsKey(uuid)) {
            accounts.put(uuid, new Account(uuid, PokeTrainer.config.getDefaultBalance()));
        }
        return accounts.get(uuid);
    }

    public static void updateAccount(Account account) {
        accounts.put(account.getUUID(), account);
    }

    public static void init() {
        accounts = new HashMap<>();

        File dir = Utils.checkForDirectory(LavenderMcServerSide.BASE_PATH + "economy/balances/");

        String[] files = dir.list();

        if(files==null) return;

        for (String file : files) {
            Utils.readFileAsync(LavenderMcServerSide.BASE_PATH + "economy/balances/", file, el -> {
                Gson gson = Utils.newGson();
                Account account = gson.fromJson(el, Account.class);
                accounts.put(account.getUUID(), account);
            });
        }
    }
}
