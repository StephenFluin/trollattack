/*
 * Created on Nov 1, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
package TrollAttack.Commands;

import TrollAttack.Being;
import TrollAttack.Player;

public class Ability extends Command {
    public Ability(String name, String failureMessage, String successMessage, String successBroadcast) {
        super(name);
        this.failureMessage = failureMessage;
        this.successMessage = successMessage;
        this.successBroadcast = successBroadcast;
        noBroadcast = false;
    }
    public Ability(String name) {
        this(name, "You fail to " + name + ".", "You succeed in " + name + "ing.", "%1 succeds in " + name + "ing.");
    }
    public Ability(String name, String message) {
        this.name = name;
        this.failureMessage = "You fail to " + message + ".";
        this.successMessage = "You successfully " + message + ".";
        noBroadcast = true;
    }
   
    private String failureMessage;
    private String successMessage;
    private String successBroadcast;
    boolean noBroadcast = true;
    
    
    public boolean canUse(Being being) {
        return false;
    }
    public boolean isSpell() {
        return false;
    }
}
