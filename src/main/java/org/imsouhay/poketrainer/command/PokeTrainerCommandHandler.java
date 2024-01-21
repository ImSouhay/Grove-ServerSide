package org.imsouhay.poketrainer.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import org.imsouhay.poketrainer.command.commands.BaseCommand;
import org.imsouhay.poketrainer.command.commands.economy.BalanceCommand;
import org.imsouhay.poketrainer.command.commands.economy.DepositCommand;
import org.imsouhay.poketrainer.command.commands.economy.WithdrawCommand;

public class PokeTrainerCommandHandler {

    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher=event.getDispatcher();

        BaseCommand.build(dispatcher);
        BalanceCommand.build(dispatcher);
        DepositCommand.build(dispatcher);
        WithdrawCommand.build(dispatcher);
    }
}