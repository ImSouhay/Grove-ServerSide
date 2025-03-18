package org.imsouhay.poketrainer.config;

import com.google.gson.Gson;
import org.imsouhay.Grove.util.Utils;
import org.imsouhay.poketrainer.PokeTrainer;

import java.util.concurrent.CompletableFuture;

import static org.imsouhay.Grove.Grove.LOGGER;

public class Lang {
    private String fillerMaterial;
    private String mainMenuFillerMaterial;
    private String homeMenuFillerMaterial;
    private String mainMenuTitle;
    private String homeMenuTitle;
    private String inactiveEventBallLore;
    private String activeEventBallLore;

    public Lang() {
        fillerMaterial="minecraft:orange_stained_glass_pane";
        mainMenuFillerMaterial="minecraft:orange_stained_glass_pane";
        homeMenuFillerMaterial="minecraft:orange_stained_glass_pane";
        mainMenuTitle="PokeTrainer";
        homeMenuTitle="PokeTrainer - Home";
        inactiveEventBallLore ="§7This PokeBall is not Currently Available.";
        activeEventBallLore = "§5Special Limited-Time Event PokeBall.";
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

    public String getInactiveEventBallLore() {
        return inactiveEventBallLore;
    }

    public String getActiveEventBallLore() {
        return activeEventBallLore;
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
                    inactiveEventBallLore = lang.getInactiveEventBallLore();
                    activeEventBallLore = lang.getActiveEventBallLore();
                });

        if (!futureRead.join()) {
            LOGGER.info("No lang.json file found for PokeTrainer. Attempting to " +
                    "generate one.");}
            Gson gson = Utils.newGson();
            String data = gson.toJson(this);
            CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(PokeTrainer.POKE_TRAINER_PATH, "lang.json", data);

            if (!futureWrite.join()) {
                LOGGER.fatal("Could not write lang.json for PokeTrainer.");

            return;
        }
        LOGGER.info("PokeTrainer lang file read successfully.");
    }
}
