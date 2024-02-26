package org.imsouhay.poketrainer.ui.button;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import com.cobblemon.mod.common.api.pokemon.feature.ChoiceSpeciesFeatureProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.imsouhay.LavenderMcServerSide.util.Utils;
import org.imsouhay.poketrainer.PokeTrainer;
import org.imsouhay.poketrainer.builder.PokeBuilder;
import org.imsouhay.poketrainer.economy.TransactionHandler;

import java.util.*;

import static org.imsouhay.LavenderMcServerSide.util.Utils.format;

public class SkinEditButton {

    private static List<Button> buttons;

    public static List<Button> getButtons(PokeBuilder builder, ChoiceSpeciesFeatureProvider pokemonFeature) {
        makeButtons(builder, pokemonFeature);
        return buttons;
    }

    private static void makeButtons(PokeBuilder builder, ChoiceSpeciesFeatureProvider pokemonFeature) {



        buttons=new LinkedList<>();

        Set<String> aspects=new HashSet<>();


        pokemonFeature.getChoices().forEach(choice -> {
            boolean[] isChosenChoice={false};
            buttons.add(GooeyButton.builder()
                            .title(format(choice))
                            .display(new ItemStack(Items.NETHER_STAR))
                            .onClick(e ->{
                                builder.getPokemon().getAspects().forEach(originalAspect -> {
                                    if(originalAspect.contains(choice)) {
                                        isChosenChoice[0]=true;
                                    }
                                });

                                    if(!isChosenChoice[0]){
                                        TransactionHandler.handleWithdraw(
                                            e,
                                            PokeTrainer.config.getPriceOf("skin"),
                                            () -> {
                                                builder.getPokemon().getAspects().forEach(a -> {
                                                    if(a.equals("male") || a.equals("female") || a.equals("shiny") || a.contains(choice)){
                                                        aspects.add(a);
                                                    }
                                                });
                                                aspects.add(pokemonFeature.getAspectFormat().replace("{{choice}}", choice));

                                                builder.getPokemon().setAspects(aspects);
                                                if(PokeTrainer.config.isFeedbackEnabled()) Utils.sendFeedBack(
                                                        e.getPlayer(),
                                                        "skinEdit",
                                                        String.valueOf(PokeTrainer.config.getPriceOf("skin")),
                                                        builder.getName(),
                                                        format(choice));
                                                builder.reloadButton();
                                                return true;
                                            }
                                            );
                                    } else {
                                        if(PokeTrainer.config.isFeedbackEnabled()) Utils.sendFeedBack(
                                                e.getPlayer(),
                                                "pokemonAlreadyHasSkin",
                                                String.valueOf(PokeTrainer.config.getPriceOf("skin")),
                                                builder.getName(),
                                                format(choice));
                                    }
                            })
                            .lore(new ArrayList<>(List.of(isChosenChoice[0]?
                                    "§cThis is your current chosen texture.":
                                    "§7Click to apply "+format(choice),
                                    "",
                                    Utils.price(PokeTrainer.config.getPriceOf("skin")))))
                            .build());});


    }

}
