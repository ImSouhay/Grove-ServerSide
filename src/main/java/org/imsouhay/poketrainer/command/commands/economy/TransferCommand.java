package org.imsouhay.poketrainer.command.commands.economy;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.imsouhay.Grove.config.Permissions;
import org.imsouhay.poketrainer.economy.TransactionManager;

public class TransferCommand {

    public static void build(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("transfer")
                .requires(sourceStack -> {
                    if (sourceStack.isPlayer()) {
                        return Permissions.INSTANCE.hasPermission(
                                sourceStack.getPlayer(),
                                Permissions.INSTANCE.getPermission("EconomyTransfer"));
                    }
                    return true;}
                )
                .then(Commands.argument("receiver", EntityArgument.player())
                        .then(Commands.argument("value", IntegerArgumentType.integer(1))
                                .executes(TransferCommand::run))));
    }

    public static int run(CommandContext<CommandSourceStack> context) {
        if (!context.getSource().isPlayer()) {
            context.getSource().sendSystemMessage(Component.nullToEmpty("This command must be ran by a player."));
            return 1;
        }


        ServerPlayer sender = context.getSource().getPlayer();
        ServerPlayer receiver;
        try {
            receiver = EntityArgument.getPlayer(context, "receiver");
        } catch (Exception e) {
            context.getSource().sendSystemMessage(Component.nullToEmpty("§cCan't find a player with that name!"));
            return 1;
        }

        if(sender == receiver) {
            context.getSource().sendSystemMessage(Component.nullToEmpty("§cYou can't transfer tokens to yourself!"));
            return 1;
        }

        TransactionManager.handleTransfer(sender, receiver, context.getArgument("value", Integer.class));

        return 1;
    }
}