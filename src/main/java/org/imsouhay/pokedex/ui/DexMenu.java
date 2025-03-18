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
import com.cobblemon.mod.common.item.PokemonItem;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.imsouhay.Grove.util.Utils;
import org.imsouhay.pokedex.PokeDex;
import org.imsouhay.pokedex.account.AccountProvider;
import org.imsouhay.pokedex.collection.ImplementedMonsCollection;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class DexMenu {

	public static Page getPage(UUID player, List<Species> speciesList) {

		List<Species> implemented = ImplementedMonsCollection.getList();

		ArrayList<Button> buttons = new ArrayList<>();


		for (Species dexSpecies : speciesList) {
			if(PokeDex.config.isDexLimitEnabled() && dexSpecies.getNationalPokedexNumber() > PokeDex.config.getDexLimit()) {
				break;
			}
			if(dexSpecies==null) continue;

			Pokemon mon = new Pokemon();
			mon.setSpecies(dexSpecies);
			Collection<String> lore = new ArrayList<>();
			boolean isCaught = AccountProvider.getAccount(player).getPokemon(dexSpecies.getName()).isCaught();
			
			if(PokeDex.config.isImplementedOnly()) {
				lore.add(isCaught ? "§aCaught" : "§cNot Caught");
			} else if (!implemented.contains(dexSpecies)) {
					lore.add("§4Not Implemented Currently.");
				} else {
					lore.add(isCaught ? "§aCaught" : "§cNot Caught");
			}

			// Adds button to hashmap.
			buttons.add(GooeyButton.builder()
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
		if(PokeDex.config.isDexSearchEnabled()) template.set(49, SearchButton.getButton(player));

		LinkedPage page = PaginationHelper.createPagesFromPlaceholders(template, buttons, null);
		String text = " - " +
                BigDecimal.valueOf(Utils.getDexProgress(AccountProvider.getAccount(player)))
						.setScale(2, RoundingMode.HALF_EVEN).floatValue() + "%";
		page.setTitle(PokeDex.lang.getTitle() + text);

		setPageTitle(page, text);
		return page;
	}

	private static void setPageTitle(LinkedPage page, String string) {
		LinkedPage next = page.getNext();
		if (next != null) {
			next.setTitle(PokeDex.lang.getTitle() + string);
			setPageTitle(next, string);
		}
	}

}
