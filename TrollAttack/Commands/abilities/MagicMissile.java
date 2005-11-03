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

public class MagicMissile extends Spell {
    double probability = .6;
    int strength = 5;
    public MagicMissile() { super( "magic missile", 7, false); }
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

            player.tell("You manifest a ball of violent light, rocketing towards " + mob.getShort() + ".");
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
