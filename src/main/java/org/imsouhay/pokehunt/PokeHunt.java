package org.imsouhay.pokehunt;

import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;
import org.imsouhay.Grove.Grove;
import org.imsouhay.pokehunt.command.basecommand.HuntCommand;
import org.imsouhay.pokehunt.config.Config;
import org.imsouhay.pokehunt.config.Lang;
import org.imsouhay.pokehunt.config.Logs;
import org.imsouhay.pokehunt.event.EventHandler;
import org.imsouhay.pokehunt.hunts.CurrentHunts;
import org.imsouhay.pokehunt.hunts.HuntManager;
import org.imsouhay.pokehunt.hunts.SpawnRates;
import org.imsouhay.pokehunt.util.CommandsRegistry;

public class PokeHunt
{
	public static final String MOD_ID = "hunt";
	public static final String POKEHUNT_PATH= Grove.BASE_PATH+"pokehunt/";

	public static final Logger LOGGER = Grove.LOGGER;
	public static Config config = new Config();
	public static SpawnRates spawnRates = new SpawnRates();
	public static CurrentHunts hunts = new CurrentHunts(null);
	public static HuntManager manager = new HuntManager();
	public static Lang language = new Lang();
	public static final Logs logs = new Logs();
	public static MinecraftServer server;

	public static void init() {
		// Adds command to registry
		CommandsRegistry.addCommand(new HuntCommand());
		EventHandler.registerEvents();
	}

	/**
	 * Initializes stuff.
	 */
	public static void load() {
		config.init();
		spawnRates.init();
		if (!config.isIndividualHunts()) {
			hunts.clearCounter();
			hunts.init();
		}
		language.init();
		logs.init();
		manager.init();
	}
}
