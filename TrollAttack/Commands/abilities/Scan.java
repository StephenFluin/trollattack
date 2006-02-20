/*
 * Created on Nov 2, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
package TrollAttack.Commands.abilities;
import java.util.LinkedList;

import TrollAttack.*;
import TrollAttack.Commands.*;

public class Scan extends Ability {
    public Scan() {
        super("scan");
    }
    public boolean execute(Being player) {
        player.tell("Usage: scan <direction>");
        return false;
    }
    public boolean run( Being player, String command) {
        int distance = 0;
        int direction = Exit.getDirection(command);
        if(direction == 0) {
            player.tell("That isn't a valid direction!");
            return false;
        }
        double tmpProficiency = player.getProficiency(this);
        Room destination = player.getActualRoom().getLink(direction);
        while(tmpProficiency > 0) {
            
            if(destination == null) {
                player.tell("There are no " + (distance == 0 ? "" : "more " ) + "rooms in this direction");
                return true;
            } else {
                if(distance++ == 0) {
                    player.tell(Communication.WHITE + "You scan to the " + Exit.directionName(direction) + " and see:");
                } else {
                    player.tell(Communication.WHITE + "You continue scanning to the " + Exit.directionName(direction) + ":");
                }
                player.look(destination);
                destination = destination.getLink(direction);
            }
            tmpProficiency -= 20;
        }
        player.tell("You can't see any farther.");
        return true;
    }
    

}
