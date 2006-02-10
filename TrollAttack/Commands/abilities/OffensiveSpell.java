/*
 * Created on Nov 3, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
package TrollAttack.Commands.abilities;

import TrollAttack.Being;
import TrollAttack.Fight;
import TrollAttack.Commands.Spell;

public class OffensiveSpell extends Spell {
    int strength;
    String success, victim;
    public OffensiveSpell(String name, int cost, int strength, String success, String victim) { 
    	super( name, cost, false); 
    	this.strength = strength;
    	this.success = success;
    	this.victim = victim;
    }
    public boolean run(Being player) {
        if(player.getFighting() == null) {
            player.tell("You can't do this to yourself!");
        }
        return false;
    }
    public boolean run(Being player, String s) {
        Being mob = player.getActualRoom().getBeing( s, player );
        if( mob == null) {
            player.tell("You don't see that here.");
            return false;
        } else {
            player.tell(success.replaceAll("%1",mob.getShort()) + " [" + strength + " damage]");
            Being[] ignore = {player,mob};
            player.getActualRoom().say(victim, ignore);
            mob.increaseHitPoints(-strength);
            if(player.isFighting()) {
                
            } else {
                Fight myFight = new Fight(player, mob );
                myFight.start();
            }
            return true;
           
        }
    }
}
