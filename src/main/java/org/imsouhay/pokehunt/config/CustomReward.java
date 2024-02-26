package org.imsouhay.pokehunt.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom prices
 */
public class CustomReward {
	private final String species; // Species of the pokemon
	private final String form; // Form of the pokemon
	private final ArrayList<String> commands; // price of hunt.

	public CustomReward() {
		species = "magikarp";
		form = "";
		commands = new ArrayList<>();
	}

	/**
	 * Getters
	 */

	public String getSpecies() {
		return species;
	}

	public String getForm() {
		return form;
	}

	public ArrayList<String> getCommand() {
		return commands;
	}
}
