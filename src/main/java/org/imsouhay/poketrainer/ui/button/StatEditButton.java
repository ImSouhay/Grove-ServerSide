package org.imsouhay.poketrainer.ui.button;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.ButtonAction;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.imsouhay.LavenderMcServerSide.LavenderMcServerSide;
import org.imsouhay.LavenderMcServerSide.util.Utils;
import org.imsouhay.poketrainer.PokeTrainer;
import org.imsouhay.poketrainer.builder.PokeBuilder;
import org.imsouhay.poketrainer.economy.TransactionHandler;
import org.imsouhay.poketrainer.util.Operation;
import org.imsouhay.poketrainer.util.vType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.imsouhay.LavenderMcServerSide.util.Utils.format;

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
        
        buttons=new LinkedList<>(Arrays.asList(
                buildButton(stat, type, operation, displayItem, 3, builder),
                buildButton(stat, type, operation, displayItem, 5, builder),
                buildButton(stat, type, operation, displayItem, 10, builder),
                buildButton(stat, type, operation, displayItem, 15, builder),
                buildButton(stat, type, operation, displayItem, 20, builder),
                filler,
                buildButton(stat, type, operation, displayItem, 30, builder),
                buildButton(stat, type, operation, displayItem, 40, builder),
                buildButton(stat, type, operation, displayItem, 50, builder),
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
                Utils.price(PokeTrainer.config.getPriceOf((operation.toChar()+""+num+"_statPoint")))));
    }

    private static void handleVEdit(ButtonAction e, vType type, Stat stat, Operation operation ,int value, PokeBuilder builder) {
        TransactionHandler.handleWithdraw(
                e,
                PokeTrainer.config.getPriceOf((operation.toChar()+""+value+"_statPoint")),
                () -> {
                    if(type==vType.EV) {
                        if((builder.getEvStats().get(stat)!=builder.getEvs().getAcceptableRange().getLast()&&operation==Operation.PLUS) ||
                                (builder.getEvStats().get(stat)!=0&&operation==Operation.MINUS)) {
                            LavenderMcServerSide.LOGGER.info(builder.getEvStats().get(stat)+" ");
                            builder.editV(type, stat, operation==Operation.PLUS? value:-value);
                            if(PokeTrainer.config.isFeedbackEnabled()) builder.getOwner().sendSystemMessage(Component.nullToEmpty("You have "+operation+"d your "+builder.getName()+"'s "+format(stat.getIdentifier().getPath())+" "+type+" by "+value+" for a price of "+PokeTrainer.config.getPriceOf(operation.toChar()+""+value+"_statPoint")+"."));
                            return true;
                        } else {
                            if(PokeTrainer.config.isFeedbackEnabled()) e.getPlayer().sendSystemMessage(Component.nullToEmpty("§cYour "+type+"'s "+format(stat.getIdentifier().getPath())+" is at "+builder.getEvStats().get(stat)+"! You can't "+operation+" it!"));
                            return false;
                        }
                    }
                    if(type==vType.IV) {
                        if((builder.getIvStats().get(stat)!=builder.getIvs().getAcceptableRange().getLast()&&operation==Operation.PLUS) ||
                                (builder.getIvStats().get(stat)!=0&&operation==Operation.MINUS)) {
                            LavenderMcServerSide.LOGGER.info(builder.getEvStats().get(stat)+" ");
                            builder.editV(type, stat, operation==Operation.PLUS? value:-value);
                            if(PokeTrainer.config.isFeedbackEnabled()) builder.getOwner().sendSystemMessage(Component.nullToEmpty("You have "+operation+"d your "+builder.getName()+"'s "+format(stat.getIdentifier().getPath())+" "+type+" by "+value+" for a price of "+PokeTrainer.config.getPriceOf(operation.toChar()+""+value+"_statPoint")+"."));
                            return true;
                        } else {
                            if(PokeTrainer.config.isFeedbackEnabled()) e.getPlayer().sendSystemMessage(Component.nullToEmpty("§cYour "+type+"'s "+format(stat.getIdentifier().getPath())+" is at "+builder.getIvStats().get(stat)+"! You can't "+operation+" it!"));
                            return false;
                        }
                    }
                    return false;
                }

                );
    }

}
