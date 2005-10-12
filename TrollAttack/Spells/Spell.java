package TrollAttack.Spells;
import TrollAttack.Being;
import TrollAttack.Player;
/*
 * Created on May 29, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author PeEll
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class Spell {
	String name;
	
	private int cost;
	private double probability = .8;
	boolean peaceful = true;
	public String toString() {
		return name;
	}
	public Spell( String spellName, int c) { 
	    this( spellName, c, true);
    }
	/**
	 * Creates a new spell with the name spellName
	 * 
	 * @param spellName The name of the spell.
	 * @param c The cost in manaPoints of casting this spell
	 * @param b Is the command restricted to peaceful casting.
	 */
	public Spell( String spellName, int cost, boolean peacefulOnly) {
	    name = spellName; 
	    this.cost = cost; 
	    peaceful = peacefulOnly;
    }
	public boolean isPeaceful() {
	    return peaceful;
	}
	public int getCost() {
	    return cost;
	}
	public double getProbability() {
	    return probability;
	}
	public boolean run() {return false;}
	public boolean run(String s) {return false;}
	public boolean execute(Being player) {
	    if(Math.random() < this.getProbability()) {
	        return this.run();
	    } else {
	        player.tell("Your magic fails you.");
	        return false;
	    }
	}
	public boolean execute(Being player, String s) {
	    if(Math.random() < this.getProbability()) {
	        return this.run(s);
	    } else {
	        player.tell("Your magic fails you.");
	        return false;
	    }
	}
}
