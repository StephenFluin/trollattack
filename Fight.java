/*
 * Created on May 9, 2005
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
public class Fight {
	Being first, second;
	public Fight(Being a, Being b) {
		first = a;
		second = b;
	}
	
	public boolean runFight() {
		double damage;
		while(first.hitPoints > 0 && second.hitPoints > 0) {
			damage = Math.floor( Math.floor(Math.random()*100 + 1)/first.hitSkill * first.hitDamage);
			TrollAttack.print(first.getShort().substring(0,1).toUpperCase() + first.getShort().substring(1) + " rend[" + damage + "] " + second.getShort() + ".");
			second.hitPoints = second.hitPoints - (int)damage;
			damage = Math.floor( Math.floor(Math.random()*100 + 1)/second.hitSkill * second.hitDamage);
			TrollAttack.print(second.getShort().substring(0,1).toUpperCase() + second.getShort().substring(1) + " rends[" + damage + "] " + first.getShort() + ".");
			first.hitPoints = first.hitPoints - (int)damage;
		}
		if(first.hitPoints > 0) {
			return true;
		} else {
			return false;
		}
	}
}
