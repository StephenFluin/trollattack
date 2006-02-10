package TrollAttack.Commands;
import TrollAttack.Being;
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
public class Spell extends Ability {
	private int manaCost;
    private String successMessage;
    
	public Spell( String spellName, int cost) { 
	    this( spellName, cost, true);
    }
	/**
	 * Creates a new spell with the name spellName
	 * 
	 * @param spellName The name of the spell.
	 * @param c The cost in manaPoints of casting this spell
	 * @param b Is the command restricted to peaceful casting.
	 */
	public Spell( String spellName, int cost, boolean peacefulOnly) {
        super( spellName , "You fail to cast " + spellName + ".", peacefulOnly);
        this.successMessage = "You successfully cast " + spellName + ".";
	    this.manaCost = cost; 
    }
	public int getCost() {
	    return manaCost;
	}
    public boolean execute(Being player) {
        boolean success = super.execute(player);
        if(success) {
            player.decreaseManaPoints(getCost());
        }
        return success;
    }
    public boolean execute(Being player, String s) {
        boolean success = super.execute(player, s);
        if(success) {
            player.decreaseManaPoints(getCost());
        }
        return success;
    }
    public boolean isSpell() {
        return true;
    }
}
