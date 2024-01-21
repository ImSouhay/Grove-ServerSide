package org.imsouhay.poketrainer.ui.menu;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.api.pokemon.feature.ChoiceSpeciesFeatureProvider;
import org.imsouhay.poketrainer.builder.PokeBuilder;
import org.imsouhay.poketrainer.ui.button.SkinEditButton;
import org.imsouhay.poketrainer.ui.template.DefaultChestTemplate;
import org.imsouhay.poketrainer.util.Destination;

import java.util.Iterator;
import java.util.List;

import static org.imsouhay.LavenderMcServerSide.util.Utils.format;


public class SkinEditMenu {
    public static Page getPage(PokeBuilder builder, ChoiceSpeciesFeatureProvider feature) {
        ChestTemplate.Builder chestTemplate= DefaultChestTemplate.getDefaultTemplate(builder, Destination.MAINANDSKIN);

        List<Button> buttons=SkinEditButton.getButtons(builder, feature);
        
        Iterator<Button> buttonsIter=buttons.iterator();

        if(buttons.size()==1) {
            chestTemplate.set(22, buttonsIter.next());
        } else {

            if(buttonsIter.hasNext()) {
                chestTemplate.set(21, buttonsIter.next());
            }
            if(buttonsIter.hasNext()) {
                chestTemplate.set(23, buttonsIter.next());
            }
            if(buttonsIter.hasNext()) {
                chestTemplate.set(22, buttonsIter.next());
            }
            if(buttonsIter.hasNext()) {
                chestTemplate.set(31, buttonsIter.next());
            }
            if(buttonsIter.hasNext()) {
                chestTemplate.set(30, buttonsIter.next());
            }
            if(buttonsIter.hasNext()) {
                chestTemplate.set(32, buttonsIter.next());
            }
            if(buttonsIter.hasNext()) {
                chestTemplate.set(13, buttonsIter.next());
            }
            if(buttonsIter.hasNext()) {
                chestTemplate.set(12, buttonsIter.next());
            }
            if(buttonsIter.hasNext()) {
                chestTemplate.set(14, buttonsIter.next());
            }
            if(buttonsIter.hasNext()) {
                chestTemplate.set(40, buttonsIter.next());
            }
            if(buttonsIter.hasNext()) {
                chestTemplate.set(39, buttonsIter.next());
            }
            if(buttonsIter.hasNext()) {
                chestTemplate.set(41, buttonsIter.next());
            }
            if(buttonsIter.hasNext()) {
                chestTemplate.set(11, buttonsIter.next());
            }
            if(buttonsIter.hasNext()) {
                chestTemplate.set(15, buttonsIter.next());
            }
            if(buttonsIter.hasNext()) {
                chestTemplate.set(20, buttonsIter.next());
            }
            if(buttonsIter.hasNext()) {
                chestTemplate.set(24, buttonsIter.next());
            }
            if(buttonsIter.hasNext()) {
                chestTemplate.set(29, buttonsIter.next());
            }
            if(buttonsIter.hasNext()) {
                chestTemplate.set(33, buttonsIter.next());
            }
            if(buttonsIter.hasNext()) {
                chestTemplate.set(38, buttonsIter.next());
            }
            if(buttonsIter.hasNext()) {
                chestTemplate.set(42, buttonsIter.next());
            }
            if(buttonsIter.hasNext()) {
                chestTemplate.set(10, buttonsIter.next());
            }
            if(buttonsIter.hasNext()) {
                chestTemplate.set(16, buttonsIter.next());
            }
            if(buttonsIter.hasNext()) {
                chestTemplate.set(19, buttonsIter.next());
            }
            if(buttonsIter.hasNext()) {
                chestTemplate.set(25, buttonsIter.next());
            }
            if(buttonsIter.hasNext()) {
                chestTemplate.set(28, buttonsIter.next());
            }
            if(buttonsIter.hasNext()) {
                chestTemplate.set(34, buttonsIter.next());
            }
            if(buttonsIter.hasNext()) {
                chestTemplate.set(37, buttonsIter.next());
            }
            if(buttonsIter.hasNext()) {
                chestTemplate.set(43, buttonsIter.next());
            }
        }

        return GooeyPage.builder()
                .title(format(feature.getAspectFormat()))
                .template(chestTemplate.build())
                .build();
    }
}
