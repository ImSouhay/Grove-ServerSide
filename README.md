LavenderMC-ServerSide
=====================
### A server-side Forge Mod made for Lavender-MC, adds a **PokeDex** and a **PokeTrainer**.

<p align="center">
  <a href="https://modrinth.com/mod/gooeylibs" target="_blank">
    <img alt="requirement" src="https://raw.githubusercontent.com/ImSouhay/hub/main/GooeyLibsRequiredIcon.svg" />
  </a>


## Config:

Most configuration files are located in: `yourServerDirectory/config/lavenderserverside/pokedex` & `poketrainer`.

### `GeneralConfig.json`:
- This files has two simple things, `pokeDexIsEnabled` and `pokeTrainerIsEnabled`, true meaning that its enabled, and false for disabled (if you switch to false while the server is running you will have to restart the server for it to take effect)

### `economy/balances/*.json`:
- This is where players' balances are saved, you can edit it manually from here if you want.

### `pokedex/config.json`:

- `implementedOnly`:
  - Set to either true or false. If true, the PokeDex will only include implemented Pokémon; false includes all.

- `rewards`:
  - Contains rewards displayed in the PokeDex `/reward` menu with the following attributes:
    - `progress`: Percentage of Pokémon caught players should reach to claim the reward.
    - `slotNumber`: Slot in the `/reward` menu, ranging from 0 at the top left to 8 at the top right, then 45 at the bottom left, and finally 53 at the bottom right.
    - `itemMaterial`: ID of the item shown in the `/reward` menu (e.g., `minecraft:diamond_sword`).
    - `commands`: Commands executed when the player claims the reward. Use `@player` to represent the player's name.

#### Example:

```json
{
  "implementedOnly": true,
  "rewards": [
    {
      "progress": 10.0,
      "slotNumber": 10,
      "itemMaterial": "cobblemon:poke_ball",
      "commands": ["give @player minecraft:diamond 5", "give @player minecraft:diamond_sword"]
    },
    {
      "progress": 5.0,
      "slotNumber": 10,
      "itemMaterial": "minecraft:diamond",
      "commands": ["gamemode creative @player"]
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

# Commands:

## **Common Commands:**

- `/reload`:

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
