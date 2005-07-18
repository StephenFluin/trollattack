/*
 * Created on Jul 9, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
package TrollAttack.Commands;

import TrollAttack.*;
import TrollAttack.Exit;
import TrollAttack.Player;
import TrollAttack.Room;
import TrollAttack.TrollAttack;
import TrollAttack.Items.Item;

/**
 * @author PeEll
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Build {
	Player player;
    public Build(Player player) {
	    this.player = player;
	}
    
    public void redit(String s) {
        String[] parts = s.split(" ");
        s = parts[0];
        if(parts.length < 2) {
            player.tell(s + " command needs more info.");
            return;
        }
        String command = parts[1];
        
        for(int i = 2;i < parts.length;i++) {
            command += " " + parts[i];
        }
        
        if(s.startsWith("exit")) {
            editExit(command, false);
        } else if(s.startsWith("bexit")) {
           editExit(command, true);
        } else if(s.startsWith("title")) {
            player.getActualRoom().title = command;
        } else if(s.startsWith("desc")) {
            player.getActualRoom().description = command;
        }
    }
    public void editExit(String s, boolean reciprocol) {
        String[] parts = s.split(" ");
        if(parts.length < 1) {
            player.tell("Which direction?");
            return;
        }
        int direction = Exit.getDirection(parts[0]);
        if(direction == 0) {
            player.tell("That isn't a direction!");
            return;
        }
        if(parts.length == 1) {
            Room destRoom;
            if(reciprocol) {
                destRoom = TrollAttack.getRoom(player.getActualRoom().followLink(direction).getDestination());
                destRoom.setLink(Exit.directionOpposite(direction), 0);
            }
            
            player.getActualRoom().setLink(direction, 0);
            TrollAttack.message("Erasing link " + Exit.directionName(direction));
        } else {
            Exit exit = player.getActualRoom().followLink(direction);
            if(exit == null) {
                int destination = Util.intize(player, parts[1]);
                //TrollAttack.message("Found destination " + destination + " from part '" + parts[0] + "'.");
                if(reciprocol) {
                    TrollAttack.getRoom(destination).setLink(Exit.directionOpposite(direction), player.getCurrentRoom());
                }
                player.getActualRoom().setLink(direction, destination);
                exit = player.getActualRoom().followLink(direction);
            }
            Exit bexit = exit.getOtherExit();
            if(Util.contains(s, "nodoor")) {
                exit.setDoor(false);
                exit.setLockable(null);
                if(reciprocol) {
                    bexit.setDoor(false);
                    bexit.setLockable(null);
                }
            } else if(Util.contains(s, "door")) {
                exit.setDoor(true);
                if(reciprocol) {
                    bexit.setDoor(true);
                }
            }
            if(Util.contains(s, "lockable")) {
                Item key = null;
                try {
                    key = TrollAttack.getItem(new Integer(s.substring(s.indexOf("locked"))));
                } catch(Exception e) {
                    player.tell("Please be clearer about which key you want to use, locked <vnum>.");
                }
                exit.setLockable(key);
            } else if(Util.contains(s, "notlockable")) {
                exit.setLockable(null);
            }
            
        }
        
    }
    
    public void mStat(Being mobile) {
        player.tell( ( mobile.isPlayer() ? "" : ("#" + ((Mobile)mobile).vnum + "\t") ) + mobile.getShort() + "'s Information");
        player.tell("Name:\t\t" + mobile.getName());
        player.tell("Short Desc.:\t" + mobile.getShort());
        player.tell("Long Desc.:\t" + mobile.getLong());
        player.tell("Hit Points:\t" + mobile.hitPoints + "/" + mobile.maxHitPoints);
        player.tell("Mana Points:\t" + mobile.manaPoints + "/" + mobile.maxManaPoints);
        player.tell("Move Points:\t" + mobile.movePoints + "/" + mobile.maxMovePoints);
        player.tell("Level:\t" + mobile.level + "\t" + "Gold:\t" + mobile.gold);
        player.tell("Damage Dice:\t" + mobile.hitDamage.toString() + "\t" + "Hit Dice:\t" + mobile.hitSkill.toString());
        player.tell("Hit Level (Minimum roll to hit):\t" + mobile.hitLevel);
        player.tell("Experience:\t" + mobile.experience + "\t" + "Favor:\t" + mobile.favor);
        if(!mobile.isPlayer()) { player.tell("Clicks:\t" + ((Mobile)mobile).getClicks()); }
    }
    
    public void iStat(Item item) {
        player.tell( item.getShort() + "'s Information");
        player.tell("Name:\t\t" + item.getName());
        player.tell("Short Desc.:\t" + item.getShort());
        player.tell("Long Desc.:\t" + item.getLong());
        player.tell("Item Type:\t" + item.getType());
        player.tell("Cost:\t" + item.cost + "\t" + "Weight:\t" + item.weight);
        player.tell(item.getTypeData());
    }
    public void area() {
        player.tell("Your area " + (player.getArea().frozen ? "<frozen>" : "") + ": " + player.getArea().filename + " " + player.getArea().name);
    }
}
