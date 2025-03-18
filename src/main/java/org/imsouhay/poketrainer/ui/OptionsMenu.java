package org.imsouhay.poketrainer.ui;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.ButtonClick;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.api.pokemon.feature.ChoiceSpeciesFeatureProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.imsouhay.Grove.util.Utils;
import org.imsouhay.poketrainer.PokeTrainer;
import org.imsouhay.poketrainer.builder.PokeBuilder;
import org.imsouhay.poketrainer.ui.menu.*;
import org.imsouhay.poketrainer.ui.template.DefaultChestTemplate;
import org.imsouhay.poketrainer.util.Destination;
import org.imsouhay.poketrainer.util.Operation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.imsouhay.Grove.util.Utils.format;

public class OptionsMenu {
    public static Page getPage(PokeBuilder builder){
        
        Button abilityButton= GooeyButton.builder()
                .display(CobblemonItems.PRISM_SCALE.getDefaultInstance())
                .title("§eAbility §fEdit")
                .onClick(e -> {
                    UIManager.openUIForcefully(e.getPlayer(), AbilityEditMenu.getPage(builder));
                })
                .lore(new ArrayList<>(List.of(getLore("ability"))))
                .build();
        
        Button genderButton= GooeyButton.builder()
                .display(CobblemonItems.CHOICE_SCARF.getDefaultInstance())
                .title("§dGender §bEdit")
                .onClick(e -> {
                    UIManager.openUIForcefully(e.getPlayer(), GenderEditMenu.getPage(builder));
                })
                .lore(new ArrayList<>(List.of(getLore("gender"))))
                .build();
        
        Button pokeballButton= GooeyButton.builder()
                .display(CobblemonItems.POKE_BALL.getDefaultInstance())
                .title("§cPokeball §fEdit")
                .onClick(e -> {
                    UIManager.openUIForcefully(e.getPlayer(), PokeballEditMenu.getPage(builder));
                })
                .lore(new ArrayList<>(List.of(getLore("pokeball"))))
                .build();
        
        Button IvButton= GooeyButton.builder()
                .display(CobblemonItems.ELECTIRIZER.getDefaultInstance())
                .title("§6IV §fEdit")
                .onClick(e -> {
                    UIManager.openUIForcefully(e.getPlayer(), IvEditMenu.getPage(builder));
                })
                .lore(new ArrayList<>(List.of(getLore("IV"))))
                .build();
        
        Button EvButton= GooeyButton.builder()
                .display(CobblemonItems.MAGMARIZER.getDefaultInstance())
                .title("§aEV §fEdit")
                .onClick(e -> {
                    UIManager.openUIForcefully(e.getPlayer(), EvEditMenu.getPage(builder));
                })
                .lore(new ArrayList<>(List.of(getLore("EV"))))
                .build();
        
        Button LevelButton= GooeyButton.builder()
                .display(CobblemonItems.RARE_CANDY.getDefaultInstance())
                .title("§6Level §fEdit")
                .onClick(e -> {
                    if(e.getClickType()== ButtonClick.LEFT_CLICK) {
                        UIManager.openUIForcefully(e.getPlayer(), LevelEditMenu.getPage(builder, Operation.PLUS));
                    } else if(e.getClickType()==ButtonClick.RIGHT_CLICK) {
                        UIManager.openUIForcefully(e.getPlayer(), LevelEditMenu.getPage(builder, Operation.MINUS));
                    }
                })
                .lore(new ArrayList<>(Arrays.asList("§aLeft-Click to open increase level editor!",
                                                    "§cRight-Click to open decrease level editor!")))
                .build();
        
        Button ShinyButton= GooeyButton.builder()
                .display(CobblemonItems.STAR_SWEET.getDefaultInstance())
                .title("§eShiny §fEdit")
                .onClick(e -> {
                    UIManager.openUIForcefully(e.getPlayer(), ShinyEditMenu.getPage(builder));
                })
                .lore(new ArrayList<>(List.of(getLore("shiny"))))
                .build();

        Button skinButton;

        List<ChoiceSpeciesFeatureProvider> features=Utils.getFeatureFromPokemon(builder.getPokemon());
        if(!features.isEmpty()) {
            skinButton=GooeyButton.builder()
                    .title("§bSkin §fEdit")
                    .display(new ItemStack(Items.PLAYER_HEAD))
                    .lore(new ArrayList<>(List.of(getLore("skin"))))
                    .onClick(e -> UIManager.openUIForcefully(e.getPlayer(), SkinChoseEditMenu.getPage(builder, features)))
                    .build();
        } else {
            skinButton=GooeyButton.builder()
                    .title("§cUnavailable")
                    .display(new ItemStack(Items.BARRIER))
                    .lore(new ArrayList<>(List.of("§7Skin Edit is not available for this pokemon!")))
                    .build();
        }
        
        Button blacklisted = GooeyButton.builder()
                .title("§cSection Unavailable")
                .display(new ItemStack(Items.BARRIER))
                .lore(new ArrayList<>(List.of("§7This Section is closed for this pokemon.")))
                .build();


        ChestTemplate.Builder chestTemplate= DefaultChestTemplate.getDefaultTemplate(builder, Destination.MAIN, PokeTrainer.lang.getHomeMenuFillerMaterial());

        chestTemplate.set(PokeTrainer.config.getSectionPos("ability"), isBlackListed("ability", builder)? blacklisted:abilityButton);
        chestTemplate.set(PokeTrainer.config.getSectionPos("gender"), isBlackListed("gender", builder)? blacklisted:genderButton);
        chestTemplate.set(PokeTrainer.config.getSectionPos("iv"), isBlackListed("iv", builder)? blacklisted:IvButton);
        chestTemplate.set(PokeTrainer.config.getSectionPos("ev"), isBlackListed("ev", builder)? blacklisted:EvButton);
        chestTemplate.set(PokeTrainer.config.getSectionPos("pokeball"), isBlackListed("pokeball", builder)? blacklisted:pokeballButton);
        chestTemplate.set(PokeTrainer.config.getSectionPos("level"), isBlackListed("level", builder)? blacklisted:LevelButton);
        chestTemplate.set(PokeTrainer.config.getSectionPos("shiny"), isBlackListed("shiny", builder)? blacklisted:ShinyButton);
        chestTemplate.set(PokeTrainer.config.getSectionPos("skin"), isBlackListed("skin", builder)? blacklisted:skinButton);

       return GooeyPage.builder()
               .title(PokeTrainer.lang.getHomeMenuTitle())
               .template(chestTemplate.build())
               .build();
    }

    private static boolean isBlackListed(String section, PokeBuilder builder) {
        return PokeTrainer.config.isPokemonBlackListed(builder.getPokemon(), section);
    }
    
    private static String getLore(String destination){
        return "§7Click to open "+format(destination)+" editor!";
    }
}
