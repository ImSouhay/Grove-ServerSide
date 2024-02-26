package org.imsouhay.poketrainer.ui.template;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import org.imsouhay.LavenderMcServerSide.util.Utils;
import org.imsouhay.poketrainer.PokeTrainer;
import org.imsouhay.poketrainer.builder.PokeBuilder;
import org.imsouhay.poketrainer.builder.PokeBuilderButton;
import org.imsouhay.poketrainer.ui.button.EscapeButton;
import org.imsouhay.poketrainer.util.Destination;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DefaultChestTemplate {
    public static ChestTemplate.Builder getDefaultTemplate(@NotNull PokeBuilder builder, Destination destination) {
        return getDefaultTemplate(builder, destination, PokeTrainer.lang.getFillerMaterial());
    }
    public static ChestTemplate.Builder getDefaultTemplate(@NotNull PokeBuilder builder, Destination destination, String fillerMaterial) {

        Button filler=GooeyButton.builder()
                .title("")
                .hideFlags(FlagType.All)
                .lore(new ArrayList<>())
                .display(Utils.parseItemId(fillerMaterial))
                .build();

        builder.setGuiButton(PokeBuilderButton.build(builder));


        return ChestTemplate.builder(6)
                .set(49, EscapeButton.getEscapeButton(destination, builder))
                .set(4, builder.getGuiButton())
                .fill(filler);
    }
}
