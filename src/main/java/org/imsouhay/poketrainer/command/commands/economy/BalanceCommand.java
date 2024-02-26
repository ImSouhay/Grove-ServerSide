package org.imsouhay.poketrainer.command.commands.economy;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.imsouhay.LavenderMcServerSide.config.Permissions;
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
                .then(Commands.argument("player", StringArgumentType.word())
                        .executes(BalanceCommand::run)));
    }

    public static int run(CommandContext<CommandSourceStack> context) {
        String playerName= context.getArgument("player", String.class);

        ServerPlayer player= context.getSource().getServer().getPlayerList().getPlayerByName(
                playerName);

        if(player==null) {
            context.getSource().sendSystemMessage(Component.nullToEmpty("Can't find player with name: "+playerName));
            return 1;
        }

        context.getSource().sendSystemMessage(
                Component.nullToEmpty("The player: \""+playerName+"\" has a balance of " + AccountProvider.getAccount(player.getUUID()).getBalance())
        );
        return 1;
    }
}
