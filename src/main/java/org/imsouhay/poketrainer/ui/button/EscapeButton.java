package org.imsouhay.poketrainer.ui.button;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.imsouhay.poketrainer.builder.PokeBuilder;
import org.imsouhay.poketrainer.util.Destination;
import org.imsouhay.poketrainer.util.RoutingUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EscapeButton {

    public static Button getEscapeButton(Destination destination, PokeBuilder builder) {
        return switch (destination){
            case MAIN -> getToMainButton(builder);
            case MAINANDOPTIONS -> getToMainAndOptionsButton(builder);
            case MAINANDIV -> getToMainAndIVButton(builder);
            case MAINANDEV -> getToMainAndEVButton(builder);
            case MAINANDSKIN -> getToMainAndSkin(builder);
        };
    }


    private static Button getToMainButton(PokeBuilder builder) {
        return GooeyButton.builder()
                .display(new ItemStack(Items.BARRIER))
                .title("§cReturn")
                .lore(new ArrayList<>(List.of("§7Left-Click to return to main menu")))
                .onClick(e -> RoutingUtils.toMain(e, builder))
                .build();
    }
    private static Button getToMainAndOptionsButton(PokeBuilder builder) {
            return GooeyButton.builder()
                    .display(new ItemStack(Items.BARRIER))
                    .title("§cReturn")
                    .lore(new ArrayList<>(Arrays.asList("§7Right-Click to return to the previous menu", "§7Left-Click to return to main menu")))
                    .onClick(e -> RoutingUtils.toMainAndOptions(e, builder))
                    .build();
        }

    private static Button getToMainAndIVButton(PokeBuilder builder) {
        return GooeyButton.builder()
                .display(new ItemStack(Items.BARRIER))
                .title("§cReturn")
                .lore(new ArrayList<>(Arrays.asList("§7Right-Click to return to the previous menu", "§7Left-Click to return to main menu")))
                .onClick(e -> RoutingUtils.toMainAndIV(e, builder))
                .build();
    }
    private static Button getToMainAndEVButton(PokeBuilder builder) {
        return GooeyButton.builder()
                .display(new ItemStack(Items.BARRIER))
                .title("§cReturn")
                .lore(new ArrayList<>(Arrays.asList("§7Right-Click to return to the previous menu", "§7Left-Click to return to main menu")))
                .onClick(e -> RoutingUtils.toMainAndEV(e, builder))
                .build();
    }

    private static Button getToMainAndSkin(PokeBuilder builder) {
        return GooeyButton.builder()
                .display(new ItemStack(Items.BARRIER))
                .title("§cReturn")
                .lore(new ArrayList<>(Arrays.asList("§7Right-Click to return to the previous menu", "§7Left-Click to return to main menu")))
                .onClick(e -> RoutingUtils.toMainAndSkin(e, builder))
                .build();
    }


}
