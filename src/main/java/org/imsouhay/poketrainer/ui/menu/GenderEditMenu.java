package org.imsouhay.poketrainer.ui.menu;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.pokemon.Gender;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.imsouhay.LavenderMcServerSide.util.Utils;
import org.imsouhay.poketrainer.PokeTrainer;
import org.imsouhay.poketrainer.builder.PokeBuilder;
import org.imsouhay.poketrainer.economy.TransactionHandler;
import org.imsouhay.poketrainer.ui.template.DefaultChestTemplate;
import org.imsouhay.poketrainer.util.Destination;

import java.util.ArrayList;
import java.util.List;

public class GenderEditMenu {

    public static Page getPage(PokeBuilder builder) {
        ChestTemplate.Builder chestTemplate= DefaultChestTemplate.getDefaultTemplate(builder, Destination.MAINANDOPTIONS);

        String lore = "§7Click to make this Pokemon";

        Button male= GooeyButton.builder()
                .display(new ItemStack(Items.LIGHT_BLUE_WOOL))
                .title("§bMale")
                .lore(new ArrayList<>(List.of(lore+" Male!", "", Utils.price(PokeTrainer.config.getPriceOf("toMale")))))
                .onClick(e ->
                        TransactionHandler.handleWithdraw(e,
                        PokeTrainer.config.getPriceOf("toMale"),
                        () -> {
                            if(builder.getGender()!=Gender.MALE) {
                                builder.setGender(Gender.MALE);
                                if(PokeTrainer.config.isFeedbackEnabled()) Utils.sendFeedBack(e.getPlayer(), "toMale", builder.getName(), String.valueOf(PokeTrainer.config.getPriceOf("toMale")));
                                builder.reloadButton();
                                return true;
                            } else {
                                if(PokeTrainer.config.isFeedbackEnabled()) Utils.sendFeedBack(e.getPlayer(), "pokemonAlreadyMale", builder.getName(), String.valueOf(PokeTrainer.config.getPriceOf("toMale")));
                                return false;
                            }
                }))
                .build();
        Button female= GooeyButton.builder()
                .display(new ItemStack(Items.PINK_WOOL))
                .title("§dFemale")
                .lore(new ArrayList<>(List.of(lore+" Female!", "", Utils.price(PokeTrainer.config.getPriceOf("toFemale")))))
                .onClick(e ->
                    TransactionHandler.handleWithdraw(e,
                            PokeTrainer.config.getPriceOf("toFemale"),
                            () -> {
                                if (builder.getGender() != Gender.FEMALE) {
                                    builder.setGender(Gender.FEMALE);
                                    if(PokeTrainer.config.isFeedbackEnabled()) Utils.sendFeedBack(e.getPlayer(), "toFemale", builder.getName(), String.valueOf(PokeTrainer.config.getPriceOf("toFemale")));
                                    builder.reloadButton();
                                    return true;
                                } else {
                                    if(PokeTrainer.config.isFeedbackEnabled()) Utils.sendFeedBack(e.getPlayer(), "pokemonAlreadyFemale", builder.getName(), String.valueOf(PokeTrainer.config.getPriceOf("toFemale")));
                                    return false;
                                }
                            })
                )
                .build();

        chestTemplate.rectangle(2, 1, 2, 3, female);
        chestTemplate.rectangle(2, 5, 2, 3, male);

        return GooeyPage.builder()
                .title("Modify Gender")
                .template(chestTemplate.build())
                .build();
    }
}
