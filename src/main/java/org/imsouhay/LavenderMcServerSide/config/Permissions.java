package org.imsouhay.LavenderMcServerSide.config;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.permission.CobblemonPermission;
import com.cobblemon.mod.common.api.permission.PermissionLevel;
import com.google.gson.Gson;
import net.minecraft.server.level.ServerPlayer;
import org.imsouhay.LavenderMcServerSide.LavenderMcServerSide;
import org.imsouhay.LavenderMcServerSide.util.Utils;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

/**
 * Class that deals with permission handling.
 */
public class Permissions {

	public static final Permissions INSTANCE = new Permissions();
	// Store all created permissions in a hashmap.
	private HashMap<String, CobblemonPermission> permissions;

	/**
	 * Constructor to create a permission object. Add your permissions here using the private method.
	 */
	public Permissions() {
		permissions = new HashMap<>();
		// Add you permissions here.
		createPermission("HuntBase", "pokehunt.base", 1);
		createPermission("HuntNotify", "pokehunt.notify", 1);
		createPermission("HuntReload", "pokehunt.reload", 2);
		createPermission("HuntRefresh", "pokehunt.refresh", 2);
		createPermission("HuntDebug", "pokehunt.debug", 2);


		createPermission("PokeDexBase", "pokedex.base", 1);
		createPermission("PokeDexCaught", "pokedex.caught", 1);
		createPermission("PokeDexNeeded", "pokedex.needed", 1);
		createPermission("PokeDexRewards", "pokedex.rewards", 1);


		createPermission("PokeTrainerBase", "poketrainer.base", 1);


		createPermission("EconomyBalance", "economy.balance", 3);
		createPermission("EconomyDeposit", "economy.deposit", 3);
		createPermission("EconomyWithdraw", "economy.withdraw", 3);


		createPermission("Reload", "lavendermc.reload", 3);
	}

	/**
	 * Method to add a new permission to the hashmap.
	 * @param name The reference of the permission.
	 * @param permissionNode The permission node.
	 * @param permissionLevel The permission level.
	 */
	private void createPermission(String name, String permissionNode, int permissionLevel) {
		permissions.put(name, new CobblemonPermission(permissionNode, parsePermission(permissionLevel)));
	}

	/**
	 * Method to fetch a permission from its reference.
	 * @param name The reference of the permission.
	 * @return
	 */
	public CobblemonPermission getPermission(String name) {
		return permissions.get(name);
	}

	public boolean hasPermission(ServerPlayer player, CobblemonPermission permission) {
		return Cobblemon.INSTANCE.getPermissionValidator().hasPermission(player, permission);
	}

	private PermissionLevel parsePermission(int permLevel) {
		for (PermissionLevel value : PermissionLevel.values()) {
			if (value.ordinal() == permLevel) {
				return value;
			}
		}
		return PermissionLevel.CHEAT_COMMANDS_AND_COMMAND_BLOCKS;
	}

	public HashMap<String, CobblemonPermission> getPermissions() {
		return permissions;
	}

	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync(LavenderMcServerSide.BASE_PATH,
				"Permissions.json", el -> {
					Gson gson = Utils.newGson();
					Permissions perm = gson.fromJson(el, Permissions.class);
					permissions=perm.getPermissions();
				});

		if (!futureRead.join()) {
			LavenderMcServerSide.LOGGER.info("No Permissions.json file found for " + LavenderMcServerSide.MOD_ID +
					"'s general config. Attempting to generate one");
			Gson gson = Utils.newGson();
			String data = gson.toJson(this);
			CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(LavenderMcServerSide.BASE_PATH,
					"Permissions.json", data);

			if (!futureWrite.join()) {
				LavenderMcServerSide.LOGGER.fatal("Could not write permissions file for " + LavenderMcServerSide.MOD_ID + ".");
			}
			return;
		}
		LavenderMcServerSide.LOGGER.info(LavenderMcServerSide.MOD_ID + "'s permission file read successfully");
	}
}
