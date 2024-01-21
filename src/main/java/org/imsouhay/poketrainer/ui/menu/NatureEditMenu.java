package org.imsouhay.poketrainer.ui.menu;

import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import org.imsouhay.poketrainer.builder.PokeBuilder;
import org.imsouhay.poketrainer.ui.button.NatureEditButton;
import org.imsouhay.poketrainer.ui.template.DefaultChestTemplate;
import org.imsouhay.poketrainer.util.Destination;

public class NatureEditMenu {
    public static Page getPage(PokeBuilder builder) {
        ChestTemplate.Builder chestTemplate= DefaultChestTemplate.getDefaultTemplate(builder, Destination.MAINANDOPTIONS);

        chestTemplate.rectangleFromList(1, 1, 4, 7, NatureEditButton.getButtons(builder));


        return GooeyPage.builder()
                .title("Modify Nature")
                .template(chestTemplate.build())
                .build();
    }
}
