package org.imsouhay.poketrainer.ui.menu;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.imsouhay.Grove.util.Utils;
import org.imsouhay.poketrainer.PokeTrainer;
import org.imsouhay.poketrainer.builder.PokeBuilder;
import org.imsouhay.poketrainer.economy.TransactionManager;
import org.imsouhay.poketrainer.ui.template.DefaultChestTemplate;
import org.imsouhay.poketrainer.util.Destination;

import java.util.ArrayList;
import java.util.List;

public class ShinyEditMenu {

    public static Page getPage(PokeBuilder builder) {
        ChestTemplate.Builder chestTemplate= DefaultChestTemplate.getDefaultTemplate(builder, Destination.MAINANDOPTIONS);

        String lore = "§7Click to make this Pokemon";

        Button toNormal= GooeyButton.builder()
                .display(new ItemStack(Items.GRAY_DYE))
                .title("§7§lNon-Shiny")
                .lore(new ArrayList<>(List.of(lore+" Non-Shiny!", "", Utils.price(PokeTrainer.config.getPriceOf("unShiny")))))
                .onClick(e -> TransactionManager.handleWithdraw(
                        e,
                        PokeTrainer.config.getPriceOf("unShiny"),
                        () -> {
                            if(builder.getPokemon().getShiny()) {
                                builder.getPokemon().setShiny(false);
                                if(PokeTrainer.config.isFeedbackEnabled()) Utils.sendFeedBack(e.getPlayer(), "toNotShiny", builder.getName(), String.valueOf(PokeTrainer.config.getPriceOf("unShiny")));
                                builder.reloadButton();
                                return true;
                            } else {
                                if(PokeTrainer.config.isFeedbackEnabled()) Utils.sendFeedBack(e.getPlayer(), "pokemonAlreadyNotShiny", builder.getName(), String.valueOf(PokeTrainer.config.getPriceOf("unShiny")));
                                return false;
                            }
                        }))
                .build();

        Button toShiny= GooeyButton.builder()
                .display(new ItemStack(Items.GREEN_DYE))
                .title("§e§lShiny")
                .lore(new ArrayList<>(List.of(lore+" §eShiny§7!", "", Utils.price(PokeTrainer.config.getPriceOf("shiny")))))
                .onClick(e ->
                    TransactionManager.handleWithdraw(
                            e,
                            PokeTrainer.config.getPriceOf("shiny"),
                            () -> {
                                if (!builder.getPokemon().getShiny()) {
                                    builder.getPokemon().setShiny(true);
                                    if(PokeTrainer.config.isFeedbackEnabled()) Utils.sendFeedBack(e.getPlayer(), "toShiny", builder.getName(), String.valueOf(PokeTrainer.config.getPriceOf("shiny")));
                                    builder.reloadButton();
                                    return true;
                                } else {
                                    if(PokeTrainer.config.isFeedbackEnabled()) Utils.sendFeedBack(e.getPlayer(), "pokemonAlreadyShiny", builder.getName(), String.valueOf(PokeTrainer.config.getPriceOf("shiny")));
                                    return false;
                                }
                            })
                )
                .build();


        chestTemplate.rectangle(2, 1, 2, 3, toShiny);
        chestTemplate.rectangle(2, 5, 2, 3, toNormal);


        return GooeyPage.builder()
                .title("Modify Shiny")
                .template(chestTemplate.build())
                .build();
    }
}
