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
import TrollAttack.Items.*;
import TrollAttack.Items.Item;

/**
 * @author PeEll
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Build {
	Being player;
    public Build(Being player) {
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
            player.tell("You forge a pathway in one direction.");
        } else if(s.startsWith("bexit")) {
           editExit(command, true);
           player.tell("You forge a pathway in both directions.");
        } else if(s.startsWith("title")) {
            player.getActualRoom().title = command;
            player.tell("You change the title of the room.");
        } else if(s.startsWith("desc")) {
            player.getActualRoom().description = command;
            player.tell("You change the description of the room.");
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
            //TrollAttack.message("Erasing link " + Exit.directionName(direction));
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
    public void mStat(Being being) {
        player.tell(Util.mStat(being));
    }
    
    
    public void iStat(Item item) {
        player.tell( item.getShort() + "'s Information");
        player.tell("Original:\t\t(" + (item == TrollAttack.getItem(new Integer(item.vnum)) ? "X" : " ") + ")");
        player.tell("Name:\t\t" + item.getName());
        player.tell("Short Desc.:\t" + item.getShort());
        player.tell("Long Desc.:\t" + item.getLong());
        player.tell("Item Type:\t" + item.getType());
        player.tell("Cost:\t" + item.cost + "\t" + "Weight:\t" + item.weight);
        player.tell(item.getTypeData());
    }
    public void aStat(Area area) {
        player.tell("Information about:" + area.name);
        player.tell("Name:\t" + area.name);
        player.tell("Filename:\t" + area.filename);
        player.tell("Vnums:\t" + area.low + "-" + area.high);
        player.tell("Frozen:\t(" + (area.frozen ? "X" : " " ));
    }
    public void aSet(Area area, String command) {
        String[] parts = command.split(" ");
        String s = parts[0];
        if(parts.length < 2) {
            player.tell(command + " command needs more info.");
            return;
        }
        command = parts[1];
        for(int i = 2;i<parts.length;i++) {
            command += " " + parts[i];
        }
        if(s.startsWith("name")) {
            area.name = command;
        } else if(s.startsWith("filename")) {
            if(parts[1].endsWith(".xml")) {
                area.filename = parts[1];
            } else {
                area.filename = parts[1] + ".xml";
            }
        } else if(s.startsWith("low")) {
            area.low = Util.intize(player, command);
        } else if(s.startsWith("high")) {
            area.high = Util.intize(player, command);
        }
        
        
    }
    public void area() {
        player.tell("Your area " + (player.getArea().frozen ? "<frozen>" : "") + ": " + player.getArea().filename + " " + player.getArea().name);
    }
    public void iSet(String[] parts) {
        Item item = null;
        try {
           item = (Item)player.getActualRoom().getItem(parts[0]);
        } catch(Exception e) {
            //TrollAttack.error("Possilbe attempt to change player with mset.");
            e.printStackTrace();
        }
        if(item != null) {
            String attr = parts[1];
            String value = parts[2];
            for(int i = 3;i < parts.length;i++) {
                value += " " + parts[i];
            }
            int intValue = 0;
            try {
               intValue  = new Integer(value).intValue();
            } catch(Exception e) {
                
            }
           if(attr.compareToIgnoreCase("name") == 0) {
                item.name = value;
            } else if(attr.compareToIgnoreCase("short") == 0) {
                item.shortDesc = value;
            } else if(attr.compareToIgnoreCase("long") == 0) {
                item.longDesc = value;
            } else if(attr.compareToIgnoreCase("weight") == 0 ) {
                item.weight = intValue;
            } else if(attr.compareToIgnoreCase("cost") == 0 ) {
                item.cost = intValue;
            } else if(attr.compareToIgnoreCase("type") == 0 ) {
                Item newItem;
                if( value.compareToIgnoreCase(Weapon.getItemType()) == 0 && item.getType() != Weapon.getItemType() ) {
                    newItem = new Weapon(item);
                } else if(value.compareToIgnoreCase(Armor.getItemType()) == 0 && item.getType() != Armor.getItemType()) {
                    newItem = new Armor(item);
                } else if(value.compareToIgnoreCase(Food.getItemType()) == 0 && item.getType() != Food.getItemType()) {
                    newItem = new Food(item);
                } else if(value.compareToIgnoreCase(DrinkContainer.getItemType()) == 0 && item.getType() != DrinkContainer.getItemType()) {
                    newItem = new DrinkContainer(item);
                } else if(value.compareToIgnoreCase(Fountain.getItemType()) == 0 && item.getType() != Fountain.getItemType()) {
                    newItem = new Fountain(item);
                } else {
					player.tell("What type do you want to make this item???" + value);
					return;
                }
                player.getActualRoom().replaceItem(item, newItem);
                TrollAttack.replaceItem(item, newItem);
            } else {
                if(item.getType().compareToIgnoreCase(Weapon.getItemType())== 0) {
                    Weapon weapon = (Weapon)item;
                    if(attr.compareToIgnoreCase("damage") == 0) {
		                weapon.setDamage(value);
		            }
                    return;
                } else if(item.getType().compareToIgnoreCase(Armor.getItemType()) == 0) {
                    Armor armor = (Armor)item;
                    if(attr.compareToIgnoreCase("armorclass") == 0) {
                        armor.setArmorClass(Util.intize(player, value));
                    } else if(attr.compareToIgnoreCase("wearlocation") == 0) {
                        armor.setWearLocation(value);
                    }
                    return;
                } else if(item.getType().compareToIgnoreCase(Food.getItemType()) == 0) {
                    Food food = (Food)item;
                    if(attr.compareToIgnoreCase("quality") == 0) {
                        food.setQuality(Util.intize(player, value));
                    }
                    return;
                } else if(item.getType().compareToIgnoreCase(DrinkContainer.getItemType()) == 0) {
                    DrinkContainer drinkcon = (DrinkContainer)item;
                    if(attr.compareToIgnoreCase("volume") == 0) {
                        drinkcon.setVolume(Util.intize(player, value));
                    } else if(attr.compareToIgnoreCase("capacity") == 0) {
                        drinkcon.setCapacity(Util.intize(player, value));
                    }
                    return;
                }
                player.tell(attr + " is not a valid attribute for this item!");
                return;
            }
        } else {
            player.tell("You don't see that here!");
            return;
        }
    }
}
