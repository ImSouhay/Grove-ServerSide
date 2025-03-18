Grove-ServerSide
=====================
### A server-side Forge Mod made for Grove-MC, adds a **PokeDex** and a **PokeTrainer** and a **PokeHunt**.

This is not the full documentation. This mod is no longer being updated, and I am no longer affiliated with The Grove.

You are free to use this mod on your server and modify parts of it to better fit your needs. Just don't claim full ownership, and if possible, give me a mention—I love seeing my work in action! 😄

<p align="center">
  <a href="https://modrinth.com/mod/gooeylibs" target="_blank">
    <img alt="requirement" src="https://raw.githubusercontent.com/ImSouhay/hub/main/GooeyLibsRequiredIcon.svg" />
  </a>

If anything is not working, try placing the config files somewhere, and run the mod so new files with the new features are made, and then you can copy your old config back, without deleting anything that have been added

## Config:

Most configuration files are located in: `yourServerDirectory/config/lavenderserverside/pokedex` & `poketrainer`.

### `GeneralConfig.json`:
- This file has two simple things, `pokeDexIsEnabled` and `pokeTrainerIsEnabled`, true meaning that its enabled, and false for disabled (if you switch to false while the server is running you will have to restart the server for it to take effect)

### `economy/balances/*.json`:
- This is where players' balances are saved, you can edit it manually from here if you want.

### `pokedex/config.json`:

- `implementedOnly`:
  - Set to either true or false. If true, the PokeDex will only include implemented Pokémon; false includes all.

- `isDexLimitEnabled`: True to activate, false to disactivate it.
- `dexLimit`: The max number of pokemons that show up in the pokedex, caught and needed menus.
  
- `rewardClaimable`: The text showed when a reward can be claimed.
- `rewardAlreadyClaimed`: The text showed when a reward have been already been claimed.
- `claimingMessage`: The message players get when they claim a reward.
- `percentageNeededToClaim`: The text showed informing players what is the percentage needed to claim a reward.
- `currentProgress`: The text showed informing players about their current progress
  
- `rewards`:
  - Contains rewards displayed in the PokeDex `/reward` menu with the following attributes:
    - `progress`: Percentage of Pokémon caught players should reach to claim the reward.
    - `slotNumber`: Slot in the `/reward` menu, ranging from 0 at the top left to 8 at the top right, then 45 at the bottom left, and finally 53 at the bottom right.
    - `itemMaterial`: ID of the item shown in the `/reward` menu (e.g., `minecraft:diamond_sword`).
    - `commands`: Commands executed when the player claims the reward. Use `@player` to represent the player's name.
    - `lore`: Extra lore, for example (`§f+5 §cRedStone!`), the `@progress` gets replaced by the relevant progress value in each case.

#### Example:

```json
{
  "implementedOnly": true,

  "isDexLimitEnabled": false,
  "dexLimit": 500,

  "rewardClaimable": "§aYou can claim this reward!",
  "rewardAlreadyClaimed": "§bYou have already claimed this reward",
  "claimingMessage": "§c[Pokedex] §2You successfully redeemed the @progress% dex rewards.",
  "percentageNeededToClaim": "§cYou need to reach @progress% to claim this reward",
  "currentProgress": "§6Current progress: @progress%",

  "rewards": [
    {
      "progress": 10.0,
      "slotNumber": 10,
      "itemMaterial": "cobblemon:poke_ball",
      "commands": ["give @player minecraft:diamond 5", "give @player minecraft:diamond_sword"]
      "lore": ["§4you get very cool diamond sword",
               ""]
    },
    {
      "progress": 5.0,
      "slotNumber": 10,
      "itemMaterial": "minecraft:diamond",
      "commands": ["gamemode creative @player"]
      "lore": []
    }
  ]
}
```

### `pokedex/lang.json`:
- `title`: The title of the Pokedex menu.
- `fillerMaterial`: The filler material in the Menus

### `pokedex/accounts/*.json`:
- Stores info about player's progression, claimed rewards and available rewards.


### `poketrainer/config.json`:
- `feedbackIsEnabled`:
  * If enabled, players will get chat messages with info when using the poketrainer.

- `defaultBalance`:
  * The Balance players will get when first time joining the server.

- `prices`:
  * The prices of the PokeTrainer stuff, you can only change the number

### `poketrainer/lang.json`:
- `mainMenuFillerMaterial`:
  * The filler material in the Main Menu (where you can chose which pokemon to edit).
- `homeMenuFillerMaterial`:
  * The filler material in the Home Menu (where you can chose what to edit).
- `fillerMaterial`:
  * The filler material in any other pages in the poketrainer.
- `mainMenuTitle`:
  * The Title of the Main Menu (where you can chose which pokemon to edit).
- `homeMenuTitle`:
  * The title of the Home Menu (where you can chose what to edit).

### `poketrainer/feedback.json`:
- Stores the feedbacks players get in the poketrainer, You are only allowed to change the feedback, changing the identifier will cause the poketrainer to stop working.
- keywords: words that get replaced by other specefic things
  - `@pokemon`: pokemon name.
  - `@price`: edit price.
  - `@ability`: ability name.
  - `@nature`: nature name.
  - `@operation`: increase or decrease.
  - `@value`: a number, different in each case. (for example, in `levelEdit` it gets replaced by the number of levels)
  - `@stat`: Attack or Defense or Speed or Health or Special Attack or Special Defense.
  - `@vtype`: EV or IV.


# Commands:

## **Common Commands:**

- `/LavenderReload`:

  * Reloads the Config files so you dont have to restart the whole server for the changes to apply. 
  
  This is supposed to be an admin command, players using it is not really a problem, but u should only let admins access it.


## **PokeDex Commands:**

- `/pokedex` or `/dex`:

  * Opens the PokeDex menu and shows all existing pokemons. (configurable from [here](#pokedexconfigjson) and [here](#pokedexlangjson))
  

- `/needed`:

  * Opens the PokeDex menu but only shows pokemons you didn't catch yet.

- `/caught`:
  * Opens the PokeDex menu but only shows pokemons you catched.

- `/reward`:
  * Opens a menu where you can claim rewards based on the % of pokemons catched. (configurable from [here](#pokedexconfigjson), [example](#example))

## **PokeTrainer Commands:**

- `/poketrainer`:
  * Opens the PokeTrainer's main menu (configurable from [here](#poketrainerconfigjson) and [here](#poketrainerlangjson)).
 
- `/PokeTokensBalance <PlayerName>`:
  * Returns the balance of the player.
 
- `/PokeTokensDeposit <PlayerName> (value)`:
  * Deposits the specified value into the player's balance
 
- `/PokeTokensWithdraw <PlayerName> (value)`:
  * Withdraws the specified value from the player's balance
 
note : balance and economy related commands are made for admins, they generate tokens from nothing.
