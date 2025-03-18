package org.imsouhay.poketrainer.economy;

import ca.landonjw.gooeylibs2.api.button.ButtonAction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.imsouhay.Grove.util.Utils;
import org.imsouhay.poketrainer.PokeTrainer;
import org.imsouhay.poketrainer.account.AccountProvider;

import java.util.function.BooleanSupplier;

public class TransactionManager {

    public static void handleWithdraw(ButtonAction e, int value, BooleanSupplier supplier) {
        if(AccountProvider.getAccount(e.getPlayer().getUUID()).getBalance()<value) {
            if(PokeTrainer.config.isFeedbackEnabled()) Utils.balanceNotEnough(e);
            return;
        }

        if (!supplier.getAsBoolean()) return;

        AccountProvider.getAccount(e.getPlayer().getUUID()).withdraw(value);


    }

    public static void handleTransfer(ServerPlayer sender, ServerPlayer receiver, int value) {
        if(AccountProvider.getAccount(sender.getUUID()).getBalance()<value) {
            Utils.balanceNotEnough(sender);
            return;
        }

        AccountProvider.getAccount(sender.getUUID()).withdraw(value);
        AccountProvider.getAccount(receiver.getUUID()).deposit(value);

        sender.sendSystemMessage(Component.nullToEmpty(PokeTrainer.fBack.get("TokensSent")
                .replace("@value", String.valueOf(value))
                .replace("@receiver", receiver.getGameProfile().getName())));

        receiver.sendSystemMessage(Component.nullToEmpty(PokeTrainer.fBack.get("TokensReceived")
                .replace("@value", String.valueOf(value))
                .replace("@sender", sender.getGameProfile().getName())));
    }

}
