package org.imsouhay.Grove.data;

import com.cobblemon.mod.common.pokemon.Species;
import org.imsouhay.Grove.data.node.PokemonNode;
import org.imsouhay.pokedex.collection.MonsCollection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PokemonTrie {
        private final PokemonNode root;

        public PokemonTrie() {
            root = new PokemonNode();
        }
        
        public void load() {
            MonsCollection.getList().forEach(specie -> insert(specie.getName().toLowerCase(), specie));
        }

        public void insert(String name, Species value) {
            PokemonNode current = root;

            for (int i = 0; i < name.length(); i++) {

                char ch = name.charAt(i);

                if (!current.containsChild(ch)) {
                    current.setChild(ch, new PokemonNode());
                }

                current = current.getChild(ch);

            }
            current.setValue(value);
        }


        public Species search(String name) {
            PokemonNode current = root;
            for (int i = 0; i < name.length(); i++) {
                char ch = name.charAt(i);
                if (!current.containsChild(ch)) {
                    return null;
                }
                current = current.getChild(ch);
            }
            return current.getValue();
        }

        public PokemonNode getNode(String prefix) {
            PokemonNode current = root;

            if(prefix.isEmpty()) return current;


            for(int i = 0; i < prefix.length(); i++) {
                char ch = prefix.charAt(i);
                if (!current.containsChild(ch)) {
                    return null;
                }
                current = current.getChild(ch);
            }

            return current;
        }


        public List<Species> searchPrefix(String prefix) {
            PokemonNode node = getNode(prefix);
            // if node is null, that means no node that reaches that prefix has been made, meaning there is no value to be searched for
            if(node==null) return Collections.emptyList();

            Species Species;
            // try normal searching, perhaps the prefix is an actual name
            if((Species = search(prefix))!=null) return new ArrayList<>(List.of(Species));


            return new ArrayList<>(node.getAllChildren());
        }
}
