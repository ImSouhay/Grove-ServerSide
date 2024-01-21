package org.imsouhay.poketrainer.ui.menu;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import org.imsouhay.poketrainer.builder.PokeBuilder;
import org.imsouhay.poketrainer.ui.button.AbilityEditButton;
import org.imsouhay.poketrainer.ui.template.DefaultChestTemplate;
import org.imsouhay.poketrainer.util.Destination;

import java.util.Iterator;
import java.util.List;

public class AbilityEditMenu {

    public static Page getPage(PokeBuilder builder) {

        ChestTemplate.Builder chestTemplate= DefaultChestTemplate.getDefaultTemplate(builder, Destination.MAINANDOPTIONS);


        List<Button> buttons= AbilityEditButton.getButtons(builder);

        Iterator<Button> buttonIterator= buttons.iterator();

        if(buttons.size()==3) {
            chestTemplate.set(20, buttonIterator.next());
            chestTemplate.set(22, buttonIterator.next());
            chestTemplate.set(24, buttonIterator.next());
        } else if(buttons.size()==2) {
            chestTemplate.set(20, buttonIterator.next());
            chestTemplate.set(24, buttonIterator.next());
        } else if(buttons.size()==4) {
            chestTemplate.set(19, buttonIterator.next());
            chestTemplate.set(21, buttonIterator.next());
            chestTemplate.set(23, buttonIterator.next());
            chestTemplate.set(25, buttonIterator.next());
        } else {
            chestTemplate.set(20, buttonIterator.next());
            chestTemplate.set(22, buttonIterator.next());
            chestTemplate.set(24, buttonIterator.next());
            chestTemplate.set(30, buttonIterator.next());
            chestTemplate.set(32, buttonIterator.next());
        }

        return GooeyPage.builder()
                .title("Modify Ability")
                .template(chestTemplate.build())
                .build();
    }
}
