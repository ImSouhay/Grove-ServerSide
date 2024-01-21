LavenderMC-ServerSide
=====================
### A server-side Forge Mod made for Lavnder-MC, adds a **PokeDex** and a **PokeBuilder**.

## Config:

Most configuration files (`config.json` and `lang.json`) are located in: `yourServerDirectory/config/lavenderserverside/pokedex` or `pokebuilder/`.

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

# Commands:

## **Common Commands:**

- `/reload`:

  * Reloads the Config files so you dont have to restart the whole server for the changes to apply. 
  
  This is supposed to be an admin command, players using it is not really a problem, but u should only let admins access it.


## **PokeDex Commands:**

- `/pokedex` or `/dex`:

  * Opens the PokeDex menu and shows all existing pokemons. (configurable, see )
  

- `/needed`:

  * Opens the PokeDex menu but only shows pokemons you didn't catch yet.

- `/caught`:
  * Opens the PokeDex menu but only shows pokemons you catched.

- `/reward`:
  * Opens a menu where you can claim rewards based on the % of pokemons catched (configurable, see )

