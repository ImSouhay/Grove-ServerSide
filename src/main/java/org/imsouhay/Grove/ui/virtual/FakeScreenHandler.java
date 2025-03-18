package org.imsouhay.Grove.ui.virtual;

import org.imsouhay.Grove.ui.api.gui.GuiInterface;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Some guis don't use screen handlers (Sign or book input)
 * This is mostly utility class to simplify implementation
 */
public class FakeScreenHandler extends AbstractContainerMenu implements VirtualScreenHandlerInterface {

    private final GuiInterface gui;

    public FakeScreenHandler(GuiInterface gui) {
        super(null, -1);
        this.gui = gui;
    }

    @Override
    public void addListener(ContainerListener listener) {

    }

    @Override
    public GuiInterface getGui() {
        return this.gui;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public void sendContentUpdates() {
        try {
            this.gui.onTick();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.broadcastChanges();
    }

    @Override
    public ItemStack quickMove(Player player, int index) {
        return null;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
        return null;
    }

    @Override
    public boolean stillValid(Player p_38874_) {
        return true;
    }
}
