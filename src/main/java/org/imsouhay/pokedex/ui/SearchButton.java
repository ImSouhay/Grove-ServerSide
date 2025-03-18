package org.imsouhay.pokedex.ui;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.ButtonAction;
import ca.landonjw.gooeylibs2.api.button.ButtonClick;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import static org.imsouhay.Grove.Grove.TRIE;
import static org.imsouhay.Grove.Grove.IMPLEMENTED_TRIE;

import net.minecraft.world.level.block.Blocks;
import org.imsouhay.Grove.ui.api.gui.SignGui;
import org.imsouhay.pokedex.PokeDex;

import java.util.*;

public class SearchButton {

    public static Button getButton(UUID playerUUID) {
        return GooeyButton.builder()
                .display(new ItemStack(Items.OAK_SIGN))
                .title(PokeDex.lang.getSearchButtonTitle())
                .lore(PokeDex.lang.getSearchButtonLore())
                .onClick(e -> {

                    if(e.getPlayer().getY() > e.getPlayer().level().getMaxBuildHeight()+5 ||
                       e.getPlayer().getY() < e.getPlayer().level().getMinBuildHeight()-5) {
                        e.getPlayer().sendSystemMessage(Component.nullToEmpty(
                                "§cYou can't use this feature while being this high in the sky."));
                        return;
                    }

                    SignGui sGui = new SignGui(e.getPlayer()) {
                        private int tick = 0;

                        {
                            this.setSignType(Blocks.OAK_WALL_SIGN);
                            this.setColor(DyeColor.BLACK);
                            this.setLine(2, Component.literal("^^^^^^^^^^^^^^^"));
                            this.setLine(3, Component.literal(e.getClickType().equals(ButtonClick.LEFT_CLICK)? "Enter Mon Name":"Enter DexNumber"));
                            this.setAutoUpdate(false);
                        }

                        @Override
                        public void onClose() {
                            UIManager.openUIForcefully(e.getPlayer(), DexMenu.getPage(e.getPlayer().getUUID(), handleInput(e, this)));
                        }

                        @Override
                        public void onTick() {
                        }
                    };
		    sGui.open();
                }).build();
    }

    public static List<Species> handleInput(ButtonAction e, SignGui sGUI) {
        return e.getClickType().equals(ButtonClick.LEFT_CLICK)? handleInputLeftClick(sGUI):handleInputRightClick(sGUI);
    }
    public static List<Species> handleInputRightClick(SignGui sGUI) {
        try {
            int dexNum=Integer.parseInt((sGUI.getLine(0).getString()+sGUI.getLine(1).getString()).trim());
	        Species specie=PokemonSpecies.INSTANCE.getByPokedexNumber(dexNum, Cobblemon.MODID);

            if(specie==null) {
		        return Collections.emptyList();
            } else {
		        return new ArrayList<>(List.of(specie));
	        }
        } catch (Exception e) {
            if(e instanceof NumberFormatException) {
                sGUI.getPlayer().sendSystemMessage(Component.nullToEmpty(PokeDex.config.getInvalidPokeDexSearchFormat()));
            }
            return Collections.emptyList();
        }
    }
    public static List<Species> handleInputLeftClick(SignGui sGUI) {
        if((sGUI.getLine(0).getString() + sGUI.getLine(1).getString()).trim().isEmpty()) return Collections.emptyList();

        return PokeDex.config.isImplementedOnly()?
                IMPLEMENTED_TRIE.searchPrefix((sGUI.getLine(0).getString() + sGUI.getLine(1).getString()).trim()) :
                TRIE.searchPrefix((sGUI.getLine(0).getString() + sGUI.getLine(1).getString()).trim());
    }

}
