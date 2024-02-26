package org.imsouhay.pokehunt.config;

/**
 * Properties to check a capture for.
 */
public class Properties {
	private final boolean ability; // Should the ability be checked
	private final boolean gender; // Should the gender be checked
	private final boolean nature; // Should the nature be checked
	private final boolean shiny; // Should the shiny state be checked

	public Properties() {
		ability = true;
		gender = true;
		nature = true;
		shiny = false;
	}

	/**
	 * Getters
	 */

	public boolean isAbility() {
		return ability;
	}

	public boolean isGender() {return gender;}

	public boolean isNature() {
		return nature;
	}

	public boolean isShiny() {
		return shiny;
	}
}
