package org.imsouhay.poketrainer.ui.menu;

import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import net.minecraft.world.item.Item;
import org.imsouhay.poketrainer.builder.PokeBuilder;
import org.imsouhay.poketrainer.ui.button.StatEditButton;
import org.imsouhay.poketrainer.ui.template.DefaultChestTemplate;
import org.imsouhay.poketrainer.util.Destination;
import org.imsouhay.poketrainer.util.Operation;
import org.imsouhay.poketrainer.util.vType;

import static org.imsouhay.LavenderMcServerSide.util.Utils.format;

public class StatEditMenu {

    public static Page getPage(PokeBuilder builder, Destination destination,
                               vType type, Stat stat,
                               Operation operation, Item displayItem) {

        ChestTemplate.Builder chestTemplate= DefaultChestTemplate.getDefaultTemplate(builder, destination);

        chestTemplate.rectangleFromList(2, 2, 2, 5,
                StatEditButton.getStatButtons(type, stat, operation, displayItem, builder));

        return GooeyPage.builder()
                .title("Modify "+format(stat.getIdentifier().getPath())+" "+type+"s")
                .template(chestTemplate.build())
                .build();
    }

}
