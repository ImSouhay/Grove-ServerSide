package org.imsouhay.poketrainer.ui.menu;

import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import org.imsouhay.poketrainer.builder.PokeBuilder;
import org.imsouhay.poketrainer.ui.button.PokeBallButton;
import org.imsouhay.poketrainer.ui.template.DefaultChestTemplate;
import org.imsouhay.poketrainer.util.Destination;

public class PokeballEditMenu {

    public static Page getPage(PokeBuilder builder) {
        ChestTemplate.Builder chestTemplate= DefaultChestTemplate.getDefaultTemplate(builder, Destination.MAINANDOPTIONS);

        chestTemplate.rectangleFromList(1, 1, 4, 7, PokeBallButton.getButtons(builder));


        return GooeyPage.builder()
                .title("Modify PokeBall")
                .template(chestTemplate.build())
                .build();
    }
}
