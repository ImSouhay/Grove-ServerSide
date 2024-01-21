package org.imsouhay.poketrainer.config;


import com.cobblemon.mod.common.api.pokeball.PokeBalls;
import com.google.gson.Gson;
import org.imsouhay.LavenderMcServerSide.util.Utils;
import org.imsouhay.poketrainer.PokeTrainer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.imsouhay.LavenderMcServerSide.LavenderMcServerSide.LOGGER;

public class Config {

    public boolean feedbackEnabled;
    public int defaultBalance;
    public Map<String, Integer> prices=new LinkedHashMap<>();

    public Config() {
        defaultBalance=0;
        feedbackEnabled=true;

        prices.put("nature", 400);
        prices.put("toMale", 200);
        prices.put("toFemale", 200);
        prices.put("shiny", 400);
        prices.put("unShiny", 100);
        prices.put("ability", 200);
        prices.put("skin", 200);

        prices.put("+3_level", 10);
        prices.put("+5_level", 20);
        prices.put("+10_level", 30);
        prices.put("+15_level", 50);
        prices.put("+20_level", 60);
        prices.put("+30_level", 100);
        prices.put("+40_level", 125);
        prices.put("+50_level", 150);

        prices.put("-3_level", 5);
        prices.put("-5_level", 7);
        prices.put("-10_level", 10);
        prices.put("-15_level", 15);
        prices.put("-20_level", 17);
        prices.put("-30_level", 22);
        prices.put("-40_level", 27);
        prices.put("-50_level", 35);

        prices.put("+3_statPoint", 10);
        prices.put("+5_statPoint", 20);
        prices.put("+10_statPoint", 30);
        prices.put("+15_statPoint", 50);
        prices.put("+20_statPoint", 60);
        prices.put("+30_statPoint", 100);
        prices.put("+40_statPoint", 125);
        prices.put("+50_statPoint", 150);

        prices.put("-3_statPoint", 5);
        prices.put("-5_statPoint", 7);
        prices.put("-10_statPoint", 10);
        prices.put("-15_statPoint", 15);
        prices.put("-20_statPoint", 17);
        prices.put("-30_statPoint", 22);
        prices.put("-40_statPoint", 27);
        prices.put("-50_statPoint", 35);

        PokeBalls balls=PokeBalls.INSTANCE;

        prices.put(balls.getAZURE_BALL().getName().getPath(), 200);
        prices.put(balls.getBEAST_BALL().getName().getPath(), 200);
        prices.put(balls.getCHERISH_BALL().getName().getPath(), 200);
        prices.put(balls.getDIVE_BALL().getName().getPath(), 200);
        prices.put(balls.getDREAM_BALL().getName().getPath(), 200);
        prices.put(balls.getDUSK_BALL().getName().getPath(), 200);
        prices.put(balls.getFRIEND_BALL().getName().getPath(), 200);
        prices.put(balls.getGREAT_BALL().getName().getPath(), 200);
        prices.put(balls.getHEAL_BALL().getName().getPath(), 200);
        prices.put(balls.getHEAVY_BALL().getName().getPath(), 200);
        prices.put(balls.getLEVEL_BALL().getName().getPath(), 200);
        prices.put(balls.getLOVE_BALL().getName().getPath(), 200);
        prices.put(balls.getLURE_BALL().getName().getPath(), 200);
        prices.put(balls.getLUXURY_BALL().getName().getPath(), 200);
        prices.put(balls.getMASTER_BALL().getName().getPath(), 200);
        prices.put(balls.getMOON_BALL().getName().getPath(), 200);
        prices.put(balls.getNEST_BALL().getName().getPath(), 200);
        prices.put(balls.getNET_BALL().getName().getPath(), 200);
        prices.put(balls.getPARK_BALL().getName().getPath(), 200);
        prices.put(balls.getPOKE_BALL().getName().getPath(), 200);
        prices.put(balls.getPREMIER_BALL().getName().getPath(), 200);
        prices.put(balls.getQUICK_BALL().getName().getPath(), 200);
        prices.put(balls.getREPEAT_BALL().getName().getPath(), 200);
        prices.put(balls.getSAFARI_BALL().getName().getPath(), 200);
        prices.put(balls.getSPORT_BALL().getName().getPath(), 200);
        prices.put(balls.getTIMER_BALL().getName().getPath(), 200);
        prices.put(balls.getULTRA_BALL().getName().getPath(), 200);
        prices.put(balls.getSLATE_BALL().getName().getPath(), 200);
    }

    public Map<String, Integer> getPrices() {
        return prices;
    }

    public int getPriceOf(String key) {
        return prices.get(key);
    }

    public int getDefaultBalance() {
        return defaultBalance;
    }

    public boolean isFeedbackEnabled() {
        return feedbackEnabled;
    }

    public void init() {
        CompletableFuture<Boolean> futureRead = Utils.readFileAsync(PokeTrainer.POKE_TRAINER_PATH, "config.json",
                el -> {
                    Gson gson = Utils.newGson();
                    Config config = gson.fromJson(el, Config.class);
                    prices=config.getPrices();
                    defaultBalance=config.getDefaultBalance();
                    feedbackEnabled=config.isFeedbackEnabled();
        });

        if (!futureRead.join()) {
            LOGGER.info("No config.json file found for PokeTrainer. Attempting to " +
                    "generate one.");
            Gson gson = Utils.newGson();
            String data = gson.toJson(this);
            CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(PokeTrainer.POKE_TRAINER_PATH, "config.json", data);

            if (!futureWrite.join()) {
                LOGGER.fatal("Could not write config.json for PokeTrainer.");
            }
            return;
        }
        LOGGER.info("PokeTrainer config file read successfully.");
    }
}
