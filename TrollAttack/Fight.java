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
    private Being first, second;
	private Boolean results = null;
	public Fight(Being a, Being b) {
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
		//TrollAttack.print("First Data: " + first.getHitDamage() + ", " + first.hitSkill);
		//TrollAttack.print("Second Data: " + second.getHitDamage() + ", " + second.hitSkill);
		while(first.hitPoints > 0 && second.hitPoints > 0) {
			try {
				Thread.sleep(1000);
			} catch( Exception e ) {
				
			}
			damage = Math.floor( Math.floor(Math.random()*100 + 1)/first.hitSkill * first.getHitDamage());
			first.increaseExperience((int)((second.level - first.level + 5)*damage));
			TrollAttack.print(first.getShort().substring(0,1).toUpperCase() + first.getShort().substring(1) + " rend[" + (int)damage + "] " + second.getShort() + ".");
			second.hitPoints = second.hitPoints - (int)damage;
			damage = Math.floor( Math.floor(Math.random()*100 + 1)/second.hitSkill * second.getHitDamage());
			TrollAttack.print(second.getShort().substring(0,1).toUpperCase() + second.getShort().substring(1) + " rends[" + (int)damage + "] " + first.getShort() + ".");
			first.hitPoints = first.hitPoints - (int)damage;
			TrollAttack.print(first.prompt());

		}
		if(first.hitPoints > 0) {
			results = new Boolean(true);
		} else {
			results = new Boolean(false);
		}
		boolean win = this.results().booleanValue();
		if(win) {
			TrollAttack.gameRooms[ first.getCurrentRoom()].removeMobile( second.name );
			TrollAttack.print("You killed " + second.getShort());
			TrollAttack.deadies.add( (Mobile)second, first.getCurrentRoom() );
			int exp = first.getExperience();
			first.increaseExperience((int)(second.level * second.getHitDamage() * second.maxHitPoints * 100 / second.hitSkill));
			//TrollAttack.print("Experience was " + exp + " and became " + TrollAttack.player.getExperience());
			
		} else {
			TrollAttack.print( second.getShort() + " kills you!");
			TrollAttack.gameOver = true;
			TrollAttack.endGame();
			//Is there any way to stop the entire program? Oh well....
		}
		first.setBeingFighting(null);
		second.setBeingFighting(null);
	}
	
}
