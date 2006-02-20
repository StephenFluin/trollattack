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
        first.setBeingFighting(second);
        second.setBeingFighting(first);
        fightRoom = a.getActualRoom();
        pBroadcast[0] = first;
        pBroadcast[1] = second;
    }

    public Boolean results() {
        return results;
    }

    public void run() {

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
        Being winner, loser;
        if (first.hitPoints > 0) {
            winner = first;
            loser = second;
        } else {
            winner = second;
            loser = first;
        }
        
        int experienceIncrease = (int) (loser.getAverageHitDamage() * loser.maxHitPoints);
        first
        .increaseExperience(experienceIncrease);
        
        winner.interrupt(Communication.RED + "You KILLED " + loser.getShort() + Util.wrapChar + Communication.WHITE + "You have gained " + Communication.CYAN + experienceIncrease + Communication.WHITE + " experience points from this kill.");
        pBroadcast[0] = winner;
        pBroadcast[1] = loser;
        fightRoom.say("%1 KILLED %2.", pBroadcast);
        loser.getActualRoom()
        .removeBeing(loser.name);
        
        first.setBeingFighting(null);
        second.setBeingFighting(null);
        loser.kill(winner);

        


    }

    public String runRound(Being first, Being second) {
        String result = "";
    	double damage;
        pBroadcast[0] = first;
        pBroadcast[1] = second;
        if (first.hitSkill.roll() >= first.hitLevel) {
            damage = Math.floor(first.getHitDamage() - (first.getArmorClass() / 10));
                first
                        .increaseExperience((int) ((second.level - first.level + 5) * damage));
                first.tell(Util.uppercaseFirst("Your attack " + getDamageString((int)damage) + " " + second.getShort() + ". [" + (int) damage + " damage]"));

            second.hitPoints = second.hitPoints - (int) damage;
            fightRoom.say(Communication.WHITE + "%1 " + getDamageString((int)damage) + " %2.[" + (int)damage + " damage]", pBroadcast);
            
        } else {

        }
        return result;

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
