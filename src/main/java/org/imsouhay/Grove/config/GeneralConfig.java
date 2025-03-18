package org.imsouhay.Grove.config;

import com.google.gson.Gson;
import org.imsouhay.Grove.Grove;
import org.imsouhay.Grove.util.Utils;

import java.util.concurrent.CompletableFuture;

public class GeneralConfig {

    public static final GeneralConfig INSTANCE =new GeneralConfig();
    private boolean pokeDexIsEnabled;
    private boolean pokeTrainerIsEnabled;

    private boolean pokeHuntIsEnabled;

    public GeneralConfig() {
        pokeDexIsEnabled=true;
        pokeTrainerIsEnabled=true;
        pokeHuntIsEnabled=true;
    }

    public boolean isPokeDexEnabled() {return pokeDexIsEnabled;}
    public boolean isPokeTrainerEnabled() {return pokeTrainerIsEnabled;}
    public boolean isPokeHuntIsEnabled() {return pokeHuntIsEnabled;}

    public void init() {
        CompletableFuture<Boolean> futureRead = Utils.readFileAsync(Grove.BASE_PATH,
                "GeneralConfig.json", el -> {
                    Gson gson = Utils.newGson();
                    GeneralConfig gcfg = gson.fromJson(el, GeneralConfig.class);
                    pokeDexIsEnabled = gcfg.isPokeDexEnabled();
                    pokeTrainerIsEnabled = gcfg.isPokeTrainerEnabled();
                    pokeHuntIsEnabled = gcfg.isPokeHuntIsEnabled();
                });

        if (!futureRead.join()) {
            Grove.LOGGER.info("No GeneralConfig.json file found for " + Grove.MOD_ID +
                    ". Attempting to generate one");
            Gson gson = Utils.newGson();
            String data = gson.toJson(this);
            CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(Grove.BASE_PATH,
                    "GeneralConfig.json", data);

            if (!futureWrite.join()) {
                Grove.LOGGER.fatal("Could not write GeneralConfig for " + Grove.MOD_ID + ".");
            }
            return;
        }
        Grove.LOGGER.info(Grove.MOD_ID + "'s general config file read successfully");
    }
}
