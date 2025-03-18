package org.imsouhay.Grove.ui.mixin;

import com.mojang.authlib.GameProfile;
import org.imsouhay.Grove.ui.impl.PlayerExtensions;
import org.imsouhay.Grove.ui.virtual.SguiScreenHandlerFactory;
import org.imsouhay.Grove.ui.virtual.VirtualScreenHandlerInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.OptionalInt;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEntityMixin extends Player implements PlayerExtensions {
    @Unique
    public abstract void onHandledScreenClosed();

    @Unique
    private boolean sgui$ignoreNext = false;

    public ServerPlayerEntityMixin(Level world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "openMenu", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;closeContainer()V", shift = At.Shift.BEFORE))
    private void sgui$dontForceCloseFor(MenuProvider factory, CallbackInfoReturnable<OptionalInt> cir) {
        if (factory instanceof SguiScreenHandlerFactory<?> sguiScreenHandlerFactory && !sguiScreenHandlerFactory.gui().resetMousePosition()) {
            this.sgui$ignoreNext = true;
        }
    }

    @Inject(method = "closeContainer", at = @At("HEAD"), cancellable = true)
    private void sgui$ignoreClosing(CallbackInfo ci) {
        if (this.sgui$ignoreNext) {
            this.sgui$ignoreNext = false;
            this.onHandledScreenClosed();
            ci.cancel();
        }
    }

    @Inject(method = "die", at = @At("TAIL"))
    private void sgui$onDeath(DamageSource source, CallbackInfo ci) {
        if (this.containerMenu instanceof VirtualScreenHandlerInterface handler) {
            handler.getGui().close(true);
        }
    }

    @Override
    public void sgui$ignoreNextClose() {
        this.sgui$ignoreNext = true;
    }
}
