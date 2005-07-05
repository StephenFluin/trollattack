package TrollAttack;
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

/**
 * 
 * Experience Rewarding
 * Experience is rewarded for damage from a player against a being
 * Experience is calculated as the the (damagee's level minus the
 * damager's level + 5)* Damage
 */
public class Fight extends Thread {
    private Player first;
    private Being second;
	private Boolean results = null;
	public Fight(Player a, Being b) {
		first = a;
		second = b;
	}
	public Boolean results() {
	    return results;
	}
	public void run() {
	    first.setBeingFighting(second);
	    second.setBeingFighting(first);
		double damage;
		//TrollAttack.error("First Data: " + first.getHitDamage() + ", " + first.hitSkill);
		//TrollAttack.error("Second Data: " + second.getHitDamage() + ", " + second.hitSkill);
		while(first.hitPoints > 0 && second.hitPoints > 0) {
			try {
				Thread.sleep(1000);
			} catch( Exception e ) {
				TrollAttack.error("There was a problem sleeping for a second during a fight.");
			}
			damage = Math.floor( Math.floor(Math.random()*100 + 1)/first.hitSkill * first.getHitDamage());
			first.increaseExperience((int)((second.level - first.level + 5)*damage));
			first.tell(Util.uppercaseFirst(first.getShort(first)) + " rend[" + (int)damage + "] " + second.getShort() + ".");
			second.hitPoints = second.hitPoints - (int)damage;
			damage = Math.floor( Math.floor(Math.random()*100 + 1)/second.hitSkill * second.getHitDamage());
			first.tell(Util.uppercaseFirst(second.getShort()) + " rends[" + (int)damage + "] " + first.getShort(first) + ".");
			first.hitPoints = first.hitPoints - (int)damage;
			first.tell(first.prompt());

		}
		if(first.hitPoints > 0) {
			results = new Boolean(true);
		} else {
			results = new Boolean(false);
		}
		boolean win = this.results().booleanValue();
		if(win) {
			TrollAttack.getRoom( first.getCurrentRoom()).removeBeing( second.name );
			first.tell("You killed " + second.getShort());
			if(second.isPlayer) {
			    ((Player)second).kill(first);
			} else {
			    TrollAttack.deadies.add( (Mobile)second, first.getCurrentRoom() );
			}
			second.dropAll();
			int exp = first.getExperience();
			first.increaseExperience((int)(second.level * second.getHitDamage() * second.maxHitPoints * 100 / second.hitSkill));
			//TrollAttack.error("Experience was " + exp + " and became " + TrollAttack.player.getExperience());
			
		} else {
		    first.kill( second );

		}
		first.setBeingFighting(null);
		second.setBeingFighting(null);
	}
	
}
