package org.imsouhay.poketrainer.ui.menu;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.api.pokemon.feature.ChoiceSpeciesFeatureProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.imsouhay.poketrainer.builder.PokeBuilder;
import org.imsouhay.poketrainer.ui.template.DefaultChestTemplate;
import org.imsouhay.poketrainer.util.Destination;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.imsouhay.Grove.util.Utils.format;

public class SkinChoseEditMenu {

    public static Page getPage(PokeBuilder builder, List<ChoiceSpeciesFeatureProvider> features) {
        ChestTemplate.Builder chestTemplate= DefaultChestTemplate.getDefaultTemplate(builder, Destination.MAINANDOPTIONS);

        List<GooeyButton> buttons=new ArrayList<>();

        ItemStack[] skulls={
                new ItemStack(Items.ZOMBIE_HEAD),
                new ItemStack(Items.WITHER_SKELETON_SKULL),
                new ItemStack(Items.DRAGON_HEAD),
                new ItemStack(Items.CREEPER_HEAD),
                new ItemStack(Items.PIGLIN_HEAD),
                new ItemStack(Items.SKELETON_SKULL)
        };
        int counter=0;

        for(ChoiceSpeciesFeatureProvider feature:features) {
            buttons.add(GooeyButton.builder()
                            .title(format(feature.getAspectFormat()))
                            .lore(List.of("§7Click to edit: " + format(feature.getAspectFormat())))
                            .onClick(e -> UIManager.openUIForcefully(e.getPlayer(), SkinEditMenu.getPage(builder, feature)))
                            .display(skulls[counter])
                            .build());
            counter++;
        }

        Iterator<GooeyButton> buttonIterator= buttons.iterator();

        if(buttons.size()==1) {
            chestTemplate.set(22, buttonIterator.next());
        } else {

        if(buttonIterator.hasNext()) {
            chestTemplate.set(21, buttonIterator.next());
        }
        if(buttonIterator.hasNext()) {
            chestTemplate.set(23, buttonIterator.next());
        }
        if(buttonIterator.hasNext()) {
            chestTemplate.set(29, buttonIterator.next());
        }
        if(buttonIterator.hasNext()) {
            chestTemplate.set(33, buttonIterator.next());
        }
        if(buttonIterator.hasNext()) {
            chestTemplate.set(31, buttonIterator.next());
        }}

        return GooeyPage.builder()
                .title("Texture Editor")
                .template(chestTemplate.build())
                .build();
    }

}
