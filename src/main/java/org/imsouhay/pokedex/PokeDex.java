package org.imsouhay.pokedex;

import org.imsouhay.Grove.Grove;
import org.imsouhay.pokedex.account.AccountProvider;
import org.imsouhay.pokedex.collection.MonsCollection;
import org.imsouhay.pokedex.config.Config;
import org.imsouhay.pokedex.config.Lang;

public class PokeDex {


    public static final Lang lang=new Lang();
    public static final Config config=new Config();
    public static final String POKE_DEX_PATH = Grove.BASE_PATH+"pokedex/";

    public static void load() {
        config.init();
        lang.init();
        if(!MonsCollection.getList().isEmpty()) {
            AccountProvider.init();
        }
    }
}
