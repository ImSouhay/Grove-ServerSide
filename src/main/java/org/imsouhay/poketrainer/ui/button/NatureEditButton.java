package org.imsouhay.poketrainer.ui.button;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.api.pokemon.Natures;
import com.cobblemon.mod.common.pokemon.Nature;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.imsouhay.LavenderMcServerSide.LavenderMcServerSide;
import org.imsouhay.LavenderMcServerSide.util.Utils;
import org.imsouhay.poketrainer.PokeTrainer;
import org.imsouhay.poketrainer.builder.PokeBuilder;
import org.imsouhay.poketrainer.economy.TransactionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.imsouhay.LavenderMcServerSide.util.Utils.format;

public class NatureEditButton {
    private static List<Button> buttons;
    private static final Item[] vitItems= {
            CobblemonItems.PROTEIN,
            CobblemonItems.ZINC,
            CobblemonItems.CALCIUM,
            CobblemonItems.IRON,
            CobblemonItems.HP_UP,
            CobblemonItems.CARBOS
    };
    
    public static List<Button> getButtons(PokeBuilder builder) {
        makeButtons(builder);
        return buttons;
    }

    private static void makeButtons(PokeBuilder builder) {
        Button filler=GooeyButton.builder()
                .title("")
                .hideFlags(FlagType.All)
                .lore(new ArrayList<>())
                .display(Utils.parseItemId(PokeTrainer.lang.getFillerMaterial()))
                .build();

        
        buttons=new LinkedList<>(Arrays.asList(
                buildButton(Natures.INSTANCE.getADAMANT(), builder, "§b", vitItems[0]),
                buildButton(Natures.INSTANCE.getBOLD(), builder, "§c", vitItems[5]),
                buildButton(Natures.INSTANCE.getBASHFUL(), builder, "§d", vitItems[3]),
                filler,
                buildButton(Natures.INSTANCE.getBRAVE(), builder, "§a", vitItems[4]),
                buildButton(Natures.INSTANCE.getCALM(), builder, "§b", vitItems[2]),
                buildButton(Natures.INSTANCE.getCAREFUL(), builder, "§e", vitItems[1]),
                buildButton(Natures.INSTANCE.getDOCILE(), builder, "§a", vitItems[3]),
                buildButton(Natures.INSTANCE.getGENTLE(), builder, "§9", vitItems[0]),
                buildButton(Natures.INSTANCE.getHARDY(), builder, "§b", vitItems[5]),
                buildButton(Natures.INSTANCE.getHASTY(), builder, "§d", vitItems[1]),
                buildButton(Natures.INSTANCE.getIMPISH(), builder, "§e", vitItems[4]),
                buildButton(Natures.INSTANCE.getJOLLY(), builder, "§a", vitItems[0]),
                buildButton(Natures.INSTANCE.getLAX(), builder, "§c", vitItems[3]),
                buildButton(Natures.INSTANCE.getLONELY(), builder, "§9", vitItems[2]),
                buildButton(Natures.INSTANCE.getMILD(), builder, "§b", vitItems[1]),
                buildButton(Natures.INSTANCE.getMODEST(), builder, "§a", vitItems[5]),
                buildButton(Natures.INSTANCE.getNAIVE(), builder, "§d", vitItems[4]),
                buildButton(Natures.INSTANCE.getNAUGHTY(), builder, "§b", vitItems[2]),
                buildButton(Natures.INSTANCE.getQUIET(), builder, "§e", vitItems[3]),
                buildButton(Natures.INSTANCE.getQUIRKY(), builder, "§c", vitItems[1]),
                filler,
                buildButton(Natures.INSTANCE.getRASH(), builder, "§b", vitItems[5]),
                buildButton(Natures.INSTANCE.getRELAXED(), builder, "§a", vitItems[2]),
                buildButton(Natures.INSTANCE.getSASSY(), builder, "§9", vitItems[4]),
                buildButton(Natures.INSTANCE.getSERIOUS(), builder, "§a", vitItems[3]),
                buildButton(Natures.INSTANCE.getTIMID(), builder, "§b", vitItems[1]),
                filler
        ));
    }

    private static Button buildButton(Nature nature, PokeBuilder builder, String colorCode, Item displayItem) {
        return GooeyButton.builder()
                .title(colorCode+""+format(nature.getName().getPath()))
                .display(new ItemStack(displayItem))
                .lore(buildLore(nature.getName().getPath()))
                .onClick(e ->
                        TransactionHandler.handleWithdraw(
                                e,
                                PokeTrainer.config.getPriceOf("nature"),
                                () -> {
                                    if(builder.getNature()!=nature) {
                                        builder.setNature(nature);
                                        if(PokeTrainer.config.isFeedbackEnabled()) Utils.sendFeedBack(e.getPlayer(), "nature", String.valueOf(PokeTrainer.config.getPriceOf("nature")), builder.getName(),
                                                format(nature.getName().getPath()));
                                        return true;
                                    } else {
                                        if(PokeTrainer.config.isFeedbackEnabled()) Utils.sendFeedBack(e.getPlayer(), "pokemonAlreadyHasNature", String.valueOf(PokeTrainer.config.getPriceOf("nature")), builder.getName(),
                                                format(nature.getName().getPath()));
                                        return false;
                                    }
                                })
                )
                .build();
    }

    private static ArrayList<String> buildLore(String natureName) {
        String defaultLore="§7Click to change nature to @nature!";
        return new ArrayList<>(List.of(
                defaultLore.replace("@nature", format(natureName)),
                "",
                Utils.price(PokeTrainer.config.getPriceOf("nature"))));
    }
}
