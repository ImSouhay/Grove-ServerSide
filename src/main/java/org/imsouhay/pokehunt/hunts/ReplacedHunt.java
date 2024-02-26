package org.imsouhay.pokehunt.hunts;

public class ReplacedHunt {
	private final SingleHunt oldHunt;
	private final SingleHunt newHunt;

	public ReplacedHunt(SingleHunt oldHunt, SingleHunt newHunt) {
		this.oldHunt = oldHunt;
		this.newHunt = newHunt;
	}

	public SingleHunt getOldHunt() {
		return oldHunt;
	}

	public SingleHunt getNewHunt() {
		return newHunt;
	}
}
