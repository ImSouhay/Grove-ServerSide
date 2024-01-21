package org.imsouhay.poketrainer.ui.button;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.ButtonAction;
import ca.landonjw.gooeylibs2.api.button.ButtonClick;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import net.minecraft.world.item.ItemStack;
import org.imsouhay.poketrainer.builder.PokeBuilder;
import org.imsouhay.poketrainer.util.Destination;
import org.imsouhay.poketrainer.util.Operation;
import org.imsouhay.poketrainer.util.RoutingUtils;
import org.imsouhay.poketrainer.util.vType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.imsouhay.LavenderMcServerSide.util.Utils.format;

public class EvEditButton {
    private static List<Button> buttons;

    public static List<Button> getButtons(PokeBuilder builder) {
        makeButtons(builder);
        return buttons;
    }


    private static void makeButtons(PokeBuilder builder) {
        buttons=new LinkedList<>(Arrays.asList(
                buildButton(Stats.HP, new ItemStack(CobblemonItems.NAUGHTY_MINT), builder),
                buildButton(Stats.ATTACK, new ItemStack(CobblemonItems.ADAMANT_MINT), builder),
                buildButton(Stats.DEFENCE, new ItemStack(CobblemonItems.JOLLY_MINT), builder),
                buildButton(Stats.SPECIAL_ATTACK, new ItemStack(CobblemonItems.MODEST_MINT), builder),
                buildButton(Stats.SPECIAL_DEFENCE, new ItemStack(CobblemonItems.QUIET_MINT), builder),
                buildButton(Stats.SPEED, new ItemStack(CobblemonItems.SERIOUS_MINT), builder)
        ));
    }

    private static Button buildButton(Stat stat, ItemStack displayItem, PokeBuilder builder) {




        return GooeyButton.builder()
                .title("§a"+format(stat.getIdentifier().getPath())+" EV")
                .lore(buildLore(stat.getIdentifier().getPath()))
                .onClick(e -> {
                    if(e.getClickType()== ButtonClick.LEFT_CLICK) {handleLeftClick(e, stat, builder);}
                    if(e.getClickType()== ButtonClick.RIGHT_CLICK){handleRightClick(e, stat, builder);}
                })
                .display(displayItem)
                .build();
    }

    private static ArrayList<String> buildLore(String statName) {
        String defaultLore="Click to open the @incORdec @statName EVs menu!".replace("@statName", format(statName));

        return new ArrayList<>(Arrays.asList("§aLeft-"+defaultLore.replace("@incORdec", "increase"),
                "§cRight-"+defaultLore.replace("@incORdec", "decrease")));


    }

    private static void handleLeftClick(ButtonAction e, Stat stat, PokeBuilder builder) {
        RoutingUtils.toStatEditor(e, builder, Destination.MAINANDEV, vType.EV, stat, Operation.PLUS, CobblemonItems.TIMID_MINT);
    }

    private static void handleRightClick(ButtonAction e, Stat stat, PokeBuilder builder) {
        RoutingUtils.toStatEditor(e, builder,Destination.MAINANDEV, vType.EV, stat, Operation.MINUS, CobblemonItems.BRAVE_MINT);
    }
}
