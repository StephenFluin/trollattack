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
	int cost;
	boolean peaceful = true;
	public Spell() {}
	public String toString() {
		return name;
	}
	public Spell(String spellName, int c) { this(spellName, c, true);}
	public Spell(String spellName, int c, boolean b) {name = spellName; peaceful = b;}
	public boolean isPeaceful() {
	    return peaceful;
	}
	public int getCost() {
	    return cost;
	}
	public void execute() {}
	public void execute(String s) {}
}
