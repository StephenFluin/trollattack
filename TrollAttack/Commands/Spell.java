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
    
    /**
     * Spells by default can only be cast when not fighting.
     * @param spellName
     * @param cost
     */
	public Spell( String spellName, int cost) { 
	    this( spellName, cost, 0);
    }
	/**
	 * Creates a new spell with the name spellName
	 * 
	 * @param spellName The name of the spell.
	 * @param cost The cost in manaPoints of casting this spell
	 * @param maxPos The max position this spell is restricted to.
	 */
	public Spell( String spellName, int cost, int maxPos) {
        super( spellName , "You fail to cast " + spellName + ".", maxPos);
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
