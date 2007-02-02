package TrollAttack;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

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
 * Experience Rewarding Experience is rewarded for damage from a player against
 * a being Experience is calculated as the the (damagee's level minus the
 * damager's level + 5)* Damage
 */
public class Fight extends Thread {

    private Room fightRoom = null;
    private boolean fightOver = false;
    
    private Vector<Being> reds;
    private Vector<Being> blues;
    private Vector<Vector<Being>> teams;
    
    private Vector<Being> redDeaths;
    private Vector<Being> blueDeaths;

    public Fight() {
    	reds = new Vector<Being>();
    	blues = new Vector<Being>();
    	redDeaths = new Vector<Being>();
    	blueDeaths = new Vector<Being>();
    	teams = new Vector<Vector<Being>>();
    	
    	teams.add(reds);
    	teams.add(blues);
    }
    public void run() {
    	
        while (!fightOver && reds.size() > 0 && blues.size() > 0) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                TrollAttack.error("There was a problem sleeping for a second during a fight.");
            }
            //TrollAttack.debug("Running a round, its currently " + reds.size() + " vs. " + blues.size());
            runRound();
        }
        // When the fight is over, everyone who is left is a winner!
        Vector<Being> winners = (reds.size() > 0 ? reds : blues);
        Vector<Being> losers = (reds.size() > 0 ? blueDeaths : redDeaths);
        
        int loserWorth = 0;
        for(Being b : losers) {
        	loserWorth += (int)(b.getAverageHitDamage() * b.maxHitPoints);
        }
        for(Being b : winners) {
        	b.increaseExperience(loserWorth/winners.size());
        	b.interrupt("You have gained " + Communication.CYAN + (loserWorth/winners.size()) + Communication.WHITE + " experience points from this kill.");
        	b.setFight(null);
        }
        
    }

    Vector<Being> deceased = new Vector<Being>();
    public void runRound() {
    	/**
    	 * @TODO
    	 * NEEDS PRETTY PRINTING OF ROUND, EVERYTHING SHOULD COME AS 1 INTERRUPT whethe ryou are in the battle or not.
    	 */
    	
    	String msg, roomMsg;
    	double damage;
    	deceased = new Vector<Being>();
    	for(Vector<Being> team : teams) {
    		Vector<Being> oSide = getOtherSide(team.get(0));
    		
    		
    		for(Being b : team) {
    			Being victim = oSide.get(0);
    			if(b.hitSkill.roll() >= b.hitLevel && b.hitPoints > 0 && victim.hitPoints >= 0) {
    				//TrollAttack.debug(b.getShort() + " attacks " + victim.getShort());
    				damage = Math.floor(b.getHitDamage() - (victim.getArmorClass() / 10));
    				b.increaseExperience((int) ((victim.level - b.level + 5) * damage));
    				msg = "Your attack " + getDamageString((int)damage) + " " + victim.getShort() + ". [" + (int)damage + " damage]";
    				victim.hurt((int)damage);
    				roomMsg = Communication.WHITE + "%1 " + getDamageString((int)damage) + " %2. [" + (int)damage + " damage]";
    				if(victim.hitPoints < 0) {
    					deceased.add(victim);
    					msg += Util.wrapChar + Communication.RED + "YOU KILLED " + victim.getShort();
    					roomMsg += Util.wrapChar + Communication.RED + "%1 KILLED %2";
    				}
    				fightSay(msg, roomMsg, b, victim);
    			} else {
    			}
    			
    		}
    	}
    	sayFlush();
    	//TrollAttack.debug("Finishing up the round, " + deceased.size() + " beings died.");
    	for(Being b : deceased) {
    		isNowDead(b);
    	}
    	
    }

    public String getDamageString(int damage) {
    	if(damage < 1) {
    		return "misses";
    	} else if(damage < 5) {
    		return "scratches";
    	} else if(damage < 10) {
    		return "hits";
    	} else if(damage < 20) {
    		return "hurts";
    	} else if(damage < 50) {
    		return "rends";
    	} else {
    		return "DEMOLISHES";
    	}
    	
    }

    /**
     * Factory method that will create a fight and start it, but only if the 
     * two beings given are not already fighting.
     * @param being The aggressor being
     * @param being2 The victim being
     * @throws FightNotAllowed 
     */
	public static void ensureFight(Being being, Being being2) throws IllegalStateException {
		//TrollAttack.debug("We are ensuring a fight between " + being.getShort() + " and " + being2.getShort() + ".");
		if(!being.isFighting() && !being2.isFighting()) {
			// New Fight
			//TrollAttack.debug("Neither was fighting, new fight. " + being.getShort() + " on red team, " + being2.getShort() + " on blue team");
			Fight f = new Fight();
			f.fightRoom = being.getActualRoom();
			
			f.reds.add(being);
			f.blues.add(being2);
			being.setFight(f);
			being2.setFight(f);
			
			f.start();
		} else if(being.isFighting() && !being2.isFighting()) {
			// Being 1 is fighting, 2 isn't.  Add 2 to opposite side
			Fight f = being.getFight();
			being2.setFight(f);
			//TrollAttack.debug("Adding " + being2.getShort() + " to the team " + f.getOtherSide(being));
			f.getOtherSide(being).add(being2);
		} else if(!being.isFighting() && being2.isFighting()) {
			
			Fight f = being2.getFight();
			being.setFight(f);
			//TrollAttack.debug("Adding " + being.getShort() + " to the team " + f.getOtherSide(being2));
			f.getOtherSide(being2).add(being);
		} else {
			// Both are fighting!! can this fight happen?
			if(being.getFight() == being2.getFight()) {
				Fight f = being.getFight();
				if(f.getSide(being) != f.getSide(being2)) {
					// Nothing needs to change.
				} else {
					throw new IllegalStateException();
				}
			} else {
				//Combine two fights
				Fight master = being.getFight();
				Fight slave = being2.getFight();
				
				master.getSide(being).addAll(slave.getOtherSide(being2));
				master.getOtherSide(being).addAll(slave.getSide(being2));
				
				for(Being b : slave.reds) {
					b.setFight(master);
				}
				for(Being b : slave.blues) {
					b.setFight(master);
				}
				slave.end();
			}
		}
		
	}
	
	public Vector<Being> getSide(Being b) {
		if(reds.contains(b)) {
			return reds;
		} else if(blues.contains(b)) {
			return blues;
		} else {
			return null;
		}
	}
	public Vector<Being> getOtherSide(Being b) {
		if(reds.contains(b)) {
			return blues;
		} else if(blues.contains(b)) {
			return reds;
		} else {
			return null;
		}
	}
	
	public void end() {
		fightOver = true;
	}
	
	public void isNowDead(Being departed) {
		//TrollAttack.debug(departed.getShort() + " was killed in battle.");
		if(getSide(departed) == reds) {
			//TrollAttack.debug("A member of reds died.");
			redDeaths.add(departed);
			reds.remove(departed);
		} else {
			//TrollAttack.debug("A member of blues died.");
			blueDeaths.add(departed);
			blues.remove(departed);
		}
		departed.kill();
	}
	
	
	private Map<Being,String> messages = new HashMap<Being,String>();
	public void fightSay(String youMsg, String roomMsg, Being being1, Being being2) {
		String msg = messages.get(being1);
		if(msg == null) msg = "";
		messages.put(being1, msg + Util.wrapChar + youMsg.replaceAll("%1", "you"));
		//TrollAttack.debug("%2 not replaced in " + youMsg);
		for(Being b : fightRoom.roomBeings) {
			if(b != being1) {
				msg = messages.get(b);
				if(msg == null) msg = "";
				msg = msg + Util.wrapChar + roomMsg.replaceAll("%1", being1.getShort());
				if(b == being2) {
					msg = msg.replaceAll("%2","you");
				} else {
					msg = msg.replaceAll("%2",being2.getShort());
				}
				messages.put(b, msg);
			}
		}
	}
	
	public void sayFlush() {
		for(Entry<Being,String> entry : messages.entrySet()) {
			entry.getKey().interrupt(Util.uppercaseFirst(entry.getValue()));
		}
		messages = new HashMap<Being,String>();
	}
	
}
