package org.imsouhay.poketrainer.ui.button;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.api.abilities.Ability;
import org.imsouhay.LavenderMcServerSide.util.Utils;
import org.imsouhay.poketrainer.PokeTrainer;
import org.imsouhay.poketrainer.builder.PokeBuilder;
import org.imsouhay.poketrainer.economy.TransactionHandler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.imsouhay.LavenderMcServerSide.util.Utils.format;

public class AbilityEditButton {

    private static List<Button> buttons;

    public static List<Button> getButtons(PokeBuilder builder) {
        makeButtons(builder);
        return buttons;
    }

    private static void makeButtons(PokeBuilder builder) {
        List<Ability> abilities=new LinkedList<>();

        builder.getPokemon().getForm().getAbilities().forEach(e ->
                abilities.add(new Ability(e.getTemplate(), false)));

        buttons=new ArrayList<>();

        abilities.forEach(ability -> buttons.add(GooeyButton.builder()
                .title("§b"+format(ability.getName()))
                .display(CobblemonItems.PRISM_SCALE.getDefaultInstance())
                .lore(new ArrayList<>(
                        builder.getPokemon().getAbility().getTemplate()==ability.getTemplate()?

                            List.of("§cThis is your Current Ability"):

                            List.of("§aClick to apply "+ability.getName()+" ability!",
                                    "",
                                    Utils.price(PokeTrainer.config.getPriceOf("ability")))))
                .onClick(e -> TransactionHandler.handleWithdraw(
                            e,
                            PokeTrainer.config.getPriceOf("ability"),
                            () -> {
                                if(builder.getPokemon().getAbility().getTemplate()!=ability.getTemplate()) {
                                    builder.setAbility(ability);
                                    if(PokeTrainer.config.isFeedbackEnabled()) Utils.sendFeedBack(e.getPlayer(), "ability", String.valueOf(PokeTrainer.config.getPriceOf("ability")), builder.getName(), format(ability.getName()));
                                    builder.reloadButton();
                                    return true;
                                }
                                if(PokeTrainer.config.isFeedbackEnabled()) Utils.sendFeedBack(e.getPlayer(), "pokemonAlreadyHasAbility", String.valueOf(PokeTrainer.config.getPriceOf("ability")), builder.getName(), format(ability.getName()));
                                return false;
                            })

                )
                .build()));
    }
}
