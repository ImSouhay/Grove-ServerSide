package org.imsouhay.poketrainer.ui.button;


import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import com.cobblemon.mod.common.api.pokeball.PokeBalls;
import com.cobblemon.mod.common.pokeball.PokeBall;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.imsouhay.LavenderMcServerSide.util.Utils;
import org.imsouhay.poketrainer.PokeTrainer;
import org.imsouhay.poketrainer.builder.PokeBuilder;
import org.imsouhay.poketrainer.economy.TransactionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.imsouhay.LavenderMcServerSide.util.Utils.format;

public class PokeBallButton {
    private static List<Button> buttons;

    public static List<Button> getButtons(PokeBuilder builder) {
        makeButtons(builder);
        return buttons;
    }

    private static void makeButtons(PokeBuilder builder) {
        PokeBalls balls=PokeBalls.INSTANCE;
        
        Button filler=GooeyButton.builder()
                .title("")
                .hideFlags(FlagType.All)
                .lore(new ArrayList<>())
                .display(Utils.parseItemId(PokeTrainer.lang.getFillerMaterial()))
                .build();
        
        buttons = new LinkedList<>(Arrays.asList(
                buildButton(balls.getAZURE_BALL(), "§c", builder),
                buildButton(balls.getBEAST_BALL(), "§f", builder),
                buildButton(balls.getCHERISH_BALL(), "§d", builder),
                buildButton(balls.getDIVE_BALL(), "§b", builder),
                buildButton(balls.getDREAM_BALL(), "§f", builder),
                buildButton(balls.getDUSK_BALL(), "§8", builder),
                buildButton(balls.getFRIEND_BALL(), "§b", builder),
                buildButton(balls.getGREAT_BALL(), "§4", builder),
                buildButton(balls.getHEAL_BALL(), "§c", builder),
                buildButton(balls.getHEAVY_BALL(), "§7", builder),
                buildButton(balls.getLEVEL_BALL(), "§e", builder),
                buildButton(balls.getLOVE_BALL(), "§d", builder),
                buildButton(balls.getLURE_BALL(), "§f", builder),
                buildButton(balls.getLUXURY_BALL(), "§6", builder),
                buildButton(balls.getMASTER_BALL(), "§0", builder),
                buildButton(balls.getMOON_BALL(), "§7", builder),
                buildButton(balls.getNEST_BALL(), "§e", builder),
                buildButton(balls.getNET_BALL(), "§f", builder),
                buildButton(balls.getPARK_BALL(), "§a", builder),
                buildButton(balls.getPOKE_BALL(), "§c", builder),
                buildButton(balls.getPREMIER_BALL(), "§5", builder),
                buildButton(balls.getQUICK_BALL(), "§7", builder),
                buildButton(balls.getREPEAT_BALL(), "§8", builder),
                buildButton(balls.getSAFARI_BALL(), "§2", builder),
                buildButton(balls.getSPORT_BALL(), "§3", builder),
                buildButton(balls.getTIMER_BALL(), "§f", builder),
                buildButton(balls.getULTRA_BALL(), "§4", builder),
                buildButton(balls.getSLATE_BALL(), "§7", builder)
        ));
    }


    private static Button buildButton(PokeBall ball, String colorCode, PokeBuilder builder) {
        return GooeyButton.builder()
                .title(colorCode+format(ball.getName().getPath()))
                .display(new ItemStack(ball.item().asItem()))
                .lore(buildLore(ball.getName().getPath(), ball.getName().getPath()))
                .onClick(e ->
                        TransactionHandler.handleWithdraw(
                                e,
                                PokeTrainer.config.getPriceOf(ball.getName().getPath()),
                                () -> {
                                    if(builder.getCaughtBall()!=ball) {
                                        builder.setCaughtBall(ball);
                                        if(PokeTrainer.config.isFeedbackEnabled()) e.getPlayer().sendSystemMessage(Component.nullToEmpty("You have set your "+builder.getName()+"'s Caught Ball to "+format(ball.getName().getPath())+"."));
                                        return true;
                                    }
                                    if(PokeTrainer.config.isFeedbackEnabled()) e.getPlayer().sendSystemMessage(Component.nullToEmpty("§cYour "+builder.getName()+" is already caught with this ball."));
                                    return false;
                                }
                        ))
                .build();
    }

    private static ArrayList<String> buildLore(String ballName, String ballPath) {
        String defaultLore="§7Click to change caught ball to @pokeball!";
        return new ArrayList<>(List.of(
                defaultLore.replace("@pokeball", format(ballName)),
                "",
                Utils.price(PokeTrainer.config.getPriceOf(ballPath))));
    }

}
