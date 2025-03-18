package org.imsouhay.Grove.config;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.permission.CobblemonPermission;
import com.cobblemon.mod.common.api.permission.PermissionLevel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import net.minecraft.server.level.ServerPlayer;
import org.imsouhay.Grove.Grove;
import org.imsouhay.Grove.util.Utils;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

/**
 * Class that deals with permission handling.
 */
public class Permissions {
	public static final Permissions INSTANCE = new Permissions();
	// Store all created permissions in a hashmap.
	private HashMap<String, CobblemonPermission> permissions;
	private HashMap<String, String> nameToNode; // name to node map
	@Expose
	private HashMap<String, Integer> readablePermissions;

	/**
	 * Constructor to create a permission object. Add your permissions here using the private method.
	 */
	public Permissions() {
		permissions = new HashMap<>();
		nameToNode=new HashMap<>();

		createPermission("HuntBase", "pokehunt.base", 0);
		createPermission("HuntReload", "pokehunt.reload", 4);
		createPermission("HuntRefresh", "pokehunt.refresh", 4);
		createPermission("HuntDebug", "pokehunt.debug", 4);


		createPermission("PokeDexBase", "pokedex.base", 0);
		createPermission("PokeDexCaught", "pokedex.caught", 0);
		createPermission("PokeDexNeeded", "pokedex.needed", 0);
		createPermission("PokeDexRewards", "pokedex.rewards", 0);


		createPermission("PokeTrainerBase", "poketrainer.base", 0);


		createPermission("EconomyBalance", "economy.balance", 0);
		createPermission("EconomyDeposit", "economy.deposit", 4);
		createPermission("EconomyWithdraw", "economy.withdraw", 4);
		createPermission("EconomyTransfer", "economy.transfer", 0);

		createPermission("Reload", "lavendermc.reload", 4);

		readablePermissions = new HashMap<>();
		
		for(String name:permissions.keySet()) {
			readablePermissions.put(name, permissions.get(name).getLevel().getNumericalValue());
		}
	}

	/**
	 * Method to add a new permission to the hashmap.
	 * @param name The reference of the permission.
	 * @param permissionLevel The permission level.
	 */
	private void createPermission(String name, String node,int permissionLevel) {
		nameToNode.put(name, node);
		permissions.put(name, new CobblemonPermission(node, parsePermission(permissionLevel)));
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

	public HashMap<String, Integer> getReadablePermissions() {
		return readablePermissions;
	}

	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync(Grove.BASE_PATH,
				"Permissions.json", el -> {
					Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().excludeFieldsWithoutExposeAnnotation().create();
					Permissions perm = gson.fromJson(el, Permissions.class);
					readablePermissions.putAll(perm.getReadablePermissions());
					translatePerms();
				});


		if (!futureRead.join())	Grove.LOGGER.info("No Permissions.json file found for " + Grove.MOD_ID +
					"'s general config. Attempting to generate one");
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().excludeFieldsWithoutExposeAnnotation().create();
		String data = gson.toJson(this);
		CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(Grove.BASE_PATH,
				"Permissions.json", data);

		if (!futureWrite.join()) {
			Grove.LOGGER.fatal("Could not write permissions file for " + Grove.MOD_ID + ".");
			return;
		}


		Grove.LOGGER.info(Grove.MOD_ID + "'s permission file read successfully");
	}
	
	public void translatePerms() {
		for(String name:readablePermissions.keySet()) {
			permissions.put(name, new CobblemonPermission(nameToNode.get(name), parsePermission(readablePermissions.get(name))));
		}
	}
}
