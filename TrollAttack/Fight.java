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
 * Experience Rewarding Experience is rewarded for damage from a player against
 * a being Experience is calculated as the the (damagee's level minus the
 * damager's level + 5)* Damage
 */
public class Fight extends Thread {
    private Being first;

    private Being second;

    private Boolean results = null;

    private Room fightRoom = null;

    private Being[] pBroadcast = new Being[3];

    public Fight(Being a, Being b) {
        first = a;
        second = b;
        fightRoom = a.getActualRoom();
        pBroadcast[0] = first;
        pBroadcast[1] = second;
    }

    public Boolean results() {
        return results;
    }

    public void run() {
        first.setBeingFighting(second);
        second.setBeingFighting(first);
        while (first.hitPoints > 0 && second.hitPoints > 0) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                TrollAttack
                        .error("There was a problem sleeping for a second during a fight.");
            }
            runRound(first, second);
            runRound(second, first);
            first.tell("");
            second.tell("");
            first.prompt();
            second.prompt();

        }
        if (first.hitPoints > 0) {
            results = new Boolean(true);
        } else {
            results = new Boolean(false);
        }
        boolean win = this.results().booleanValue();
        if (win) {
            TrollAttack.getRoom(first.getCurrentRoom())
                    .removeBeing(second.name);
            int experienceIncrease = (int) (second.getAverageHitDamage() * second.maxHitPoints);
            first.interrupt(Communication.RED + "You KILLED " + second.getShort() + Util.wrapChar + Communication.WHITE + "You have gained " + Communication.CYAN + experienceIncrease + Communication.WHITE + " experience points from this kill.");
            pBroadcast[0] = first;
            pBroadcast[1] = second;
            fightRoom.say("%1 killed %2.", pBroadcast);
            if (second.isPlayer) {
                ((Player) second).kill(first);
            } else {
                // SHOULD BE HANDLED BY RESETS!!!!
            }
            second.dropAll();
            
            first
                    .increaseExperience(experienceIncrease);
        } else {
            first.kill(second);

        }
        first.setBeingFighting(null);
        second.setBeingFighting(null);

    }

    public void runRound(Being first, Being second) {
        double damage;
        pBroadcast[0] = first;
        pBroadcast[1] = second;
        if (first.hitSkill.roll() >= first.hitLevel) {
            damage = Math.floor(first.getHitDamage());
            if (first.isPlayer) {
                first
                        .increaseExperience((int) ((second.level - first.level + 5) * damage));
            }
            first.tell(Util.uppercaseFirst("Your attack " + getDamageString((int)damage) + " " + second.getShort() + ". [" + (int) damage + " damage]"));

            fightRoom.say(Communication.WHITE + "%1 " + getDamageString((int)damage) + " %2. [" + (int) damage + " damage]",
                    pBroadcast);
            second.hitPoints = second.hitPoints - (int) damage;
        } else {

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

}
