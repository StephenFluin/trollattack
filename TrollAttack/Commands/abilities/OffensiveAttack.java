/*
 * Jan 9, 2007
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
import TrollAttack.Roll;
import TrollAttack.TrollAttack;
import TrollAttack.Commands.Ability;



public class OffensiveAttack extends Ability {
	String aName, aDesc, rDesc;
	Roll strength, timeCost;
	public OffensiveAttack(String attackName, String attackDescription, String roomDescription, Roll strength, Roll wait) {
		super(attackName);
		aName = attackName;
		aDesc = attackDescription;
		rDesc = roomDescription;
		this.strength = strength;
		this.timeCost = wait;
	}
	public boolean execute(Being player) {
		if(!player.isFighting()) {
			player.tell("You can only do this while fighting someone.");
			
		} else {
			return act(player,player.getFight().getOtherSide(player).get(0));
		}
		return false;
		
	}
	public boolean run(Being player, String command) {
		Being victim = player.getActualRoom().getBeing(command,player);
		if(victim == null || victim == player) {
			player.tell("You don't see that here.");
			return false;
		} else if(victim instanceof Player && !victim.getName().equalsIgnoreCase(command)) {
			player.tell("You can only attack players by using their full names.");
			return false;
		}
		Fight.ensureFight(player,victim);
		
		return act(player,victim);
		
		
	}
	public boolean act(Being actor, Being victim) {
		int str = strength.roll();
		victim.increaseHitPoints(-str);
		actor.tell(aDesc.replaceAll("%1", victim.getShort()) + "[" + str + " damage]");
		Being[] broadcast = {actor, victim};
		actor.getActualRoom().say(rDesc, broadcast);
		try{
			Thread.sleep(timeCost.roll()*1000);
		} catch(InterruptedException e) {
			TrollAttack.error("Can't sleep, clowns will eat me.");
			e.printStackTrace();
		}
		return true;
	}
}


