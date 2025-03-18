package org.imsouhay.pokehunt.ui;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.LinkedPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.item.PokemonItem;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.imsouhay.pokehunt.PokeHunt;
import org.imsouhay.pokehunt.hunts.SingleHunt;
import org.imsouhay.pokehunt.util.Utils;

import static org.imsouhay.Grove.util.StyleUtils.*;
import static org.imsouhay.pokehunt.util.Utils.parseLongDate;

import java.util.*;

public class PokeHuntMenu {

    public static Page getPage(UUID uuid) {
        List<Button> hunts = new ArrayList<>();
        for (SingleHunt hunt :
                PokeHunt.config.isIndividualHunts() ?
                        PokeHunt.manager.getPlayerHunts(uuid).getHunts().values() :
                        PokeHunt.hunts.getHunts().values()) {

            if(hunt.isDone()) {
                ArrayList<String> onCooldownLore= new ArrayList<>(
                        PokeHunt.config.getDefaultLore().get("onCooldown")
                );

                onCooldownLore.add("§7Available in: §c"+parseLongDate(hunt.getEndTime() - new Date().getTime()));


                hunts.add(GooeyButton.builder()
                        .title("§cOn Cooldown")
                        .display(new ItemStack(Items.BARRIER))
                        .lore(onCooldownLore)
                        .build());
                continue;
            }

            ArrayList<String> defaultLore = new ArrayList<>(
                    PokeHunt.config.getDefaultLore().get(Utils.getStringRarityFromPokemon(hunt.getPokemon()))
            );

            Collection<Component> lore = new ArrayList<>(defaultLore.stream().map(Component::nullToEmpty).toList());

            boolean isShiny = hunt.getPokemon().getShiny();

            MutableComponent title = Component.literal(hunt.getPokemon().getSpecies().getName()).setStyle(isShiny ? yellow : aqua);

            if (PokeHunt.config.getMatchProperties().isShiny() && isShiny) {
                title.append(Component.literal("★").setStyle(red));
            }

            if (PokeHunt.config.getMatchProperties().isGender()) {
                switch (hunt.getPokemon().getGender()) {
                    case MALE: title.append(Component.literal(" ♂").setStyle(blue));break;
                    case FEMALE: title.append(Component.literal(" ♀").setStyle(red));break;
                    case GENDERLESS: title.append(Component.literal(" ○").setStyle(green));
                }
            }


            if (!hunt.getPokemon().getForm().getName().equalsIgnoreCase("normal")) {
                title.append(Component.literal(" - " + hunt.getPokemon().getForm().getName()).setStyle(aqua));
            }

            if (PokeHunt.config.getMatchProperties().isAbility()) {
                lore.add(Component.translatable("cobblemon.ui.info.ability").setStyle(dark_green)
                        .append(Component.literal(": "))
                        .append(Component.translatable(hunt.getPokemon().getAbility().getDisplayName()).setStyle(green)));
            }

            if (PokeHunt.config.getMatchProperties().isNature()) {
                lore.add(Component.translatable("cobblemon.ui.info.nature").setStyle(dark_purple)
                        .append(Component.literal(": "))
                        .append(Component.translatable(hunt.getPokemon().getNature().getDisplayName())
                                .setStyle(Style.EMPTY.withColor(TextColor.parseColor("light_purple")))));
            }

            lore.add(Component.literal(PokeHunt.language.getTimeRemaining() + parseLongDate(hunt.getEndTime() - new Date().getTime())));

            GooeyButton button = GooeyButton.builder()
                    .display(PokemonItem.from(hunt.getPokemon(), 1))
                    .title(title)
                    .lore(Component.class, lore)
                    .build();

            hunts.add(button);
        }

        Button filler = GooeyButton.builder()
                .display(Utils.parseItemId(PokeHunt.language.getFillerMaterial()))
                .hideFlags(FlagType.All)
                .lore(new ArrayList<>())
                .title("")
                .build();

        PlaceholderButton placeholder = new PlaceholderButton();

        int rows = (int) Math.ceil((double) PokeHunt.config.getHuntAmount() / 7) + 2;

        ChestTemplate template = ChestTemplate.builder(rows)
                .rectangle(1, 1, rows - 2, 7, placeholder)
                .fill(filler)
                .build();


        LinkedPage page = PaginationHelper.createPagesFromPlaceholders(template, hunts, null);
        page.setTitle(PokeHunt.language.getTitle());

        return page;
    }
}
