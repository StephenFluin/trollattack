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
import TrollAttack.Commands.Spell;

public class Heal extends Spell {
    private int strength = 10;
    
    public Heal() { super("heal", 6, 3); }
    public boolean run(Being player) {
        player.tell("You pass a healing hand over yourself.");
        player.increaseHitPoints( strength );
        return true;
    }
    public boolean run(Being player,  String s ) {
        if( s.compareToIgnoreCase("self") == 0 ) {
            return this.run(player);
        }
        Being victim = player.getActualRoom().getBeing( s, null );
        if( victim == null) {
            player.tell("You don't see that here.");
            return false;
        } else {

            player.tell("You pass a healing hand over " + victim.getShort() + ".");
            Being[] players = {player, victim};
            player.getActualRoom().say("%1 passes a healing hand over %2.", players);
            victim.increaseHitPoints( strength );
            return true;
           
        }
    }
}