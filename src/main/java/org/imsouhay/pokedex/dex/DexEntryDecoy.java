package org.imsouhay.pokedex.dex;


/*
    There once was a genius dev who made a minecraft mod, and it was using dex number as
    a universal identifier in all of its features, but later, stuff changed, and multiple pokemons
    started having the same dex number, so the genius dev fixed all the features and started using
    the pokemons names as identifiers, however, some users still had the config files using
    the dex number, so the genius dev had an idea, make a decoy class, that uses the dex number
    read the configs, and then transform it into the new gud class.
    I am the genius dev :-D
 */
public class DexEntryDecoy {
    private final Integer dexNumber;
    private final boolean isCaught;

    public DexEntryDecoy(Integer num, boolean caught) {
        dexNumber = num;
        isCaught = caught;
    }

    public Integer getDexNumber() {
        return dexNumber;
    }

    public boolean isCaught() {
        return isCaught;
    }
}
