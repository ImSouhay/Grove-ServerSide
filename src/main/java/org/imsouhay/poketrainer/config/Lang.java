package org.imsouhay.poketrainer.config;

import com.google.gson.Gson;
import org.imsouhay.LavenderMcServerSide.util.Utils;
import org.imsouhay.poketrainer.PokeTrainer;

import java.util.concurrent.CompletableFuture;

import static org.imsouhay.LavenderMcServerSide.LavenderMcServerSide.LOGGER;

public class Lang {
    private String fillerMaterial;
    private String mainMenuFillerMaterial;
    private String homeMenuFillerMaterial;
    private String mainMenuTitle;
    private String homeMenuTitle;

    public Lang() {
        fillerMaterial="minecraft:orange_stained_glass_pane";
        mainMenuFillerMaterial="minecraft:orange_stained_glass_pane";
        homeMenuFillerMaterial="minecraft:orange_stained_glass_pane";
        mainMenuTitle="PokeBuilder";
        homeMenuTitle="PokeBuilder - Home";
    }

    public String getFillerMaterial() {return  fillerMaterial;}
    public String getMainMenuTitle() {return  mainMenuTitle;}
    public String getHomeMenuTitle() {return  homeMenuTitle;}
    public String getMainMenuFillerMaterial() {
        return mainMenuFillerMaterial;
    }

    public String getHomeMenuFillerMaterial() {
        return homeMenuFillerMaterial;
    }

    /**
     * Method to initialize the config.
     */
    public void init() {
        CompletableFuture<Boolean> futureRead = Utils.readFileAsync(PokeTrainer.POKE_TRAINER_PATH, "lang.json",
                el -> {
                    Gson gson = Utils.newGson();
                    Lang lang = gson.fromJson(el, Lang.class);
                    mainMenuTitle = lang.getMainMenuTitle();
                    homeMenuTitle = lang.getHomeMenuTitle();
                    fillerMaterial = lang.getFillerMaterial();
                    mainMenuFillerMaterial= lang.getMainMenuFillerMaterial();
                    homeMenuFillerMaterial= lang.getHomeMenuFillerMaterial();
                });

        if (!futureRead.join()) {
            LOGGER.info("No lang.json file found for PokeTrainer. Attempting to " +
                    "generate one.");
            Gson gson = Utils.newGson();
            String data = gson.toJson(this);
            CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(PokeTrainer.POKE_TRAINER_PATH, "lang.json", data);

            if (!futureWrite.join()) {
                LOGGER.fatal("Could not write lang.json for PokeTrainer.");
            }
            return;
        }
        LOGGER.info("PokeTrainer lang file read successfully.");
    }
}
