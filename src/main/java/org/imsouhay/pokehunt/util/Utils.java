package org.imsouhay.pokehunt.util;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.imsouhay.pokehunt.PokeHunt;
import org.imsouhay.pokehunt.hunts.SingleHunt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * Abstract class that contains some utility methods.
 */
public abstract class Utils {
	/**
	 * Method to write some data to file.
	 * @param filePath the directory to write the file to
	 * @param filename the name of the file
	 * @param data the data to write to file
	 * @return CompletableFuture if writing to file was successful
	 */
	public static CompletableFuture<Boolean> writeFileAsync(String filePath, String filename, String data) {
		CompletableFuture<Boolean> future = new CompletableFuture<>();

		Path path = Paths.get(new File("").getAbsolutePath() + filePath, filename);
		File file = path.toFile();

		// If the path doesn't exist, create it.
		if (!Files.exists(path.getParent())) {
			file.getParentFile().mkdirs();
		}

		// Write the data to file.
		try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(
				path,
				StandardOpenOption.WRITE,
				StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING
		)) {
			ByteBuffer buffer = ByteBuffer.wrap(data.getBytes(StandardCharsets.UTF_8));

			fileChannel.write(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
				@Override
				public void completed(Integer result, ByteBuffer attachment) {
					attachment.clear();
					try {
						fileChannel.close();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					future.complete(true);
				}

				@Override
				public void failed(Throwable exc, ByteBuffer attachment) {
					future.complete(writeFileSync(file, data));
				}
			});
		} catch (IOException | SecurityException e) {
			PokeHunt.LOGGER.fatal("Unable to write file asynchronously, attempting sync write.");
			future.complete(future.complete(false));
		}

		return future;
	}

	/**
	 * Method to write a file sync.
	 * @param file the location to write.
	 * @param data the data to write.
	 * @return true if the write was successful.
	 */
	public static boolean writeFileSync(File file, String data) {
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(data);
			writer.close();
			return true;
		} catch (Exception e) {
			PokeHunt.LOGGER.fatal("Unable to write to file for " + PokeHunt.MOD_ID + ".\nStack Trace: ");
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * Method to read a file asynchronously
	 * @param filePath the path of the directory to find the file at
	 * @param filename the name of the file
	 * @param callback a callback to deal with the data read
	 * @return true if the file was read successfully
	 */
	public static CompletableFuture<Boolean> readFileAsync(String filePath, String filename,
	                                                       Consumer<String> callback) {
		CompletableFuture<Boolean> future = new CompletableFuture<>();
		ExecutorService executor = Executors.newSingleThreadExecutor();

		Path path = Paths.get(new File("").getAbsolutePath() + filePath, filename);
		File file = path.toFile();

		if (!file.exists()) {
			future.complete(false);
			executor.shutdown();
			return future;
		}

		try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ)) {
			ByteBuffer buffer = ByteBuffer.allocate((int) fileChannel.size()); // Allocate buffer for the entire file

			Future<Integer> readResult = fileChannel.read(buffer, 0);
			readResult.get(); // Wait for the read operation to complete
			buffer.flip();

			byte[] bytes = new byte[buffer.remaining()];
			buffer.get(bytes);
			String fileContent = new String(bytes, StandardCharsets.UTF_8);

			callback.accept(fileContent);

			fileChannel.close();
			executor.shutdown();
			future.complete(true);
		} catch (Exception e) {
			future.complete(readFileSync(file, callback));
			executor.shutdown();
		}

		return future;
	}

	/**
	 * Method to read files sync.
	 * @param file The file to read
	 * @param callback what to do with the read data.
	 * @return true if the file could be read successfully.
	 */
	public static boolean readFileSync(File file, Consumer<String> callback) {
		try {
			Scanner reader = new Scanner(file);

			StringBuilder data = new StringBuilder();

			while (reader.hasNextLine()) {
				data.append(reader.nextLine());
			}
			reader.close();
			callback.accept(data.toString());
			return true;
		} catch (Exception e) {
			PokeHunt.LOGGER.fatal("Unable to read file " + file.getName() + " for " + PokeHunt.MOD_ID + ".\nStack Trace: ");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Method to create a new gson builder.
	 * @return Gson instance.
	 */
	public static Gson newGson() {
		return new GsonBuilder().setPrettyPrinting().create();
	}

	/**
	 * Formats a message by removing minecraft formatting codes if sending to console.
	 * @param message The message to format.
	 * @param isPlayer If the sender is a player or console.
	 * @return String that is the formatted message.
	 */
	public static String formatMessage(String message, Boolean isPlayer) {
		if (isPlayer) {
			return message.trim();
		} else {
			return message.replaceAll("§[0-9a-fk-or]", "").trim();
		}
	}

	public static String parseLongDate(long time) {
		// 1000 ms in 1 s
		// 60s in 1 m
		// 60m in 1 h
		// 24h in 1 d
		long second = 1000;
		long minute = second * 60;
		long hour = minute * 60;
		long day = hour * 24;

		long timeLeft = time;
		String output = "";

		if (timeLeft > day) {
			output += (time - (time % day)) / day + "d ";
			timeLeft = timeLeft % day;
		}

		if (timeLeft > hour) {
			output += (timeLeft - (timeLeft % hour)) / hour + "h ";
			timeLeft = timeLeft % hour;
		}

		if (timeLeft > minute) {
			output += (timeLeft - (timeLeft % minute)) / minute + "m ";
			timeLeft = timeLeft % minute;
		}

		if (timeLeft > second) {
			output += (timeLeft - (timeLeft % second)) / second + "s ";
			timeLeft = timeLeft % second;
		}

		return output;
	}

	public static String capitaliseFirst(String message) {

		if (message.contains("[") || message.contains("]")) {
			return message.replaceAll("\\[|\\]", "");
		}

		if (message.contains("_")) {
			String[] messages = message.split("_");
			StringBuilder output = new StringBuilder();
			for (String msg : messages) {
				output.append(capitaliseFirst(msg));
			}
			return output.toString();
		}

		return message.substring(0, 1).toUpperCase() + message.substring(1).toLowerCase();
	}

	public static String formatPlaceholders(String message, ServerPlayer player, Pokemon pokemon) {
		String newMessage = message;
		if (message == null) {
			return "";
		}

		if (player != null) {
			newMessage = newMessage.replaceAll("\\{player\\}", player.getName().getString());
		}

		if (pokemon != null) {
			newMessage = newMessage.replaceAll("\\{pokemon\\}", pokemon.getSpecies().getName());
		}

		return  newMessage;
	}

	public static ItemStack parseItemId(String id) {
		CompoundTag tag = new CompoundTag();
		tag.putString("id", id);
		tag.putInt("Count", 1);
		return ItemStack.of(tag);
	}

	public static void broadcastMessage(String message) {
		MinecraftServer server = PokeHunt.server;
		ArrayList<ServerPlayer> players = new ArrayList<>(server.getPlayerList().getPlayers());

		for (ServerPlayer pl : players) {
			pl.sendSystemMessage(Component.literal(message));
		}
	}

	public static void removeAllHunts() {

		if (PokeHunt.config.isIndividualHunts()) {
			for (UUID player : PokeHunt.manager.getPlayers()) {
				ArrayList<SingleHunt> copy = new ArrayList<>(PokeHunt.manager.getPlayerHunts(player).getHunts().values());
				for (SingleHunt hunt : copy) {
					if (hunt != null) {
						hunt.getTimer().cancel();
					}
				}
			}
		} else {
			ArrayList<SingleHunt> copy = new ArrayList<>(PokeHunt.hunts.getHunts().values());
			for (SingleHunt hunt : copy) {
				if (hunt != null) {
					hunt.getTimer().cancel();
				}
			}
		}
	}

	public static void runCommands(ArrayList<String> commands, ServerPlayer player, Pokemon pokemon) {
		// Run commands
		CommandDispatcher<CommandSourceStack> dispatcher =
				PokeHunt.server.getCommands().getDispatcher();
		for (String command : commands) {
			try {
				dispatcher.execute(
						Utils.formatPlaceholders(command, player, pokemon),
						PokeHunt.server.createCommandSourceStack());
			} catch (CommandSyntaxException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	public static String getStringRarityFromPokemon(Pokemon pokemon) {
		String rarity=PokeHunt.spawnRates.getRarity(pokemon);

		return getStringRarity(rarity);
	}

	public static String getStringRarity(String rarity) {
		if(rarity==null) return null;
		return switch (rarity) {
			case "common" -> "Common";
			case "uncommon" -> "Uncommon";
			case "rare" -> "Rare";
			default -> "UltraRare";
		};
	}

}
