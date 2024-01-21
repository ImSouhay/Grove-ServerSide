package org.imsouhay.poketrainer.ui.button;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.ButtonAction;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import com.cobblemon.mod.common.CobblemonItems;
import net.minecraft.network.chat.Component;
import org.imsouhay.LavenderMcServerSide.LavenderMcServerSide;
import org.imsouhay.LavenderMcServerSide.util.Utils;
import org.imsouhay.poketrainer.PokeTrainer;
import org.imsouhay.poketrainer.builder.PokeBuilder;
import org.imsouhay.poketrainer.economy.TransactionHandler;
import org.imsouhay.poketrainer.util.Operation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class LevelEditButton {

    private static List<Button> buttons;

    public static List<Button> getButtons(Operation operation, PokeBuilder builder) {
        makeButtons(operation, builder);
        return buttons;
    }

    private static void makeButtons(Operation operation, PokeBuilder builder) {
        buttons=new LinkedList<>(Arrays.asList(
                buildButton(3, operation, builder),
                buildButton(5, operation, builder),
                buildButton(10, operation, builder),
                buildButton(15, operation, builder),
                buildButton(20, operation, builder),
                buildButton(30, operation, builder),
                buildButton(40, operation, builder),
                buildButton(50, operation, builder)
        ));
    }

    private static Button buildButton(int num, Operation operation, PokeBuilder builder) {
        return GooeyButton.builder()
                .title(operation.toChar()+""+num+" Levels")
                .lore(buildLore(num, operation))
                .display(CobblemonItems.RARE_CANDY.getDefaultInstance())
                .onClick(e -> handleLevelEdit(e, operation, num, builder))
                .build();
    }

    private static ArrayList<String> buildLore(int num, Operation operation) {
        String defaultLore="Click to @incORdec level by @num levels!";

        return new ArrayList<>(List.of(
                "§7" + defaultLore
                        .replace("@incORdec", operation.toString())
                        .replace("@num", String.valueOf(num)), "", Utils.price(PokeTrainer.config.getPriceOf(operation.toChar()+""+num+"_level"))));


    }

    private static void handleLevelEdit(ButtonAction e, Operation operation, int value, PokeBuilder builder) {
        TransactionHandler.handleWithdraw(
                e,
                PokeTrainer.config.getPriceOf(operation.toChar()+""+value+"_level"),
                () -> {
                        if((builder.getLevel()!=100&&operation==Operation.PLUS) || (builder.getLevel()!=1&&operation==Operation.MINUS)) {
                            LavenderMcServerSide.LOGGER.info("lvl: "+builder.getLevel());
                            builder.editLevel(
                                    switch (operation) {
                                        case PLUS -> value;
                                        case MINUS -> -value;
                                    }
                            );
                            if(PokeTrainer.config.isFeedbackEnabled()) builder.getOwner().sendSystemMessage(Component.nullToEmpty("You have " + operation + " your " + builder.getName() + "'s level by "+value+" for a price of " + PokeTrainer.config.getPriceOf((operation.toChar() + "" + value + "_level")) + "."));
                            return true;
                        } else {
                            if(PokeTrainer.config.isFeedbackEnabled()) e.getPlayer().sendSystemMessage(Component.nullToEmpty("§cYour pokemon lvl is "+builder.getLevel()+"! You can't "+operation+" anymore!"));
                            return false;
                        }
                }

    );

    }
}
