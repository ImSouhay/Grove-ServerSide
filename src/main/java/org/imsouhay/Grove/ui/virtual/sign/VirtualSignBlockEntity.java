package org.imsouhay.Grove.ui.virtual.sign;

import org.imsouhay.Grove.ui.mixin.SignBlockEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;


public class VirtualSignBlockEntity extends SignBlockEntity {

    public VirtualSignBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public boolean setText(SignText text, boolean front) {
        return front ? this.setFrontText(text) : this.setBackText(text);
    }

    private boolean setBackText(SignText backText) {
        if (backText != this.getBackText()) {
            ((SignBlockEntityAccessor) this).setBackText(backText);
            return true;
        } else {
            return false;
        }
    }

    private boolean setFrontText(SignText frontText) {
        if (frontText != this.getFrontText()) {
            ((SignBlockEntityAccessor) this).setFrontText(frontText);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean playerIsTooFarAwayToEdit(UUID uuid) {
        return false;
    }

}
