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
        pBroadcast[0] = pBroadcast[1] = first;
        pBroadcast[2] = second;
    }

    public Boolean results() {
        return results;
    }

    public void run() {
        first.setBeingFighting(second);
        second.setBeingFighting(first);
        double damage;
        //TrollAttack.error("First Data: " + first.getHitDamage() + ", " +
        // first.hitSkill);
        //TrollAttack.error("Second Data: " + second.getHitDamage() + ", " +
        // second.hitSkill);
        while (first.hitPoints > 0 && second.hitPoints > 0) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                TrollAttack
                        .error("There was a problem sleeping for a second during a fight.");
            }
            runRound(first, second);
            runRound(second, first);
            first.tell(first.prompt());
            second.tell(second.prompt());

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
            first.tell(Communication.RED + "You KILLED " + second.getShort());
            pBroadcast[0] = pBroadcast[1] = first;
            pBroadcast[2] = second;
            fightRoom.say("%1 killed %2.", pBroadcast);
            if (second.isPlayer) {
                ((Player) second).kill(first);
            } else {
                // SHOULD BE HANDLED BY RESETS!!!!
                //TrollAttack.deadies.add( (Mobile)second,
                // first.getCurrentRoom() );
            }
            second.dropAll();
            int exp = first.getExperience();
            first
                    .increaseExperience((int) (second.getAverageHitDamage() * second.maxHitPoints));
            //TrollAttack.error("Experience was " + exp + " and became " +
            // TrollAttack.player.getExperience());

        } else {
            first.kill(second);

        }
        first.setBeingFighting(null);
        second.setBeingFighting(null);

    }

    public void runRound(Being first, Being second) {
        double damage;
        pBroadcast[0] = pBroadcast[1] = first;
        pBroadcast[2] = second;
        if (first.hitSkill.roll() >= first.hitLevel) {
            damage = Math.floor(first.getHitDamage());
            if (first.isPlayer) {
                first
                        .increaseExperience((int) ((second.level - first.level + 5) * damage));
            }
            first.tell(Util.uppercaseFirst(first.getShort(first)) + " rend ["
                    + (int) damage + "] " + second.getShort() + ".");

            fightRoom.say("%1 causes " + (int) damage + " damage to %2!",
                    pBroadcast);
            second.hitPoints = second.hitPoints - (int) damage;
        } else {
            first.tell("You miss " + second.getShort());
            fightRoom.say("%1 misses %2!", pBroadcast);
        }

    }

}
