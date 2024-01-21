package org.imsouhay.poketrainer.util;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.ButtonAction;
import ca.landonjw.gooeylibs2.api.button.ButtonClick;
import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import net.minecraft.world.item.Item;
import org.imsouhay.LavenderMcServerSide.util.Utils;
import org.imsouhay.poketrainer.builder.PokeBuilder;
import org.imsouhay.poketrainer.ui.MainMenu;
import org.imsouhay.poketrainer.ui.OptionsMenu;
import org.imsouhay.poketrainer.ui.menu.EvEditMenu;
import org.imsouhay.poketrainer.ui.menu.IvEditMenu;
import org.imsouhay.poketrainer.ui.menu.SkinChoseEditMenu;
import org.imsouhay.poketrainer.ui.menu.StatEditMenu;


public class RoutingUtils {

    public static void toMainAndOptions(ButtonAction e, PokeBuilder builder) {
        if(e.getClickType() == ButtonClick.RIGHT_CLICK) {
            toOptions(e, builder);
            return;
        }
        toMain(e, builder);
    }

    public static void toMainAndIV(ButtonAction e, PokeBuilder builder) {
        if(e.getClickType() == ButtonClick.RIGHT_CLICK) {
            toIV(e, builder);
            return;
        }
        toMain(e, builder);
    }

    public static void toMainAndEV(ButtonAction e, PokeBuilder builder) {
        if(e.getClickType() == ButtonClick.RIGHT_CLICK) {
            toEV(e, builder);
            return;
        }
        toMain(e, builder);
    }

    public static void toMainAndSkin(ButtonAction e, PokeBuilder builder) {
        if(e.getClickType() == ButtonClick.RIGHT_CLICK) {
            toSkin(e, builder);
            return;
        }
        toMain(e, builder);
    }



    public static void toOptions(ButtonAction e, PokeBuilder builder) {
        UIManager.openUIForcefully(e.getPlayer(), OptionsMenu.getPage(builder));
    }

    public static void toMain(ButtonAction e, PokeBuilder builder) {
        UIManager.openUIForcefully(e.getPlayer(), MainMenu.getPage(e.getPlayer()));
    }

    public static void toSkin(ButtonAction e, PokeBuilder builder) {
        UIManager.openUIForcefully(e.getPlayer(), SkinChoseEditMenu.getPage(builder, Utils.getFeatureFromPokemon(builder.getPokemon())));
    }


    public static void toIV(ButtonAction e, PokeBuilder builder) {
        UIManager.openUIForcefully(e.getPlayer(), IvEditMenu.getPage(builder));
    }

    public static void toEV(ButtonAction e, PokeBuilder builder) {
        UIManager.openUIForcefully(e.getPlayer(), EvEditMenu.getPage(builder));
    }
    public static void toStatEditor(ButtonAction e, PokeBuilder builder, Destination destination, vType type, Stat stat,
                                    Operation operation, Item displayItem) {
        UIManager.openUIForcefully(e.getPlayer(), StatEditMenu.getPage(
                builder,
                destination,
                type,
                stat,
                operation,
                displayItem));
    }
}
