package org.imsouhay.Grove.ui.virtual;

import org.imsouhay.Grove.ui.api.gui.GuiInterface;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import org.jetbrains.annotations.NotNull;

public record SguiScreenHandlerFactory<T extends GuiInterface>(T gui, MenuConstructor factory) implements MenuProvider {

    @Override
    public @NotNull Component getDisplayName() {
        Component text = this.gui.getTitle();
        if (text == null) {
            text = Component.empty();
        }
        return text;
    }

    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return factory.createMenu(syncId, playerInventory, player);
    }
}
