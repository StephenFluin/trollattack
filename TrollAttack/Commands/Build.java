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

import java.io.File;

import TrollAttack.*;
import TrollAttack.Classes.Class;
import TrollAttack.Classes.Class.Classes;
import TrollAttack.Items.*;

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
        } else if(s.startsWith("destroy")) {
            player.getActualArea().areaRooms.delete(player.getActualRoom());
            TrollAttack.gameRooms.delete(player.getActualRoom());
            player.setCurrentRoom(1);
            TrollAttack.getRoom(1).addBeing(player);
            
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
                destRoom = player.getActualRoom().getLink(direction);
                destRoom.setLink(Exit.directionOpposite(direction), 0);
            }
            
            player.getActualRoom().setLink(direction, 0);
            //TrollAttack.message("Erasing link " + Exit.directionName(direction));
        } else {
            Exit exit = player.getActualRoom().getExit(direction);
            if(exit == null) {
                int destination = Util.intize(player, parts[1]);
                //TrollAttack.message("Found destination " + destination + " from part '" + parts[0] + "'.");
                if(reciprocol) {
                    TrollAttack.getRoom(destination).setLink(Exit.directionOpposite(direction), player.getCurrentRoom());
                }
                player.getActualRoom().setLink(direction, destination);
                exit = player.getActualRoom().getExit(direction);
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
            area();
            return;
        }
        command = parts[1];
        for(int i = 2;i<parts.length;i++) {
            command += " " + parts[i];
        }
        if(s.startsWith("name")) {
            area.name = command;
            player.tell("Area name changed to '" + area.name + "'");
        } else if(s.startsWith("filename")) {
            if(parts[1].length() < 3) {
                player.tell("Area filename must be at least 3 characters long.");
            } else {
                if(!parts[1].endsWith(".xml")) {
                    parts[1] += ".xml";
                }
                String oldFilename = area.filename;
                area.filename = parts[1];
                area.save(TrollAttack.gameRooms, TrollAttack.gameMobiles, TrollAttack.gameItems);
                File oldFile = new File("Areas/" + oldFilename);
                TrollAttack.message("Deleting file Areas/" + oldFilename + ".");
                oldFile.delete();
                
                
                player.tell("Area filename changed to '" + parts[1] + "'");
            }
        } else if(s.startsWith("low")) {
            area.low = Util.intize(player, command);
            player.tell("Area range changed to " + area.low + "-" + area.high + ".");
        } else if(s.startsWith("high")) {
            area.high = Util.intize(player, command);
            player.tell("Area range changed to " + area.low + "-" + area.high + ".");
        }  else {
            player.tell("Usage: area <command> <value>");
            player.tell("Possible Commands:");
            player.tell("\tfilename <new file name>");
            player.tell("\tname <new name>");
            player.tell("\thigh <new high vnum>");
            player.tell("\tlow <new low vnum>");
            return;
        }
        
        
    }
    public void area(String s) {
        String[] parts = s.split(" ");
        if(parts.length == 1) {
            player.tell("Usage: area <command> <value>");
            player.tell("Possible Commands:");
            player.tell("\tfilename <new file name>");
            player.tell("\tname <new name>");
            player.tell("\thigh <new high vnum>");
            player.tell("\tlow <new low vnum>");
        } else {
            if(parts[0].compareToIgnoreCase("filename") == 0) {
                if(parts[1].length() < 3) {
                    player.tell("Area filename must be at least 3 characters long.");
                } else {
                    if(!parts[1].endsWith(".xml")) {
                        parts[1] += ".xml";
                    }
                    player.getArea().filename = parts[1];
                    player.tell("Area filename changed to '" + parts[1] + "'");
                    
                }
            } else if(parts[0].compareToIgnoreCase("name") == 0) {
                String areaName = parts[1];
                for(int i = 2;i<parts.length;i++) {
                    areaName += " " + parts[i];
                }
                player.getArea().name = areaName;
                player.tell("Area name changed to '" + areaName + "'");
            }
            
        }
    }
    public void area() {

        player.tell("Your area " + (player.getArea().frozen ? "<frozen>" : "") + ": " + player.getArea().filename + "\n" + player.getArea().name);
        player.tell("Vnum range:\t" + player.getArea().low + "-" + player.getArea().high);
        
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
    public void mSet(String[] parts) {

        Being mobile = null;
        try {
           mobile = (Being)player.getActualRoom().getBeing(parts[0], player);
        } catch(Exception e) {
            TrollAttack.error("Possilbe attempt to change player with mset.");
            e.printStackTrace();
        }
        if(mobile != null) {
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
            if(attr.compareToIgnoreCase("hp") == 0) {
                mobile.hitPoints = intValue;
            } else if(attr.compareToIgnoreCase("hp") == 0) {
                mobile.hitPoints = intValue;
            } else if(attr.compareToIgnoreCase("maxhp") == 0) {
                mobile.maxHitPoints = intValue;
            } else if(attr.compareToIgnoreCase("mana") == 0) {
                mobile.manaPoints = intValue;
            } else if(attr.compareToIgnoreCase("maxmana") == 0) {
                mobile.maxManaPoints = intValue;
            } else if(attr.compareToIgnoreCase("move") == 0) {
                mobile.movePoints = intValue;
            } else if(attr.compareToIgnoreCase("maxmove") == 0) {
                mobile.maxMovePoints = intValue;
            } else if(attr.compareToIgnoreCase("name") == 0) {
                mobile.name = value;
            } else if(attr.compareToIgnoreCase("short") == 0) {
                mobile.shortDescription = value;
            } else if(attr.compareToIgnoreCase("long") == 0) {
                mobile.longDesc = value;
            } else if(attr.compareToIgnoreCase("gold") == 0) {
                mobile.gold = intValue;
            } else if(attr.compareToIgnoreCase("damagedice") == 0) {
                mobile.hitDamage = new Roll(value);
            } else if(attr.compareToIgnoreCase("hitdice") == 0) {
                mobile.hitSkill = new Roll(value);
            } else if(attr.compareToIgnoreCase("hitlevel") == 0) {
                mobile.hitLevel = intValue;
            } else if(attr.compareToIgnoreCase("level") == 0) {
                mobile.level = intValue;
            } else if(attr.compareToIgnoreCase("trainer") == 0) {
                if(value.length() < 4) {
                    mobile.canTeach = (!mobile.canTeach);
                } else {
                    mobile.canTeach = new Boolean(value).booleanValue();
                }
            } else if(attr.compareToIgnoreCase("class") == 0) {
                player.tell( mobile.setBeingClass(value));
                
            } else {
                player.tell(attr + " is not a valid attribute for a mobile!");
                return;
            }
            player.tell("You set the '" + attr + "' attribute of " + mobile.getShort() + ".");
        } else {
            player.tell("You don't see that here!");
            }
    }
}
