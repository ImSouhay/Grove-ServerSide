package org.imsouhay.poketrainer;

import org.imsouhay.LavenderMcServerSide.LavenderMcServerSide;
import org.imsouhay.poketrainer.account.AccountProvider;
import org.imsouhay.poketrainer.config.Config;
import org.imsouhay.poketrainer.config.Lang;

public class PokeTrainer {
    public static final String POKE_TRAINER_PATH = LavenderMcServerSide.BASE_PATH+"/poketrainer/";

    public static final Lang lang=new Lang();
    public static final Config config=new Config();
    public static void load() {
        config.init();
        lang.init();
        AccountProvider.init();
    }

}
