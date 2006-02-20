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
import java.util.LinkedList;
import java.io.File;

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
        
        if(s.startsWith("now")) {
        	boolean noWander  = player.getActualRoom().getNoWander();
        	player.getActualRoom().setNoWander(!noWander);
        	if(!noWander) {
        		player.tell("You prevent mobiles from wandering into this room.");
        	} else {
        		player.tell("You allow mobiles to wander into this room.");
        	}
        	return;
        }
       
        
        
        String command = parts[1];
        
        for(int i = 2;i < parts.length;i++) {
            command += " " + parts[i];
        }
        
        if(s.startsWith("exit")) {
            editExit(command, false);
            return;
        } else if(s.startsWith("bexit")) {
           editExit(command, true);
           return;
        } else if(s.startsWith("shop")) {
        	editShop(command);
        }
        if(parts.length < 2) {
            player.tell(s + " command needs more info.");
            return;
        }
        if(s.startsWith("title")) {
            player.getActualRoom().title = command;
            player.tell("You change the title of the room.");
        } else if(s.startsWith("desc")) {
            player.getActualRoom().description = command;
            player.tell("You change the description of the room.");
        } else if(s.startsWith("destroy")) {
            player.getActualArea().areaRooms.remove(player.getActualRoom());
            TrollAttack.gameRooms.remove(player.getActualRoom());
            player.setCurrentRoom(1);
            TrollAttack.getRoom(1).addBeing(player);
            
        }
    }
    public void editExit(String s, boolean reciprocol) {
        String[] parts = s.split(" ");
        if(parts.length < 1) {
            player.tell("Usage: redit bexit <direction> <destination> [door|nodoor] [lockable <key vnum>|notlockable] [wanderable|nowanderable]");
            player.tell("       redit exit <direction> <destination> [door|nodoor] [lockable <key vnum>|notlockable] [wanderable|nowanderable");
            player.tell("The bexit command will make a link from your current room to the destination, AND a link from the destination to this room.  This is the standard way to make exits.");
            player.tell(Util.wrapChar + "Example: redit bexit east 10023 door");
            return;
        }
        int direction = Exit.getDirection(parts[0]);
        if(direction == 0) {
            player.tell("That isn't a direction!");
            return;
        }
        
        //Length of 1 means clear the exit(s).
        if(parts.length == 1) {
            Room destRoom;
            if(reciprocol) {
                destRoom = player.getActualRoom().getLink(direction);
                destRoom.setLink(Exit.directionOpposite(direction), 0);
            }
            
            player.getActualRoom().setLink(direction, 0);
           
        // Means we are attempting to create a new exit.
        } else {
            Exit exit = player.getActualRoom().getExit(direction);
            if(exit == null) {
                int destination = Util.intize(player, parts[1]);
                try {
                    if(reciprocol) {
                        player.tell(TrollAttack.getRoom(destination).setLink(Exit.directionOpposite(direction), player.getCurrentRoom()));
                    }
                } catch(NullPointerException e) {
                    player.tell("That is not a valid destination for this direction.");
                    return;
                }
                player.tell(player.getActualRoom().setLink(direction, destination));
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
                    key = TrollAttack.getItem(new Integer(s.substring(s.indexOf("lockable"))));
                } catch(Exception e) {
                    player.tell("Please be clearer about which key you want to use, locked <vnum>.");
                }
                exit.setLockable(key);
            } else if(Util.contains(s, "notlockable")) {
                exit.setLockable(null);
            }
            
        }
        
    }
    public void editShop(String command) {
    	if(command.length() < 1) {
    		player.tell("Usage: redit shop add <vnum>" + Util.wrapChar +
    					"       redit shop remove <vnum>" + Util.wrapChar +
    					"       redit shop create" + Util.wrapChar +
    					"       redit shop destroy");
    		return;
    	}
    	if(command.startsWith("c")) {
    		if(player.getActualRoom() instanceof Shop) {
    			player.tell("This is already a shop.");
    			return;
    		} else {
    			player.tell("You turn this room into a SHOP!");
    			TrollAttack.replaceRoom(player.getActualRoom(), new Shop(player.getActualRoom()));
    		}
    	}
    	if(player.getActualRoom() instanceof Shop) {
    		Shop s = ((Shop)player.getActualRoom());
    		if(command.startsWith("a")) {
    			Item i = null;
    			if(Util.split(command).length > 1) {
    				i = TrollAttack.getItem(Util.intize(Util.split(command)[1]));
    			}
        		if(i == null) {
        			player.tell("That isn't a valid item.");
        			return;
        		} else {
        			s.addShopItem(i);
        			player.tell("You add " + i.getShort() + " to this shop.");
        		}
        	} else if(command.startsWith("r")) {
        		Item removal = null;
        		if(Util.split(command).length != 2) {
        			player.tell("What do you want to remove?");
        			return;
        		}
        		for(Item i : s.shopItems) {
        			if(Util.contains(i.getShort(),  Util.split(command)[1])) {
        				removal = i;
        				break;
        			}
        		}
        		if(removal != null) {
        			s.shopItems.remove(removal);
        			player.tell("You remove " + removal.getShort() + " from this shop.");
        		} else {
        			player.tell("There is nothing in this shop by that name.");
        		}
        		
        	} else if(command.startsWith("dest")) {
        		//Room r = new Room(player.getActualRoom());
        		player.tell("the game doesn't yet know how to convert a shop back to a room, email PeELl and tell him to get off of his but.");
        	}
    	} else {
    		player.tell("You can only do this to a shop!");
    		return;
    	}
    	
    }
    public void mStat(Being being) {
        player.tell(Util.mStat(being));
    }
    public void rStat(Room room) {
    	String message = "Name:\t\t" + room.title + Util.wrapChar +
    				"No Wander:\t(" + (room.getNoWander() ? "X" : " ") + ")"+ Util.wrapChar + 
    				"Shop (" + ( room instanceof Shop ? "X" : " " ) + ")"+ Util.wrapChar;
    	for(Exit e : room.roomExits) {
    		message += "Exit " + e.getDirectionName() + " to " + e.getDestination()+ Util.wrapChar;
    		if(e.isDoor()) {
    			message += "    Door: " + (e.isOpen() ? "open" : "closed")+ Util.wrapChar;
    		}
    		if(e.isLockable()) {
    			message += "    Lockable: Locked: (" + ( e.isLocked() ? "X" : " " ) + ")  Key=" + e.getKey()+ Util.wrapChar;
    		}
    		message += "    No Wander (" + ( e.isNoWander() ? "X" : " " ) + ")"+ Util.wrapChar;
    		
    	}
    	player.tell(message);
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
                LinkedList<Room> r = TrollAttack.gameRooms;
                LinkedList<Mobile> m = TrollAttack.gameMobiles;
                LinkedList<Item> i = TrollAttack.gameItems;
                area.save((LinkedList<Room>)r, m, i);
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
                } else if(value.compareToIgnoreCase(Item.getItemType()) == 0 && (item.getType() != Item.getItemType())) {
                    newItem = new Item(item);
                } else {
					player.tell("'" + value + "' is not a known type, or the item is already of that type.\nChoose from: Weapon, Armor, Food, DrinkContainer, Fountain, Item");
					return;
                }
                player.tell("You set " + item.getShort() + "'s type.");
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
                    if(attr.compareToIgnoreCase("ac") == 0) {
                        armor.setArmorClass(Util.intize(player, value));
                    } else if(attr.startsWith("wear")) {
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
            if(attr.compareToIgnoreCase("reroll") == 0) {
            	Roll c = new Roll("3d6");
            	mobile.setStatistics(c.roll(), c.roll(), c.roll(), c.roll(), c.roll(), c.roll(), c.roll());
            } else if(attr.compareToIgnoreCase("hp") == 0) {
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
            } else if(attr.startsWith("str")) {
                mobile.strength = intValue;
            } else if(attr.startsWith("con")) {
                mobile.constitution = intValue;
            } else if(attr.startsWith("cha")) {
                mobile.charisma = intValue;
            } else if(attr.startsWith("dex")) {
                mobile.dexterity = intValue;
            } else if(attr.startsWith("int")) {
                mobile.intelligence = intValue;
            } else if(attr.startsWith("wis")) {
                mobile.wisdom = intValue;
            } else if(attr.compareToIgnoreCase("trainer") == 0) {
                if(value.length() < 4) {
                    mobile.canTeach = (!mobile.canTeach);
                } else {
                    mobile.canTeach = new Boolean(value).booleanValue();
                }
            } else if(attr.compareToIgnoreCase("wanderer") == 0) {
                if(mobile.getClass() == Mobile.class) {
                    if(value.length() < 4) {
                        ((Mobile)mobile).setWanderer(!((Mobile)mobile).isWanderer());
                    } else {
                        ((Mobile)mobile).setWanderer(new Boolean(value).booleanValue());
                    }
                } else {
                    player.tell("You can only change the 'wanderer' attribute of mobiles.");
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
