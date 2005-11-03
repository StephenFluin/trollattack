/*
 * Created on Nov 2, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
package TrollAttack.Classes;

public class AbilityData {
    public double maxProficiency = 0;
    public int level = 0;
    public double proficiency = 0;
    public AbilityData(int level, double maxProficiency ) {
        this.maxProficiency = maxProficiency;
        this.level = level;
    }
    public AbilityData(double proficiency) {
        this.proficiency = proficiency;
    }
    
    public void increaseProficiency(double increase) {
        this.proficiency += increase;
    }
}