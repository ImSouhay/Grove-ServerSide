package org.imsouhay.poketrainer.builder;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveSet;
import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.item.PokemonItem;
import org.imsouhay.LavenderMcServerSide.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.imsouhay.LavenderMcServerSide.util.Utils.format;

public class PokeBuilderButton {
    private Button button;

    public Button build(PokeBuilder builder) {
        button = GooeyButton.builder()
                .title("§b"+builder.getName())
                .display(PokemonItem.from(builder.getPokemon()))
                .lore(buildLore(builder))
                .build();
        return button;
    }

    public Button getButton() {
        return button;
    }

    public void rebuild(PokeBuilder builder) {
        this.build(builder);
    }

    private List<String> buildLore(PokeBuilder builder) {
        return new ArrayList<>(Arrays.asList(
                "§7Held Item: §e"+format(builder.getHeldItem().getDisplayName().getString()),
                "§7Ability: §e"+format(builder.getAbility().getName()),
                "§7Nature: §e"+format(builder.getNature().getName().getPath()),
                "§7Gender: §e"+builder.getGenderName(),
                "§7Friendship: §e"+builder.getFriendShip(),
                "§7Hidden Power: §e"+format(builder.getPokemon().getTeraType().getName()),
                "§7Caught Ball: §e"+format(builder.getCaughtBall().getName().getPath()),
                "§7IVs: §e"+builder.getIVsTotal()+"§7/§e"+builder.getMaxIVs(),
                buildStatsLine(builder.getIvStats()),
                "§7EVs: §e"+builder.getEVsTotal()+"§7/§e"+builder.getMaxEVs(),
                buildStatsLine(builder.getEvStats()),
                "§7Moves:",
                buildMovesLine(builder.getMoves())
        ));
    }

    private String buildStatsLine(Map<Stat, Integer> map) {
        return String.format(
                "§cHP: %d §7/ §6Atk: %d §7/ §eDef: %d §7/ §9SpA %d §7/ §aSpD: %d §7/ §fSpe %d",
                map.get(Stats.HP),
                map.get(Stats.ATTACK),
                map.get(Stats.DEFENCE),
                map.get(Stats.SPECIAL_ATTACK),
                map.get(Stats.SPECIAL_DEFENCE),
                map.get(Stats.SPEED)
        );
    }

    private String buildMovesLine(MoveSet moveSet) {
        StringBuilder result= new StringBuilder();
        List<Move> moves=moveSet.getMoves();

        for(Move move: moves) {
            result.append("§b"+format(move.getName()));
            if(moves.size()!=moves.indexOf(move)+1) {
                result.append(" §7- ");
            }
        }

        return result.toString();
    }

}
