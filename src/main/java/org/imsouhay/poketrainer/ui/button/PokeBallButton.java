package org.imsouhay.poketrainer.ui.button;


import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import com.cobblemon.mod.common.api.pokeball.PokeBalls;
import com.cobblemon.mod.common.pokeball.PokeBall;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.imsouhay.Grove.util.Utils;
import org.imsouhay.poketrainer.PokeTrainer;
import org.imsouhay.poketrainer.builder.PokeBuilder;
import org.imsouhay.poketrainer.economy.TransactionManager;

import java.util.*;

import static org.imsouhay.Grove.util.Utils.format;

public class PokeBallButton {
    private static final String[] colors={"§c", "§f", "§d", "§b", "§8", "§4", "§7", "§e", "§a", "§2", "§3"};
    private static final Random random = new Random();

    public static List<Button> getButtons(PokeBuilder builder) {
        return makeButtons(builder);
    }

    private static List<Button> makeButtons(PokeBuilder builder) {
        List<Button> buttons = new ArrayList<>();
        int counter = 0;

        for(PokeBall pokeBall:PokeBalls.INSTANCE.all()) {
            if(!PokeTrainer.config.isBlocked(pokeBall) && !PokeTrainer.config.hasEvent(pokeBall)) {
                buttons.add(buildButton(pokeBall, colors[counter], builder));
                counter++;
                if(counter>=11) counter=0;
                continue;
            }

            ArrayList<String> lore=buildLore(pokeBall.getName().getPath());
            lore.add(0, PokeTrainer.lang.getActiveEventBallLore());

            int eventCode=PokeTrainer.config.isEventActive(pokeBall);
            if(eventCode == -1) {
                continue;
            }
            
            boolean eventActive=(eventCode==1);

            buttons.add(GooeyButton.builder()
                    .title((eventActive ? colors[random.nextInt(11)]:"§c")+format(pokeBall.getName().getPath()))
                    .display(eventActive ? new ItemStack(pokeBall.item().asItem()):new ItemStack(Items.BARRIER))
                    .lore(eventActive ? lore:List.of(PokeTrainer.lang.getInactiveEventBallLore()))
                    .onClick(e ->{
                        if(eventCode==0) {
                            e.getPlayer().sendSystemMessage(Component.nullToEmpty(PokeTrainer.fBack.get("pokeBallEventNotCurrentlyAvailable")));
                            return;
                        }


                        TransactionManager.handleWithdraw(
                                e,
                                PokeTrainer.config.getPriceOf(pokeBall.getName().getPath()),
                                () -> {
                                    if(builder.getCaughtBall()!=pokeBall) {
                                        builder.setCaughtBall(pokeBall);
                                        if(PokeTrainer.config.isFeedbackEnabled()) Utils.sendFeedBack(
                                                e.getPlayer(),
                                                "pokeball",
                                                String.valueOf(PokeTrainer.config.getPriceOf(pokeBall.getName().getPath())),
                                                builder.getName(),
                                                format(pokeBall.getName().getPath()));
                                        builder.reloadButton();
                                        return true;
                                    }
                                    if(PokeTrainer.config.isFeedbackEnabled()) Utils.sendFeedBack(e.getPlayer(),
                                            "pokemonAlreadyCaughtWithBall",
                                            String.valueOf(PokeTrainer.config.getPriceOf(pokeBall.getName().getPath())),
                                            builder.getName(),
                                            format(pokeBall.getName().getPath()));
                                    return false;
                                }
                        );})
                    .build());
        }
        return buttons;
    }


    private static Button buildButton(PokeBall ball, String colorCode, PokeBuilder builder) {
        return GooeyButton.builder()
                .title(colorCode+format(ball.getName().getPath()))
                .display(new ItemStack(ball.item().asItem()))
                .lore(buildLore(ball.getName().getPath()))
                .onClick(e ->
                        TransactionManager.handleWithdraw(
                                e,
                                PokeTrainer.config.getPriceOf(ball.getName().getPath()),
                                () -> {
                                    if(builder.getCaughtBall()!=ball) {
                                        builder.setCaughtBall(ball);
                                        if(PokeTrainer.config.isFeedbackEnabled()) Utils.sendFeedBack(
                                                e.getPlayer(),
                                                "pokeball",
                                                String.valueOf(PokeTrainer.config.getPriceOf(ball.getName().getPath())),
                                                builder.getName(),
                                                format(ball.getName().getPath()));
                                        builder.reloadButton();
                                        return true;
                                    }
                                    if(PokeTrainer.config.isFeedbackEnabled()) Utils.sendFeedBack(e.getPlayer(),
                                            "pokemonAlreadyCaughtWithBall",
                                            String.valueOf(PokeTrainer.config.getPriceOf(ball.getName().getPath())),
                                            builder.getName(),
                                            format(ball.getName().getPath()));
                                    return false;
                                }
                        ))
                .build();
    }

    private static ArrayList<String> buildLore(String ballName) {
        String defaultLore="§7Click to change caught ball to @pokeball!";
        return new ArrayList<>(List.of(
                defaultLore.replace("@pokeball", format(ballName)),
                "",
                Utils.price(PokeTrainer.config.getPriceOf(ballName))));
    }

}
