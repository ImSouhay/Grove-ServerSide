package org.imsouhay.LavenderMcServerSide.util;

import ca.landonjw.gooeylibs2.api.button.ButtonAction;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.pokemon.feature.ChoiceSpeciesFeatureProvider;
import com.cobblemon.mod.common.api.pokemon.feature.SpeciesFeatures;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.imsouhay.LavenderMcServerSide.LavenderMcServerSide;
import org.imsouhay.LavenderMcServerSide.exception.InvalidItemIdException;
import org.imsouhay.pokedex.PokeDex;
import org.imsouhay.pokedex.account.Account;
import org.imsouhay.poketrainer.PokeTrainer;

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
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

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
			LavenderMcServerSide.LOGGER.fatal("Unable to write file asynchronously, attempting sync write.");
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
			LavenderMcServerSide.LOGGER.fatal("Unable to write to file for " + LavenderMcServerSide.MOD_ID + ".\nStack Trace: ");
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

			String data = "";

			while (reader.hasNextLine()) {
				data += reader.nextLine();
			}
			reader.close();
			callback.accept(data);
			return true;
		} catch (Exception e) {
			LavenderMcServerSide.LOGGER.fatal("Unable to read file " + file.getName() + " for "
					+ LavenderMcServerSide.MOD_ID + ".\nStack Trace: ");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Method to check if a directory exists. If it doesn't, create it.
	 * @param path The directory to check.
	 * @return the directory as a File.
	 */
	public static File checkForDirectory(String path) {
		File dir = new File(new File("").getAbsolutePath() + path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	/**
	 * Method to create a new gson builder.
	 * @return Gson instance.
	 */
	public static Gson newGson() {
		return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	}

	/**
	 * Parses item ID string to an item stack
	 * @param id The id to parse to
	 * @return ItemStack
	 */
	public static ItemStack parseItemId(String id) {
		Item item= ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
		if(item==null){
			throw new InvalidItemIdException(id);
		}
		return new ItemStack(item);
	}

	public static String formatCommand(String command, String playerName) {
		return command.replaceFirst("@player", playerName);
	}

	public static double getDexProgress(Account account) {

		if (PokeDex.config.isImplementedOnly()) {
			return (double) account.getCaught().size() / PokemonSpecies.INSTANCE.getImplemented().size() * 100;
		} else {
			return (double) account.getCaught().size() / PokemonSpecies.INSTANCE.getSpecies().size() * 100;
		}
	}

	public static List<ChoiceSpeciesFeatureProvider> getFeatureFromPokemon(Pokemon pokemon) {
		List<ChoiceSpeciesFeatureProvider> pokemonFeatures = new ArrayList<>();

		SpeciesFeatures.INSTANCE.getFeaturesFor(pokemon.getSpecies()).forEach(e -> {
					if(e instanceof ChoiceSpeciesFeatureProvider feature) pokemonFeatures.add(feature);
				}
		);

		return pokemonFeatures;
	}

	public static String format(String unFormatted) {
		if(unFormatted.equalsIgnoreCase("hp")) return "HP";

		StringBuilder output= new StringBuilder();

		String[] words=unFormatted
				.replace("{{choice}}", "")
				.replace("-", " ")
				.replace("_", " ")
				.replace("]", "")
				.replace("[", "")
				.split(" ");

		for(String word:words) {
			output.append(String.valueOf(word.charAt(0)).toUpperCase());
			output.append(word.substring(1));
			output.append(word.equals(words[words.length - 1]) ? "":" ");
		}

		return output.toString();
	}

	public static int getPercentage(int value, int maxValue) {
		return (value/maxValue)*100;
	}

	public static void balanceNotEnough(ButtonAction e) {
		e.getPlayer().sendSystemMessage(Component.nullToEmpty(PokeTrainer.fBack.get("BalanceNotEnough")));
	}

	public static void sendFeedBack(ServerPlayer player, String key) {
		sendFeedBack(player, key, "", "", "", "", "", 0, "", "", "", "");
	}
	public static void sendFeedBack(ServerPlayer player, String key, String pokemonName) {
		sendFeedBack(player, key, pokemonName, "", "", "", "", 0, "", "", "", "");
	}
	public static void sendFeedBack(ServerPlayer player, String key, String pokemonName,
									String price) {
		sendFeedBack(player, key, pokemonName, price, "", "", "", 0, "", "", "", "");
	}

	public static void sendFeedBack(ServerPlayer player, String key, String price,
									String pokemonName, String operation, int value) {
		sendFeedBack(player, key, pokemonName, price, "", "", operation, value, "", "", "", "");
	}

	public static void sendFeedBack(ServerPlayer player, String key, String price, String pokemonName,
									String something/* this can be pokeball OR natureName OR abilityName OR skin*/) {
		sendFeedBack(player, key, pokemonName, price, something, something, "", 0, something, something, "", "");
	}

	public static void sendFeedBack(ServerPlayer player, String key, String price, String pokemonName,
									String operation, String stat, String vType, int value) {
		sendFeedBack(player, key, pokemonName, price, "", "", operation, value, "", "", stat, vType);
	}

	public static void sendFeedBack(ServerPlayer player, String key, String pokemonName,
									String price, String abilityName, String natureName, String operation,
									int value, String pokeBall, String skin, String stat, String vType) {
		player.sendSystemMessage(Component.nullToEmpty(PokeTrainer.fBack.get(key)
				.replace("@pokemon", pokemonName)
				.replace("@price", price)
				.replace("@ability", abilityName)
				.replace("@nature", natureName)
				.replace("@operation", operation)
				.replace("@value", String.valueOf(value))
				.replace("@pokeball", pokeBall)
				.replace("@skin", skin)
				.replace("@stat", stat)
				.replace("@vtype", vType)
		));
	}

	public static String price(int value) {
		return "§6Price: §e"+value+" Tokens";
	}

}
