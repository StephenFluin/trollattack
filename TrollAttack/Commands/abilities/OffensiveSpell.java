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
import TrollAttack.Player;
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
        if(!player.isFighting()) {
            player.tell("You can't do this to yourself!");
        } else {
        	return run(player,player.getFight().getOtherSide(player).get(0));
        }
        return false;
    }
    public boolean run(Being player, String s) {
        Being mob = player.getActualRoom().getBeing( s, player );
        if(mob instanceof Player && !mob.getName().equalsIgnoreCase(s)) {
			player.tell("You can only attack players by using their full names");
			return false;
		}
        return run(player,mob);
    }
    public boolean run(Being player, Being mob) {
    	 if( mob == null) {
             player.tell("You don't see that here.");
             return false;
         } else {
             player.tell(success.replaceAll("%1",mob.getShort()) + " [" + strength + " damage]");
             Being[] ignore = {player,mob};
             player.getActualRoom().say(victim, ignore);
             mob.increaseHitPoints(-strength);
             Fight.ensureFight(player,mob);
            
             return true;
            
         }
    }
}
