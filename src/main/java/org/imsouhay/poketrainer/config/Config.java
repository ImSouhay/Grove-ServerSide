package org.imsouhay.poketrainer.config;


import com.cobblemon.mod.common.api.pokeball.PokeBalls;
import com.cobblemon.mod.common.pokeball.PokeBall;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.Gson;
import org.imsouhay.Grove.util.Utils;
import org.imsouhay.poketrainer.PokeTrainer;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.imsouhay.Grove.Grove.LOGGER;

public class Config {

    public boolean feedbackEnabled;
    public int defaultBalance;
    public Map<String, Integer> prices;
    public List<String> blackListedPokeBalls;
    public Map<String, Boolean> pokeBallEvents;
    private HashMap<String, ArrayList<String>> pokemonsBlackList;
    private HashMap<String, Integer> sectionsPosition;


    public Config() {
        defaultBalance=0;
        feedbackEnabled=true;
        blackListedPokeBalls = new ArrayList<>();
        pokeBallEvents = new HashMap<>();
        sectionsPosition = new HashMap<>();
        prices = new LinkedHashMap<>();

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

        prices.put("+3_EV", 10);
        prices.put("+5_EV", 20);
        prices.put("+10_EV", 30);
        prices.put("+15_EV", 50);
        prices.put("+20_EV", 60);
        prices.put("+30_EV", 100);
        prices.put("+40_EV", 125);
        prices.put("+50_EV", 150);

        prices.put("-3_EV", 5);
        prices.put("-5_EV", 7);
        prices.put("-10_EV", 10);
        prices.put("-15_EV", 15);
        prices.put("-20_EV", 17);
        prices.put("-30_EV", 22);
        prices.put("-40_EV", 27);
        prices.put("-50_EV", 35);
        
        prices.put("+1_IV", 10);
        prices.put("+3_IV", 20);
        prices.put("+5_IV", 30);
        prices.put("+10_IV", 50);
        prices.put("+15_IV", 60);
        prices.put("+31_IV", 100);

        prices.put("-1_IV", 1);
        prices.put("-3_IV", 5);
        prices.put("-5_IV", 7);
        prices.put("-10_IV", 10);
        prices.put("-15_IV", 15);
        prices.put("-31_IV", 17);

        sectionsPosition.put("ability", 11);
        sectionsPosition.put("gender", 15);
        sectionsPosition.put("iv", 21);
        sectionsPosition.put("ev", 23);
        sectionsPosition.put("pokeball", 29);
        sectionsPosition.put("level", 33);
        sectionsPosition.put("shiny", 39);
        sectionsPosition.put("skin", 41);


        for(PokeBall ball:PokeBalls.INSTANCE.all()) {
            prices.put(ball.getName().getPath(), 200);
        }

        pokemonsBlackList = new HashMap<>();
        pokemonsBlackList.put("ability", new ArrayList<>());
        pokemonsBlackList.put("gender", new ArrayList<>());
        pokemonsBlackList.put("iv", new ArrayList<>());
        pokemonsBlackList.put("ev", new ArrayList<>());
        pokemonsBlackList.put("pokeball", new ArrayList<>());
        pokemonsBlackList.put("level", new ArrayList<>());
        pokemonsBlackList.put("shiny", new ArrayList<>());
        pokemonsBlackList.put("skin", new ArrayList<>());
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


    public HashMap<String, ArrayList<String>> getPokemonsBlackList() {
        return pokemonsBlackList;
    }

    public boolean isPokemonBlackListed(Pokemon pokemon, String section) {
        for (String name : pokemonsBlackList.get(section)) {
            if (name.equalsIgnoreCase(pokemon.getDisplayName().getString().trim())
                    || name.equalsIgnoreCase(pokemon.getSpecies().getName().trim())) return true;
        }
        return false;
    }

    public List<String> getBlackListedPokeBalls() {
        return blackListedPokeBalls;
    }

    public boolean isBlocked(PokeBall pokeBall) {
        return blackListedPokeBalls.contains(pokeBall.getName().getPath());
    }

    public Map<String, Boolean> getPokeBallEvents() {
        return pokeBallEvents;
    }

    public boolean hasEvent(PokeBall pokeBall) {
        return pokeBallEvents.containsKey(pokeBall.getName().getPath());
    }

    /* binary boolean type
     * 1 -> true
     * 0 -> false
     * -1 -> null
     */
    public int isEventActive(PokeBall pokeBall) {
        try {
            return pokeBallEvents.get(pokeBall.getName().getPath())? 1:0;
        } catch(NullPointerException e) {
            return -1;
        }
    }

    public HashMap<String, Integer> getSectionsPosition() {
        return sectionsPosition;
    }

    public int getSectionPos(String section) {
        try {
            return sectionsPosition.get(section);
        } catch(Exception e) {
            LOGGER.error(section+" section position can't be found in the poketrainer config file");
            return 0;
        }
    }

    public void init() {
        CompletableFuture<Boolean> futureRead = Utils.readFileAsync(PokeTrainer.POKE_TRAINER_PATH, "config.json",
                el -> {
                    Gson gson = Utils.newGson();
                    Config config = gson.fromJson(el, Config.class);
                    prices.putAll(config.getPrices());
                    pokemonsBlackList.putAll(config.getPokemonsBlackList());
                    defaultBalance=config.getDefaultBalance();
                    feedbackEnabled=config.isFeedbackEnabled();
                    blackListedPokeBalls =config.getBlackListedPokeBalls();
                    pokeBallEvents=config.getPokeBallEvents();
                    sectionsPosition=config.getSectionsPosition();
        });

        if (!futureRead.join()) {
            LOGGER.info("No config.json file found for PokeTrainer. Attempting to " +
                    "generate one.");}
            Gson gson = Utils.newGson();
            String data = gson.toJson(this);
            CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(PokeTrainer.POKE_TRAINER_PATH, "config.json", data);

            if (!futureWrite.join()) {
                LOGGER.fatal("Could not write config.json for PokeTrainer.");
            return;
        }
        LOGGER.info("PokeTrainer config file read successfully.");
    }
}
