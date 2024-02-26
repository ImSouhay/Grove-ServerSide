package org.imsouhay.LavenderMcServerSide.config;

import com.google.gson.Gson;
import org.imsouhay.LavenderMcServerSide.LavenderMcServerSide;
import org.imsouhay.LavenderMcServerSide.util.Utils;

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

    public boolean isPokeHuntIsEnabled() {
        return pokeHuntIsEnabled;
    }

    public void init() {
        CompletableFuture<Boolean> futureRead = Utils.readFileAsync(LavenderMcServerSide.BASE_PATH,
                "GeneralConfig.json", el -> {
                    Gson gson = Utils.newGson();
                    GeneralConfig gcfg = gson.fromJson(el, GeneralConfig.class);
                    pokeDexIsEnabled = gcfg.isPokeDexEnabled();
                    pokeTrainerIsEnabled = gcfg.isPokeTrainerEnabled();
                    pokeHuntIsEnabled = gcfg.isPokeHuntIsEnabled();
                });

        if (!futureRead.join()) {
            LavenderMcServerSide.LOGGER.info("No GeneralConfig.json file found for " + LavenderMcServerSide.MOD_ID +
                    ". Attempting to generate one");
            Gson gson = Utils.newGson();
            String data = gson.toJson(this);
            CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(LavenderMcServerSide.BASE_PATH,
                    "GeneralConfig.json", data);

            if (!futureWrite.join()) {
                LavenderMcServerSide.LOGGER.fatal("Could not write GeneralConfig for " + LavenderMcServerSide.MOD_ID + ".");
            }
            return;
        }
        LavenderMcServerSide.LOGGER.info(LavenderMcServerSide.MOD_ID + "'s general config file read successfully");
    }
}
