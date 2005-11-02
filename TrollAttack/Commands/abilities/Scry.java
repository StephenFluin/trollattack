/*
 * Created on Nov 2, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
package TrollAttack.Commands.abilities;
import TrollAttack.Exit;
import TrollAttack.Player;
import TrollAttack.Room;
import TrollAttack.Commands.*;

public class Scry extends Ability {
    public Scry() {
        super("scry");
    }
    public boolean execute(Player player) {
        player.tell("Usage: scry <direction>");
        return false;
    }
    public boolean execute(Player player, String command) {
        int direction = Exit.getDirection(command);
        if(direction == 0) {
            player.tell("That isn't a direction!");
            return false;
        }
        int tmpProficiency = player.getProficiency(this);
        while(tmpProficiency > 0) {
            Room destination = player.getActualRoom().getLink(direction);
            if(destination == null) {
                player.tell("There are no more rooms in this direction");
                return true;
            }
        }
        return true;
    }
}
