/*
 * Created on Nov 1, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2007
 * to Stephen Fluin.
 */
package TrollAttack.Commands;

import TrollAttack.Being;

public class Ability extends Command {
    public Ability(String name, String failureMessage) {
        this(name, failureMessage, false);
    }
    public Ability(String name, String failureMessage, boolean peacefulOnly) {
        super(name,peacefulOnly);
        this.failureMessage = failureMessage;
        needsPlayer = true;
    }
    public Ability(String name, boolean peacefulOnly) {
        this(name, "You fail to " + name + ".", peacefulOnly);
    }
    public Ability(String name) {
        this(name, false);
    }
   
    private String failureMessage;
    
    
    public boolean canUse(Being being) {
        return false;
    }
    public boolean isSpell() {
        return false;
    }
    
    /**
     * Executes the ability.  Returns true if they randomly succeed, and the
     *  ability succeeded, or if it is a spell and they randomly failed.
     */
    public boolean execute(Being player) {
        boolean success = player.isSuccess(this);
        if(success) {
            success = run(player);
        } else {
            player.tell(failureMessage);
            if(isSpell()) {
            	success = true;
            }
        }
        
        player.learnFromPractice(success, this);
        return success;
    }
    
    /**
     * Executes the ability.  Returns true if they randomly succeed, and the
     *  ability succeeded, or if it is a spell and they randomly failed.
     */
    public boolean execute(Being player, String command) {
        //TrollAttack.debug("Got to execute 2.");
        boolean success = player.isSuccess(this);
        if(success) {
            success = run(player, command);
        } else {
            player.tell(failureMessage);
            if(isSpell()) {
            	success = true;
            }
        }
        player.learnFromPractice(success, this);
        return success;
    }
    public boolean run(Being player) {
        //TrollAttack.debug("Got to dead-end 1.");
        return false;
    }
    public boolean run(Being player, String command) {
        //TrollAttack.debug("Got to dead-end 2." + toString());
        return false;
    }
    public static Ability find(String abilityName) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
