package org.imsouhay.pokedex.dex;

public class DexEntry {
	private final String pokeName;
	private final boolean isCaught;

	public DexEntry(String name, boolean caught) {
		pokeName = name;
		isCaught = caught;
	}

	public String getPokeName() {
		return pokeName;
	}

	public boolean isCaught() {
		return isCaught;
	}
}
