package org.imsouhay.poketrainer.ui.menu;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import net.minecraft.network.chat.Component;
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

public class ShinyEditMenu {

    public static Page getPage(PokeBuilder builder) {
        ChestTemplate.Builder chestTemplate= DefaultChestTemplate.getDefaultTemplate(builder, Destination.MAINANDOPTIONS);

        String lore = "§7Click to make this Pokemon";

        Button toNormal= GooeyButton.builder()
                .display(new ItemStack(Items.GRAY_DYE))
                .title("§7§lNon-Shiny")
                .lore(new ArrayList<>(List.of(lore+" Non-Shiny!", "", Utils.price(PokeTrainer.config.getPriceOf("unShiny")))))
                .onClick(e -> TransactionHandler.handleWithdraw(
                        e,
                        PokeTrainer.config.getPriceOf("unShiny"),
                        () -> {
                            if(builder.getPokemon().getShiny()) {
                                builder.getPokemon().setShiny(false);
                                if(PokeTrainer.config.isFeedbackEnabled()) e.getPlayer().sendSystemMessage(Component.nullToEmpty("You have made your " + builder.getName() + " Non-Shiny for a price of " + PokeTrainer.config.getPriceOf("unShiny")));
                                return true;
                            } else {
                                if(PokeTrainer.config.isFeedbackEnabled()) e.getPlayer().sendSystemMessage(Component.nullToEmpty("§cYour "+builder.getName()+" is already not shiny!"));
                                return false;
                            }
                        }))
                .build();

        Button toShiny= GooeyButton.builder()
                .display(new ItemStack(Items.GREEN_DYE))
                .title("§e§lShiny")
                .lore(new ArrayList<>(List.of(lore+" §eShiny§7!", "", Utils.price(PokeTrainer.config.getPriceOf("shiny")))))
                .onClick(e ->
                    TransactionHandler.handleWithdraw(
                            e,
                            PokeTrainer.config.getPriceOf("shiny"),
                            () -> {
                                if (!builder.getPokemon().getShiny()) {
                                    builder.getPokemon().setShiny(true);
                                    if(PokeTrainer.config.isFeedbackEnabled()) e.getPlayer().sendSystemMessage(Component.nullToEmpty("You have made your " + builder.getName() + " Shiny for a price of " + PokeTrainer.config.getPriceOf("shiny")));
                                    return true;
                                } else {
                                    if(PokeTrainer.config.isFeedbackEnabled()) e.getPlayer().sendSystemMessage(Component.nullToEmpty("§cYour " + builder.getName() + " is already shiny!"));
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
