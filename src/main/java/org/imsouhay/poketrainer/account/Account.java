package org.imsouhay.poketrainer.account;

import com.google.gson.Gson;
import org.imsouhay.Grove.Grove;
import org.imsouhay.Grove.util.Utils;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Account {
    private final UUID uuid;
    private int balance;

    public Account(UUID uuid, int balance) {
        this.uuid=uuid;
        this.balance=balance;
        writeToFile();
    }

    public UUID getUUID() {
        return uuid;
    }

    public int getBalance() {
        return balance;
    }

    public boolean deposit(int value) {
        balance+=value;
        writeToFile();
        return true;
    }

    public boolean withdraw(int value) {
        if(balance<value) return false;

        balance-=value;
        writeToFile();
        return true;
    }

    private void writeToFile() {
        AccountProvider.updateAccount(this);
        Gson gson = Utils.newGson();

        CompletableFuture<Boolean> future = Utils.writeFileAsync(Grove.BASE_PATH+"economy/balances/",
                uuid + ".json", gson.toJson(this));

        if (!future.join()) {
            Grove.LOGGER.error("Unable to write account for " + uuid);
        }
    }
}
