package org.imsouhay.Grove.data.node;

import com.cobblemon.mod.common.pokemon.Species;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PokemonNode {
    protected final Map<Character, PokemonNode> children = new HashMap<>();
    protected Species mon;

    public PokemonNode getChild(char ch) {
        return children.get(ch);
    }

    public void setChild(char ch, PokemonNode node) {
        children.put(ch, node);
    }

    public boolean containsChild(char ch) {
        return children.containsKey(ch);
    }


    public Species getValue() {
        return mon;
    }

    public void setValue(Species mon) {
        this.mon = mon;
    }

    public List<Species> getAllChildren() {
        List<Species> list = mon == null ? new ArrayList<>() : new ArrayList<>(List.of(mon));

        for (PokemonNode node : children.values()) {
            if (node != null) {
                list.addAll(node.getAllChildren());
            }
        }

        return list;
    }
}
