package org.imsouhay.poketrainer.config;

import com.google.gson.Gson;
import org.imsouhay.Grove.util.Utils;
import org.imsouhay.poketrainer.PokeTrainer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.imsouhay.Grove.Grove.LOGGER;

public class FeedBack {

    private Map<String, String> feedBacks = new LinkedHashMap<>();

    public FeedBack() {
        feedBacks.put("BalanceNotEnough", "§cYour balance is not enough!");
        feedBacks.put("TokensSent", "§6You have sent @value tokens to @receiver successfully");
        feedBacks.put("TokensReceived", "§6You have received @value tokens from @sender!");

        feedBacks.put("toMale", "You have set your @pokemon's Gender to Male for a price of @price.");
        feedBacks.put("pokemonAlreadyMale", "§cYour @pokemon is already Male!");

        feedBacks.put("toFemale", "You have set your @pokemon's Gender to Female for a price of @price.");
        feedBacks.put("pokemonAlreadyFemale", "§cYour @pokemon is already Female!");

        feedBacks.put("toShiny", "You have made your @pokemon Shiny for a price of @price.");
        feedBacks.put("pokemonAlreadyShiny", "§cYour @pokemon is already Shiny!");

        feedBacks.put("toNotShiny", "You have made your @pokemon Not-Shiny for a price of @price.");
        feedBacks.put("pokemonAlreadyNotShiny", "§cYour @pokemon is already Not-Shiny!");

        feedBacks.put("ability", "You have applied the @ability ability on your @pokemon for a price of @price.");
        feedBacks.put("pokemonAlreadyHasAbility", "§cYour @pokemon already has this ability!");

        feedBacks.put("levelEdit", "You have @operationd your @pokemon's level by @value for a price of @price.");
        feedBacks.put("levelLimitReached", "§cYour pokemon lvl is at @value! You can't @operation it anymore!");

        feedBacks.put("pokeball", "You have set your @pokemon's Caught Ball to @pokeball.");
        feedBacks.put("pokemonAlreadyCaughtWithBall", "§cYour @pokemon is already caught with this ball!");

        feedBacks.put("statEdit", "You have @operationd your @pokemon's @stat @vtype by @value for a price of @price.");
        feedBacks.put("statLimitReached", "§cYour @stat @vtype is at @value! You can't @operation it anymore!");

        feedBacks.put("skinEdit", "You have edited your @pokemon's texture for a price of @price.");
        feedBacks.put("pokemonAlreadyHasSkin", "§cYou can't apply a texture that you already have.");

        feedBacks.put("pokeBallEventNotCurrentlyAvailable", "§cThis PokeBall is not currently available.");
    }

    public Map<String, String> getFeedBacks() {
        return feedBacks;
    }

    public String get(String key) {
        return feedBacks.get(key);
    }


    public void init() {
        CompletableFuture<Boolean> futureRead = Utils.readFileAsync(PokeTrainer.POKE_TRAINER_PATH, "feedbacks.json",
                el -> {
                    Gson gson = Utils.newGson();
                    FeedBack feedBack = gson.fromJson(el, FeedBack.class);
                    feedBacks.putAll(feedBack.getFeedBacks());
                });

        if (!futureRead.join()) {
            LOGGER.info("No feedbacks.json file found for PokeTrainer. Attempting to generate one.");}
            Gson gson = Utils.newGson();
            String data = gson.toJson(this);
            CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(PokeTrainer.POKE_TRAINER_PATH, "feedbacks.json", data);

            if (!futureWrite.join()) {
                LOGGER.fatal("Could not write feedbacks.json for PokeTrainer.");

            return;
        }
        LOGGER.info("PokeTrainer feedbacks file read successfully.");
    }


}
