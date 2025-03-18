package org.imsouhay.poketrainer;

import org.imsouhay.Grove.Grove;
import org.imsouhay.poketrainer.account.AccountProvider;
import org.imsouhay.poketrainer.config.Config;
import org.imsouhay.poketrainer.config.FeedBack;
import org.imsouhay.poketrainer.config.Lang;

public class PokeTrainer {
    public static final String POKE_TRAINER_PATH = Grove.BASE_PATH+"poketrainer/";

    public static final Lang lang=new Lang();
    public static final Config config=new Config();
    public static final FeedBack fBack=new FeedBack();
    public static void load() {
        config.init();
        lang.init();
        fBack.init();
        AccountProvider.init();
    }

}
