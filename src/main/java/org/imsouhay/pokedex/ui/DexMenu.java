package org.imsouhay.pokedex.ui;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.button.linked.LinkType;
import ca.landonjw.gooeylibs2.api.button.linked.LinkedPageButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.LinkedPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.item.PokemonItem;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.imsouhay.LavenderMcServerSide.LavenderMcServerSide;
import org.imsouhay.LavenderMcServerSide.util.Utils;
import org.imsouhay.pokedex.PokeDex;
import org.imsouhay.pokedex.account.AccountProvider;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class DexMenu {

	public Page getPage(UUID player, Collection<Species> speciesList) {

		ArrayList<Species> implemented = new ArrayList<>(PokemonSpecies.INSTANCE.getImplemented());



		Comparator<Species> comparator = Comparator.comparingInt(Species::getNationalPokedexNumber);

		List<Species> orderedList= new ArrayList<>(speciesList);

		orderedList.sort(comparator);

		ArrayList<Button> sortedButtons = new ArrayList<>();
		int counter=0;
		// For each species, make a button.
		for (Species dexSpecies : orderedList) {
			if(PokeDex.config.isDexLimitEnabled() && counter> PokeDex.config.getDexLimit()) {
				break;
			}
			counter++;

			// Creates the button
			Pokemon mon = new Pokemon();
			mon.setSpecies(dexSpecies);
			Collection<String> lore = new ArrayList<>();
			boolean isCaught;
			try {
				isCaught =
						AccountProvider.getAccount(player).getPokemon(dexSpecies.getNationalPokedexNumber()).isCaught();
			} catch (NullPointerException e) {
				continue;
			}
			if (!implemented.contains(dexSpecies)) {
				lore.add("§4Not Implemented Currently.");
			} else {
				lore.add(isCaught ? "§aCaught" : "§cNot Caught");
			}

			// Adds button to hashmap.
			sortedButtons.add(GooeyButton.builder()
					.display(PokemonItem.from(mon))
					.title("§8" + dexSpecies.getNationalPokedexNumber() + ": §3" + dexSpecies.getName())
					.lore(lore)
					.build()
			);
		}

		PlaceholderButton placeholderButton = new PlaceholderButton();

		LinkedPageButton nextPage = LinkedPageButton.builder()
				.display(new ItemStack(Items.ARROW))
				.title("§7Next Page")
				.linkType(LinkType.Next)
				.build();

		LinkedPageButton previousPage = LinkedPageButton.builder()
				.display(new ItemStack(Items.ARROW))
				.title("§7Previous Page")
				.linkType(LinkType.Previous)
				.build();

		Button filler = GooeyButton.builder()
				.display(Utils.parseItemId(PokeDex.lang.getFillerMaterial()))
				.hideFlags(FlagType.All)
				.lore(new ArrayList<>())
				.title("")
				.build();

		ChestTemplate template = ChestTemplate.builder(6)
				.rectangle(0, 0, 5, 9, placeholderButton)
				.fill(filler)
				.set(45, previousPage)
				.set(53, nextPage)
				.build();

		LinkedPage page = PaginationHelper.createPagesFromPlaceholders(template, sortedButtons, null);
		String text = " - " +
                BigDecimal.valueOf(Utils.getDexProgress(AccountProvider.getAccount(player)))
						.setScale(2, RoundingMode.HALF_EVEN).floatValue() + "%";
		page.setTitle(PokeDex.lang.getTitle() + text);

		setPageTitle(page, text);
		return page;
	}

	private void setPageTitle(LinkedPage page, String string) {
		LinkedPage next = page.getNext();
		if (next != null) {
			next.setTitle(PokeDex.lang.getTitle() + string);
			setPageTitle(next, string);
		}
	}

}
