package org.imsouhay.poketrainer.command.commands.economy;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.imsouhay.Grove.config.Permissions;
import org.imsouhay.poketrainer.account.AccountProvider;

public class BalanceCommand {
    public static void build(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(Commands.literal("PokeTokensBalance")
                .requires(sourceStack -> {
                    if (sourceStack.isPlayer()) {
                        return Permissions.INSTANCE.hasPermission(
                                sourceStack.getPlayer(),
                                Permissions.INSTANCE.getPermission("EconomyBalance"));
                    }
                    return true;}
                )
                .executes(BalanceCommand::runOnSelf)
                .then(Commands.argument("target", EntityArgument.player())
                        .executes(BalanceCommand::run)));

        dispatcher.register(Commands.literal("balance")
                .requires(sourceStack -> {
                    if (sourceStack.isPlayer()) {
                        return Permissions.INSTANCE.hasPermission(
                                sourceStack.getPlayer(),
                                Permissions.INSTANCE.getPermission("EconomyBalance"));
                    }
                    return true;}
                )
                .executes(BalanceCommand::runOnSelf)
                .then(Commands.argument("target", EntityArgument.player())
                        .executes(BalanceCommand::run)));
    }

    public static int run(CommandContext<CommandSourceStack> context) {
        ServerPlayer target;
        try {
            target= EntityArgument.getPlayer(context, "target");
        } catch(Exception e) {
            context.getSource().sendSystemMessage(Component.nullToEmpty("Couldn't find a player with that name!"));
            return 1;
        }


        context.getSource().sendSystemMessage(
                Component.nullToEmpty("§eThe player: \""+target.getGameProfile().getName()+"\" has a balance of " + AccountProvider.getAccount(target.getUUID()).getBalance())
        );
        return 1;
    }

    public static int runOnSelf(CommandContext<CommandSourceStack> context) {
        if (!context.getSource().isPlayer()) {
            context.getSource().sendSystemMessage(Component.nullToEmpty("This command must be ran by a player."));
            return 1;
        }

        context.getSource().sendSystemMessage(
                Component.nullToEmpty("§6You have "+ AccountProvider.getAccount(context.getSource().getPlayer().getUUID()).getBalance()+" tokens."));
        return 1;
    }
}
