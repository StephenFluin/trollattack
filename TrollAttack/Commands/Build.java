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
import java.util.Vector;
import java.io.File;

import TrollAttack.Items.*;

/**
 * @author PeEll
 *
 * This class attempts to encapsulate all of the builder functions into one file to decrease the size of CommandHandler.
 * Sadly it currently does not decrease the size of CommandHandler because right now each command requires an entry in both.
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
        if(s.startsWith("nof")) {
        	if(parts.length != 2) {
        		player.tell("Usage: redit nofloor <damage>");
        		return;
        	}
        	int noFloor = player.getActualRoom().noFloor;
        	if(noFloor != 0) {
        		player.getActualRoom().noFloor = 0;
        		player.tell("You prevent beings from falling through the floor.");
        	} else {
        		player.getActualRoom().noFloor = Util.intize(player, parts[1]);
        		player.tell("You remove the floor from the room, allowing creatures to fall to the room below causing " + player.getActualRoom().noFloor + " damage.");
        	}
        	return;
        }
        if(s.startsWith("pus")) {
        	if(parts.length == 1 && player.getActualRoom().push != null) {
        		player.tell("You eliminate the pushing forces from this room.");
        		player.getActualRoom().push = null;
        		return;
        	} else if(parts.length >= 4) {
        		String message = parts[3];
        		for(int i = 4;i<parts.length;i++) {
        			message += " " + parts[i];
        		}
        		int dir = Exit.getDirection(parts[1]);
        		int wait = Util.intize(parts[2]);
        		
        		Push p = new Push(dir , wait, message);
        		player.getActualRoom().push = p;
        		player.tell("You create a pushing force that will send beings to the " + Exit.directionName(dir) + " after " + wait/1000 + " seconds.");
        	} else {
        		player.tell("Usage: redit push <direction> <wait> <message>");
        	}
        }
       
        String command;
        if(parts.length > 1) {
        	command = parts[1];
        } else {
        	command = "";
        }
        
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
            if(Util.contains(s, "nowander")) {
            	exit.setNoWander(!exit.isNoWander());
            	String message = "";
            	if(exit.isNoWander()) {
            		message = "Mobiles can no longer wander to the " + exit.getDirectionName() + ".";
            	} else {
            		message = "Mobiles can once again wander to the " + exit.getDirectionName() + ".";
            	}
            	if(reciprocol) {
            		bexit.setNoWander(exit.isNoWander());
            		if(bexit.isNoWander()) {
                		message += Util.wrapChar + "Mobiles can no longer wander from the " + exit.getDirectionName() + ".";
                	} else {
                		message += "Mobiles can once again wander from the " + exit.getDirectionName() + ".";
                	}
            	}
            	player.tell(message);
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
    			Room r = player.getActualRoom();
    			Shop s = new Shop(player.getActualRoom());
    			TrollAttack.replaceRoom(player.getActualRoom(), s);

    			return;
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
        		Room r = new Room(s);
        		player.tell("You turn this shop back into a normal room!");
        		TrollAttack.replaceRoom(player.getActualRoom(), r);
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
    				"Shop  (" + ( room instanceof Shop ? "X" : " " ) + ")"+ Util.wrapChar +
    				"NoFoor(" + ( room.noFloor != 0 ? ("X) Damage:" + room.noFloor) : " )" ) + Util.wrapChar +
    				"Push: " + ( (room.push == null ? "none" : Exit.directionName(room.push.direction) ) ) + Util.wrapChar;
    	
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
        player.tell("Cost:\t" + item.getCost() + "\t" + "Weight:\t" + item.getWeight());
        player.tell(item.getTypeData());
    }
    public void aStat(Area area) {
        player.tell("Information about:" + area.name);
        player.tell("Name:\t" + area.name);
        player.tell("Filename:\t" + area.filename);
        player.tell("Vnums:\t" + area.low + "-" + area.high);
        player.tell("Frozen:\t(" + (area.frozen ? "X" : " " ));
    }
    public void aSet(String command) {
        String[] parts = Util.split(command);
        if(parts.length < 2) {
            player.tell("Usage: aset name|filename|low|high value");
            return;
        }
        Area area = player.getArea();
        String value = Util.getRestOfCommand(parts);
        if(command.startsWith("name")) {
            area.name = value;
            player.tell("Area name changed to '" + area.name + "'");
        } else if(command.startsWith("filename")) {
            if(parts[1].length() < 3) {
                player.tell("Area filename must be at least 3 characters long.");
            } else {
                if(!parts[1].endsWith(".xml")) {
                    parts[1] += ".xml";
                }
                
                // Check to make sure that an area of that filename doesn't exist.
                for(Area a : TrollAttack.gameAreas) {
                	if(a.filename.equals(parts[1])) {
                		player.tell("That filename already exists, check \"alist\"!");
                		return;
                	}
                }
                
                String oldFilename = area.filename;
                area.filename = parts[1];
                Vector<Room> r = TrollAttack.gameRooms;
                Vector<Mobile> m = TrollAttack.gameMobiles;
                Vector<Item> i = TrollAttack.gameItems;
                area.save(r, m, i);
                
                TrollAttack.message("Renaming area from " + oldFilename + " to " + area.filename + ".");
                File oldFile = new File("Areas/" + oldFilename);
                if(!oldFile.canWrite()) {
                	TrollAttack.error("Don't have write ability for " + oldFilename + ", meaning we can't delete it.");
                }
                oldFile.delete();
                
                
                player.tell("Area filename changed to '" + parts[1] + "'");
            }
        } else if(command.startsWith("low")) {
            area.low = Util.intize(player, value);
            player.tell("Area range changed to " + area.low + "-" + area.high + ".");
        } else if(command.startsWith("high")) {
            area.high = Util.intize(player, value);
            player.tell("Area range changed to " + area.low + "-" + area.high + ".");
        } else if(command.startsWith("delete")) {
        	area.delete();
        	player.tell("The area that once was, is no more!");
        	TrollAttack.message("Area deletion: " + area.filename);
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
    public String area() {
    	if(player.getArea() == null) {
    		return "You have no area!";
    	}
        return "Your area " + (player.getArea().frozen ? "<frozen>" : "") + ": " + player.getArea().filename + Util.wrapChar + player.getArea().name + Util.wrapChar +
        "Vnum range:\t" + player.getArea().low + "-" + player.getArea().high;
        
    }
    public void iSet(String[] parts) {
       /* This method is going to get sticky because of ambiguity when 
    	* you edit an item. Are you editing the original, or are you editing 
    	* just your instance, or all instances of this item?
    	* 
    	* It should edit all items with this vnum because attributes you can 
    	* change with iset are universal for that item.
    	*/
    	Item item = null;
        try {
           item = (Item)player.getActualRoom().getItem(parts[0]);
           item = TrollAttack.getItem(item.vnum);
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
                item.setName(value);
            } else if(attr.compareToIgnoreCase("short") == 0) {
                item.setShort(value);
            } else if(attr.compareToIgnoreCase("long") == 0) {
                item.setLong(value);
            } else if(attr.compareToIgnoreCase("weight") == 0 ) {
                item.setWeight(intValue);
            } else if(attr.compareToIgnoreCase("cost") == 0 ) {
                item.setCost(intValue);
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
                } else if(value.equalsIgnoreCase(Container.getItemType()) && (item.getType() != Container.getItemType())) {
                	newItem = new Container(item);
                } else {
					player.tell("'" + value + "' is not a known type, or the item is already of that type.\nChoose from: Weapon, Armor, Food, DrinkContainer, Fountain, Item, Container");
					return;
                }
                player.tell("You set " + item.getShort() + "'s type.");
                player.getActualRoom().replaceItem(item, newItem);
                TrollAttack.replaceItem(item, newItem);
            } else {
            	// This section provides additional options for each item type.
            	
                if(item.getType().compareToIgnoreCase(Weapon.getItemType())== 0) {
                    Weapon weapon = (Weapon)item;
                    if(attr.compareToIgnoreCase("damage") == 0) {
		                weapon.setDamage(value);
		            }
                    return;
                } else if(item.getType().compareToIgnoreCase(Armor.getItemType()) == 0) {
                    Armor armor = (Armor)item;
                    if(attr.compareToIgnoreCase("ac") == 0 || attr.compareToIgnoreCase("armorclass") == 0) {
                        armor.setArmorClass(Util.intize(player, value));
                        player.tell("You set " + armor.getShort() + "'s armor class to " + value + ".");
                    } else if(attr.startsWith("wear")) {
                        armor.setWearLocation(value);
                        player.tell("You set " + armor.getShort() + "'s wear location to " + value + ".");
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
                } else if(item.getType().equalsIgnoreCase(Container.getItemType())) {
                	Container con = (Container)item;
                	if(attr.compareToIgnoreCase("capacity") == 0) {
                		con.setCapacity(Util.intize(player, value));
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
                if(intValue > 50 && mobile instanceof Player && !(player.level >64)) {
                	player.tell("You can't advance players to beyond level 50.");
                	return;
                } else {
                	mobile.level = intValue;
                }
                
            	
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
                    return;
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
