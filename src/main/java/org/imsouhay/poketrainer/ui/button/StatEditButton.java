package org.imsouhay.poketrainer.ui.button;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.ButtonAction;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.imsouhay.Grove.util.Utils;
import org.imsouhay.poketrainer.PokeTrainer;
import org.imsouhay.poketrainer.builder.PokeBuilder;
import org.imsouhay.poketrainer.economy.TransactionManager;
import org.imsouhay.poketrainer.util.Operation;
import org.imsouhay.poketrainer.util.vType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.imsouhay.Grove.util.Utils.format;

public class StatEditButton {
    
    private static List<Button> buttons;
    public static List<Button> getStatButtons(vType type, Stat stat,
                                              Operation operation, Item displayItem, PokeBuilder builder) {
        
        makeButtons(type, stat, operation, displayItem, builder);
        
        return buttons;
    }
    
    private static void makeButtons(vType type, Stat stat,
                                   Operation operation, Item displayItem, PokeBuilder builder) {
        Button filler=GooeyButton.builder()
                .title("")
                .hideFlags(FlagType.All)
                .lore(new ArrayList<>())
                .display(Utils.parseItemId(PokeTrainer.lang.getFillerMaterial()))
                .build();

        switch (type) {
            case EV -> makeEVButtons(stat, operation, displayItem, builder, filler);
            case IV -> makeIVButtons(stat, operation, displayItem, builder, filler);
        }
    }

    private static void makeIVButtons(Stat stat, Operation operation,
                                      Item displayItem, PokeBuilder builder, Button filler) {
        buttons=new LinkedList<>(Arrays.asList(
                filler,
                buildButton(stat, vType.IV, operation, displayItem, 1, builder),
                buildButton(stat, vType.IV, operation, displayItem, 3, builder),
                buildButton(stat, vType.IV, operation, displayItem, 5, builder),
                filler,
                filler,
                buildButton(stat, vType.IV, operation, displayItem, 10, builder),
                buildButton(stat, vType.IV, operation, displayItem, 15, builder),
                buildButton(stat, vType.IV, operation, displayItem, 31, builder),
                filler
        ));
    }

    private static void makeEVButtons(Stat stat, Operation operation,
                                      Item displayItem, PokeBuilder builder, Button filler) {
        buttons=new LinkedList<>(Arrays.asList(
                buildButton(stat, vType.EV, operation, displayItem, 3, builder),
                buildButton(stat, vType.EV, operation, displayItem, 5, builder),
                buildButton(stat, vType.EV, operation, displayItem, 10, builder),
                buildButton(stat, vType.EV, operation, displayItem, 15, builder),
                buildButton(stat, vType.EV, operation, displayItem, 20, builder),
                filler,
                buildButton(stat, vType.EV, operation, displayItem, 30, builder),
                buildButton(stat, vType.EV, operation, displayItem, 40, builder),
                buildButton(stat, vType.EV, operation, displayItem, 50, builder),
                filler
        ));
    }


    private static Button buildButton(Stat stat, vType type, Operation operation, Item displayItem, int num, PokeBuilder builder) {
        char plusOrMinus= operation.toChar();

        String statName=stat.getIdentifier().getPath();
        
        return GooeyButton.builder()
                .title("§a"+plusOrMinus+num+" "+format(statName)+" "+type+"(s)")
                .display(new ItemStack(displayItem))
                .lore(buildLore(statName, type.toString(), operation, num))
                .onClick(e -> {
                    handleVEdit(e, type, stat, operation, num, builder);
                })
                .build();
    }

    private static ArrayList<String> buildLore(String statName,String vType, Operation operation, int num) {
        String defaultLore="§7Click to @incORdec @statName @ivORevs by @num @ivORev(s)!";
        return new ArrayList<>(List.of(defaultLore
                        .replace("@num", String.valueOf(num))
                        .replace("@incORdec", operation.toString())
                        .replace("@statName", format(statName))
                        .replaceAll("@ivORev", vType),
                "",
                Utils.price(PokeTrainer.config.getPriceOf((operation.toChar()+""+num+"_"+vType)))));
    }

    private static void handleVEdit(ButtonAction e, vType type, Stat stat, Operation operation ,int value, PokeBuilder builder) {
        TransactionManager.handleWithdraw(
                e,
                PokeTrainer.config.getPriceOf((operation.toChar()+""+value+"_"+type.toString())),
                () -> {
                    if(type==vType.EV) {
                        return handleEVEdit(e, type, stat, operation, value, builder);
                    }
                    if(type==vType.IV) {
                        return handleIVEdit(e, type, stat, operation, value, builder);
                    }
                    return false;
                }

                );
    }

    private static boolean handleEVEdit(ButtonAction e, vType type, Stat stat,
                                 Operation operation ,int value, PokeBuilder builder) {
        int previousStatValue = builder.getEvStats().get(stat);
        if((builder.getEvStats().get(stat)!=builder.getEvs().getAcceptableRange().getLast()&&operation==Operation.PLUS) ||
                (builder.getEvStats().get(stat)!=0&&operation==Operation.MINUS)) {
            builder.editV(type, stat, operation==Operation.PLUS? value:-value);
            if(PokeTrainer.config.isFeedbackEnabled()) Utils.sendFeedBack(
                    e.getPlayer(),
                    "statEdit",
                    String.valueOf(PokeTrainer.config.getPriceOf(operation.toChar()+""+value+"_EV")),
                    builder.getName(),
                    operation.toString(),
                    format(stat.getIdentifier().getPath()),
                    type.toString(),
                    Math.abs(builder.getEvStats().get(stat) - previousStatValue)
            );
            builder.reloadButton();
            return true;
        } else {
            if(PokeTrainer.config.isFeedbackEnabled()) Utils.sendFeedBack(
                    e.getPlayer(),
                    "statLimitReached",
                    String.valueOf(PokeTrainer.config.getPriceOf(operation.toChar()+""+value+"_EV")),
                    builder.getName(),
                    operation.toString(),
                    format(stat.getIdentifier().getPath()),
                    type.toString(),
                    builder.getEvStats().get(stat)
            );
            return false;
        }
    }

    private static boolean handleIVEdit(ButtonAction e, vType type, Stat stat,
                                 Operation operation ,int value, PokeBuilder builder) {
        int previousStatValue = builder.getIvStats().get(stat);
        if((builder.getIvStats().get(stat)!=builder.getIvs().getAcceptableRange().getLast()&&operation==Operation.PLUS) ||
                (builder.getIvStats().get(stat)!=0&&operation==Operation.MINUS)) {

            builder.editV(type, stat, operation==Operation.PLUS? value:-value);

            if(PokeTrainer.config.isFeedbackEnabled()) Utils.sendFeedBack(
                    e.getPlayer(),
                    "statEdit",
                    String.valueOf(PokeTrainer.config.getPriceOf(operation.toChar()+""+value+"_IV")),
                    builder.getName(),
                    operation.toString(),
                    format(stat.getIdentifier().getPath()),
                    type.toString(),
                    Math.abs(builder.getIvStats().get(stat) - previousStatValue)
            );
            builder.reloadButton();
            return true;
        } else {
            if(PokeTrainer.config.isFeedbackEnabled()) Utils.sendFeedBack(
                    e.getPlayer(),
                    "statLimitReached",
                    String.valueOf(PokeTrainer.config.getPriceOf(operation.toChar()+""+value+"_IV")),
                    builder.getName(),
                    operation.toString(),
                    format(stat.getIdentifier().getPath()),
                    type.toString(),
                    builder.getIvStats().get(stat)
            );

            return false;
        }
    }

}
