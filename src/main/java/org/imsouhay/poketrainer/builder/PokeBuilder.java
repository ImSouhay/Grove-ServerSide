package org.imsouhay.poketrainer.builder;

import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.abilities.Ability;
import com.cobblemon.mod.common.api.moves.MoveSet;
import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokeball.PokeBall;
import com.cobblemon.mod.common.pokemon.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.imsouhay.poketrainer.util.vType;

import java.util.HashMap;
import java.util.Map;

import static org.imsouhay.LavenderMcServerSide.util.Utils.format;

public class PokeBuilder {

    private final Pokemon pokemon;
    private GooeyButton guiButton;
    private ServerPlayer owner;
    private PokeBuilderButton button;
    private String name;
    private ItemStack heldItem;
    private Ability ability;
    private Nature nature;
    private Gender gender;
    private String genderName;
    private int friendShip;
    private PokeBall caughtBall;
    private IVs ivs;
    private final Map<Stat, Integer> ivStats= new HashMap<>();
    private int IVsTotal=0;
    private int maxIVs;
    private EVs evs;
    private final Map<Stat, Integer> evStats= new HashMap<>();
    private int EVsTotal=0;
    private int maxEVs;
    private MoveSet moves;
    private int level;

    private boolean exists=true;

    public PokeBuilder(ServerPlayer player, int slot) {
        this.pokemon= Cobblemon.INSTANCE.getStorage().getParty(player).get(slot);
        if(pokemon==null) {
            exists=false;
            return;
        }

        this.owner= player;
        button=new PokeBuilderButton();

        this.name= format(pokemon.getSpecies().getName());
        this.heldItem= this.pokemon.heldItem();
        this.ability= this.pokemon.getAbility();
        this.nature= this.pokemon.getNature();
        this.gender= this.pokemon.getGender();
        this.friendShip= this.pokemon.getFriendship();
        this.caughtBall= this.pokemon.getCaughtBall();
        this.ivs= this.pokemon.getIvs();
        this.maxIVs=this.ivs.getAcceptableRange().getLast()*6;
        this.evs= this.pokemon.getEvs();
        this.maxEVs=this.evs.getAcceptableRange().getLast()*6;
        this.moves= this.pokemon.getMoveSet();
        this.level= this.pokemon.getLevel();

        switch (this.gender) {
            case MALE -> this.genderName="Male";
            case FEMALE -> this.genderName="Female";
            case GENDERLESS -> this.genderName="Genderless";
        }

        for(Stat stat:Stats.values()) {
            if(stat==Stats.EVASION || stat==Stats.ACCURACY) {continue;}

            ivStats.put(stat, this.ivs.get(stat));
        }

        for(Stat stat:Stats.values()) {
            if(stat==Stats.EVASION || stat==Stats.ACCURACY) {continue;}

            evStats.put(stat, this.evs.get(stat));
        }

        for(int num:ivStats.values()) {
            IVsTotal+=num;
        }

        for(int num:evStats.values()) {
            EVsTotal+=num;
        }

        button.build(this);

    }

    // Get Methods

    public String getName() {
        return this.name;
    }

    public ServerPlayer getOwner() {
        return owner;
    }

    public PokeBuilderButton getButton() {
        return button;
    }

    public Ability getAbility() {
        return this.ability;
    }

    public Gender getGender() {
        return this.gender;
    }

    public String getGenderName() {
        return this.genderName;
    }

    public int getFriendShip() {
        return this.friendShip;
    }

    public ItemStack getHeldItem() {
        return this.heldItem;
    }

    public IVs getIvs() {
        return this.ivs;
    }

    public int getMaxIVs() {
        return maxIVs;
    }

    public Map<Stat, Integer> getIvStats() {
        return this.ivStats;
    }

    public Nature getNature() {
        return this.nature;
    }

    public PokeBall getCaughtBall() {
        return this.caughtBall;
    }

    public Pokemon getPokemon() {
        return this.pokemon;
    }

    public EVs getEvs() {
        return this.evs;
    }

    public int getMaxEVs() {
        return maxEVs;
    }

    public Map<Stat, Integer> getEvStats() {
        return this.evStats;
    }

    public int getLevel() {
        return this.level;
    }

    public MoveSet getMoves() {
        return this.moves;
    }

    public int getIVsTotal() {
        return this.IVsTotal;
    }

    public int getEVsTotal() {
        return this.EVsTotal;
    }

    public GooeyButton getGuiButton() {
        return guiButton;
    }


    // Set Methods

    public void editV(vType type, Stat stat, int value) {
        switch (type) {
            case EV -> editEV(stat, value);
            case IV -> editIV(stat, value);
        }
        
    }
    public void editEV(Stat stat, int value) {
        int newValue;
        if(this.evs.get(stat)<-value) {
            newValue=0;
        } else {
            newValue = Math.min(this.evs.get(stat) + value, this.evs.getAcceptableRange().getLast());
        }
        this.evs.set(stat, newValue);
        this.evStats.replace(stat, newValue);
        this.pokemon.setEV(stat, newValue);
    }

    public void editIV(Stat stat, int value) {
        int newValue;
        if(this.ivs.get(stat)<-value) {
            newValue=0;
        } else {
            newValue = Math.min(this.ivs.get(stat) + value, this.ivs.getAcceptableRange().getLast());
        }
        this.ivs.set(stat, newValue);
        this.ivStats.replace(stat, newValue);
        this.pokemon.setIV(stat, newValue);
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
        this.pokemon.setAbility(this.ability);
        
    }

    public void setCaughtBall(PokeBall caughtBall) {
        this.caughtBall = caughtBall;
        this.pokemon.setCaughtBall(this.caughtBall);
        
    }

    public void setGender(Gender gender) {
        this.gender = gender;
        this.pokemon.setGender(this.gender);
        
    }

    public void setGender(String gender) {
        this.genderName= gender;

        switch (gender.toLowerCase()) {
            case "male":
                this.gender= Gender.MALE;
                break;
            case "female":
                this.gender= Gender.FEMALE;
                break;
            case "genderless":
                this.gender= Gender.GENDERLESS;
                break;
        };
        this.pokemon.setGender(this.gender);
        
    }

    public void setNature(Nature nature) {
        this.nature = nature;
        this.pokemon.setNature(this.nature);
        
    }

    public void setLevel(int level) {
        this.level = level;
        this.pokemon.setLevel(this.level);
        
    }

    public void editLevel(int levels) {
        int newValue;

        if(this.level+levels>100) {
            newValue=100;
        } else {
            newValue = Math.max(this.level + levels, 1);
        }

        this.level=newValue;
        this.pokemon.setLevel(this.level);
        
    }

    public void setGuiButton(GooeyButton guiButton) {
        this.guiButton = guiButton;
    }

    public boolean exists() {return exists;}

    public void reloadButton() {
        this.guiButton=PokeBuilderButton.build(this);
        this.guiButton.update();
    }


}
