package org.imsouhay.poketrainer.ui;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.item.PokemonItem;
import net.minecraft.server.level.ServerPlayer;
import org.imsouhay.LavenderMcServerSide.util.Utils;
import org.imsouhay.poketrainer.PokeTrainer;
import org.imsouhay.poketrainer.builder.PokeBuilder;
import org.imsouhay.poketrainer.util.RoutingUtils;

import java.util.ArrayList;

import static org.imsouhay.LavenderMcServerSide.util.Utils.format;

public class MainMenu {

    public static Page getPage(ServerPlayer player){

        Button filler = GooeyButton.builder()
                .display(Utils.parseItemId(PokeTrainer.lang.getMainMenuFillerMaterial()))
                .title("")
                .lore(new ArrayList<>())
                .hideFlags(FlagType.All)
                .build();

        ChestTemplate.Builder chestTemplate=ChestTemplate
                .builder(6)
                .fill(filler);

        Button[] pokemonButtons=new Button[6];
        PokeBuilder[] builder=new PokeBuilder[6];

        for(int i=0; i<pokemonButtons.length; i++){
            builder[i]=new PokeBuilder(player, i);
            if(!builder[i].exists()) {
                pokemonButtons[i]=null;
                continue;
            }
            int finalI = i;

            pokemonButtons[i]=GooeyButton.builder()
                    .title("§b"+format(builder[i].getName()))
                    .display(PokemonItem.from(builder[i].getPokemon()))
                    .onClick(e -> RoutingUtils.toOptions(e, builder[finalI]))
                    .build();
        }

        chestTemplate.set(1, 2, pokemonButtons[0]);
        chestTemplate.set(1, 4, pokemonButtons[1]);
        chestTemplate.set(1, 6, pokemonButtons[2]);
        chestTemplate.set(4, 2, pokemonButtons[3]);
        chestTemplate.set(4, 4, pokemonButtons[4]);
        chestTemplate.set(4, 6, pokemonButtons[5]);



        return GooeyPage
                .builder()
                .template(chestTemplate.build())
                .title(PokeTrainer.lang.getMainMenuTitle())
                .build();
    }
}
