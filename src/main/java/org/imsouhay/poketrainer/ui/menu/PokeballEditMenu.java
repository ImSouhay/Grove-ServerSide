package org.imsouhay.poketrainer.ui.menu;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.button.linked.LinkType;
import ca.landonjw.gooeylibs2.api.button.linked.LinkedPageButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.LinkedPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.imsouhay.poketrainer.PokeTrainer;
import org.imsouhay.poketrainer.builder.PokeBuilder;
import org.imsouhay.poketrainer.builder.PokeBuilderButton;
import org.imsouhay.poketrainer.ui.button.EscapeButton;
import org.imsouhay.poketrainer.ui.button.PokeBallButton;
import org.imsouhay.poketrainer.util.Destination;

import java.util.ArrayList;

public class PokeballEditMenu {

    public static Page getPage(PokeBuilder builder) {
        Button filler=GooeyButton.builder()
                .title("")
                .hideFlags(FlagType.All)
                .lore(new ArrayList<>())
                .display(org.imsouhay.Grove.util.Utils.parseItemId(PokeTrainer.lang.getFillerMaterial()))
                .build();

        LinkedPageButton nextPage = LinkedPageButton.builder()
                .display(new ItemStack(Items.ARROW))
                .title("§7Next Page")
                .linkType(LinkType.Next)
                .build();

        LinkedPageButton previousPage = LinkedPageButton.builder()
                .display(new ItemStack(Items.ARROW))
                .title("§7Previous Page")
                .linkType(LinkType.Previous)
                .build();

        builder.setGuiButton(PokeBuilderButton.build(builder));

        PlaceholderButton placeholder = new PlaceholderButton();

        ChestTemplate template = ChestTemplate.builder(6)
                .rectangle(1, 1, 4, 7, placeholder)
                .set(49, EscapeButton.getEscapeButton(Destination.MAINANDOPTIONS, builder))
                .set(4, builder.getGuiButton())
                .set(45, previousPage)
                .set(53, nextPage)
                .fill(filler)
                .build();


        LinkedPage page = PaginationHelper.createPagesFromPlaceholders(template, PokeBallButton.getButtons(builder), null);
        page.setTitle("Modify PokeBall");
        setPageTitle(page, "Modify PokeBall");

        return page;
    }

    private static void setPageTitle(LinkedPage page, String string) {
        LinkedPage next = page.getNext();
        if (next != null) {
            next.setTitle(string);
            setPageTitle(next, string);
        }
    }
}
