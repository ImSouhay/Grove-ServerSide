LavenderMC-ServerSide
=====================
### A server-side Forge Mod made for Lavnder-MC, adds a **PokeDex** and a **PokeBuilder**.

# Config:
  ```
  Most of the Config files (config.json/lang.json) exist in: yourServerDirectory/config/lavenderserverside/pokedex or pokebuilder/
  ```


- `pokedex/config.json`:

 * `implementedOnly`:
  
  + can be either true or false, true meaning that the pokedex will only have implemented pokemons, and false is the opposite.
    
 * `rewards`:
  + this stores all of the rewards that will show up in the pokedex `/reward` menu and it has some stuff in it:
 * `progress`: the % of pokemons caught players should reach to claim this reward.
     
 * `slotNumber`: the slot where the reward will be at in the `/reward` menu, starting with 0 at the top left, to 8 at the top right, then 45 at the button left,and finally 53 at the button right
     
 * `itemMaterial`: the ID of the item that will show up in the `/reward` menu, for example: `minecraft:diamond_sword`.
     
 * `commands`: the commands that will get executed when the player claims the reward, the command should be without the `/` at the beginning and `@player` will get replaced by the name of the player who is claiming the reward


### Example:
```markdown
{
  "implementedOnly": true,
  "rewards": [
    {
      "progress": 10.0,
      "slotNumber": 10,
      "itemMaterial": "cobblemon:poke_ball",
      "commands": ["give @player minecraft:diamond 5",
		                 "give @player minecraft:diamond_sword"]
    },
    {
      "progress": 5.0,
      "slotNumber": 10,
      "itemMaterial": "minecraft:diamond",
      "commands": ["gamemode creative @player"]
    }]
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

