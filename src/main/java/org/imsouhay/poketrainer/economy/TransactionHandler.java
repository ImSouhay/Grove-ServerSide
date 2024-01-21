package org.imsouhay.poketrainer.economy;

import ca.landonjw.gooeylibs2.api.button.ButtonAction;
import org.imsouhay.LavenderMcServerSide.util.Utils;
import org.imsouhay.poketrainer.PokeTrainer;
import org.imsouhay.poketrainer.account.AccountProvider;

import java.util.function.BooleanSupplier;

public class TransactionHandler {

    public static void handleWithdraw(ButtonAction e, int value, BooleanSupplier supplier) {
        if(AccountProvider.getAccount(e.getPlayer().getUUID()).getBalance()<value) {
            if(PokeTrainer.config.isFeedbackEnabled()) Utils.balanceNotEnough(e);
            return;
        }

        if (!supplier.getAsBoolean()) return;

        AccountProvider.getAccount(e.getPlayer().getUUID()).withdraw(value);
    }

}
