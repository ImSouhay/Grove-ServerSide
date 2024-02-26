package org.imsouhay.poketrainer.command.commands.economy;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.imsouhay.LavenderMcServerSide.config.Permissions;
import org.imsouhay.poketrainer.account.Account;
import org.imsouhay.poketrainer.account.AccountProvider;

public class WithdrawCommand {
    public static void build(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(Commands.literal("PokeTokensWithdraw")
                .requires(sourceStack -> {
                    if (sourceStack.isPlayer()) {
                        return Permissions.INSTANCE.hasPermission(
                                sourceStack.getPlayer(),
                                Permissions.INSTANCE.getPermission("EconomyWithdraw"));
                    }
                    return true;}
                )
                .then(Commands.argument("player", StringArgumentType.word())
                        .then(Commands.argument("value", IntegerArgumentType.integer(1))
                                .executes(WithdrawCommand::run))));


    }

    public static int run(CommandContext<CommandSourceStack> context) {
        String playerName= context.getArgument("player", String.class);

        ServerPlayer player= context.getSource().getServer().getPlayerList().getPlayerByName(
                playerName);

        if(player==null) {
            context.getSource().sendSystemMessage(Component.nullToEmpty("Can't find player with name: "+playerName));
            return 1;
        }

        Account account= AccountProvider.getAccount(player.getUUID());
        int value=context.getArgument("value", Integer.class);

        if(account.withdraw(value)){
        context.getSource().sendSystemMessage(
                Component.nullToEmpty("The player: \""+playerName+"\" got a withdraw of "+ value +" and now has a total of " + account.getBalance()));
        } else {
            context.getSource().sendSystemMessage(
                    Component.nullToEmpty("The player: \""+playerName+"\" doesn't have enough tokens to withdraw "+ value +", his balance is: " + account.getBalance())
            );
        }

        return 1;
    }
}
