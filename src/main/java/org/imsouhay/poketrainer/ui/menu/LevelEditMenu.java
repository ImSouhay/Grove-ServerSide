package org.imsouhay.poketrainer.ui.menu;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import org.imsouhay.poketrainer.builder.PokeBuilder;
import org.imsouhay.poketrainer.ui.button.LevelEditButton;
import org.imsouhay.poketrainer.ui.template.DefaultChestTemplate;
import org.imsouhay.poketrainer.util.Destination;
import org.imsouhay.poketrainer.util.Operation;

import java.util.List;

public class LevelEditMenu {

    public static Page getPage(PokeBuilder builder, Operation operation) {
        ChestTemplate.Builder chestTemplate= DefaultChestTemplate.getDefaultTemplate(builder, Destination.MAINANDOPTIONS);

        List<Button> buttons= LevelEditButton.getButtons(operation, builder);

        int row=2;
        int column=2;

        for(Button button: buttons) {
            if(column==7 && row==2){row++;column=3;}
            chestTemplate.set(row, column, button);
            column++;
        }


        return GooeyPage.builder()
                .title("Modify Levels")
                .template(chestTemplate.build())
                .build();
    }
}
