package org.imsouhay.Grove.ui.mixin;

import org.imsouhay.Grove.ui.api.gui.SignGui;
import org.imsouhay.Grove.ui.virtual.FakeScreenHandler;
import org.imsouhay.Grove.ui.virtual.VirtualScreenHandlerInterface;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.FilteredText;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;


@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerPlayNetworkHandlerMixin {

    @Unique
    private AbstractContainerMenu sgui$previousScreen = null;

    @Shadow
    public ServerPlayer player;


    @Shadow public abstract void send(Packet<?> p_9830_);

    @Inject(method = "handleContainerClose", at = @At("HEAD"), cancellable = true)
    private void sgui$storeScreenHandler(ServerboundContainerClosePacket p_9858_, CallbackInfo ci) {
        if (this.player.containerMenu instanceof VirtualScreenHandlerInterface handler) {
            if (handler.getGui().canPlayerClose()) {
                this.sgui$previousScreen = this.player.containerMenu;
            } else {
                var screenHandler = this.player.containerMenu;
                screenHandler.getType();
                try {
                    this.send(new ClientboundOpenScreenPacket(screenHandler.containerId, screenHandler.getType(), handler.getGui().getTitle()));
                    screenHandler.sendAllDataToRemote();
                } catch (Throwable e) {

                }
                ci.cancel();
            }

        }
    }

    @Inject(method = "handleContainerClose", at = @At("TAIL"))
    private void sgui$executeClosing(ServerboundContainerClosePacket p_9858_, CallbackInfo ci) {
        try {
            if (this.sgui$previousScreen != null) {
                if (this.sgui$previousScreen instanceof VirtualScreenHandlerInterface screenHandler) {
                    screenHandler.getGui().close(true);
                }
            }
        } catch (Throwable e) {
            if (this.sgui$previousScreen instanceof VirtualScreenHandlerInterface screenHandler) {
                screenHandler.getGui().handleException(e);
            } else {
                e.printStackTrace();
            }
        }
        this.sgui$previousScreen = null;
    }

    @Inject(method = "updateSignText", at = @At("HEAD"), cancellable = true)
    private void sgui$catchSignUpdate(ServerboundSignUpdatePacket packet, List<FilteredText> signText, CallbackInfo ci) {
        try {
            if (this.player.containerMenu instanceof FakeScreenHandler fake && fake.getGui() instanceof SignGui gui) {
                for (int i = 0; i < packet.getLines().length; i++) {
                    gui.setLineInternal(i, Component.literal(packet.getLines()[i]));
                }
                gui.close(true);
                ci.cancel();
            }
        } catch (Throwable e) {
            if (this.player.containerMenu instanceof VirtualScreenHandlerInterface handler ) {
                handler.getGui().handleException(e);
            } else {
                e.printStackTrace();
            }
        }
    }


}
