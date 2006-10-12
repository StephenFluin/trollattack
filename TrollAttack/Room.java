package TrollAttack;

import java.util.Iterator;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import TrollAttack.Items.Fountain;
import TrollAttack.Items.Item;

public class Room {
    public int vnum;

    //public Exit east, west , north, south, northEast, northWest, southEast,
    // southWest, up, down;
    private boolean noWander = false;
    public int noFloor = 0;
    public Push push = null;
    public int water = 0;

    public String description = "", title = "";

    public Vector<Item> roomItems = new Vector<Item>();

    public Vector<Being> roomBeings = new Vector<Being>();

    public Vector<Exit> roomExits = new Vector<Exit>();

    /**
     * A constructor that automatically generates the default title and 
     * description, and empty exit list.
     * @param vnum The vnum of the new room.
     */
    public Room(int vnum) {
    	this(vnum, "A Freshly Created Room",
	    	               "Change the title of this room by typing \"redit title <new title>\".   Enter the description of this room by typing \"redit desc <description>\".",
	    	               new java.util.Vector<Exit>());
    }
    
    public Room(int vnum, String title, String description, Vector<Exit> exits) {
        this.vnum = vnum;

        roomExits = exits;
        this.title = title;
        this.description = description;

        
        Iterator<Exit> eachExit = roomExits.iterator();
        while (eachExit.hasNext()) {
            Exit exit = eachExit.next();
            exit.setRoom(this);
        }
    }
    /**
     * Downcasts a shop to a room.
     * @param s A shop that we want to downcast.
     */
	public Room(Shop s) {
		this(s.vnum, s.title, s.description, s.roomExits);
		roomBeings = s.roomBeings;
		setNoWander(getNoWander());
		roomItems = s.roomItems;
		TrollAttack.replaceRoom(s, this);
	}
	
    public String toString() {
        String returnValue = vnum + ":" + title;
        int i = 0;
        for (Item currentItem : roomItems) {
            i++;
            returnValue += "\n\rroomItems[" + i + "](" + currentItem + ")"
                    + currentItem.getShort();
        }
        return returnValue;

    }

    public Node toNode(Document doc) {

        Node m = doc.createElement("room");
        Vector<Node> attribs = new Vector<Node>();
        attribs.add(Util.nCreate(doc, "vnum", vnum + ""));
        attribs.add(Util.nCreate(doc, "title", title));
        attribs.add(Util.nCreate(doc, "description", description + ""));
        if(getNoWander() ) {
        	attribs.add(Util.nCreate(doc, "nowander", getNoWander() ? "true" : "false"));        	
        }
        if(noFloor != 0) {
        	attribs.add(Util.nCreate(doc, "nofloor", noFloor + ""));
        }
        if(water > 0) {
        	attribs.add(Util.nCreate(doc, "water", water + ""));
        }
        if(push != null) {
        	attribs.add(push.toNode(doc));
        }
        Iterator<Exit> eachExit = roomExits.iterator();
        while (eachExit.hasNext()) {
            Exit exit = eachExit.next();
            attribs.add(exit.toNode(doc));
        }
        
        if(this instanceof Shop) {
        	attribs.add(((Shop)this).toShopNode(doc));
        }
        
        /*
         * Node itemList = doc.createElement("itemList");
         * 
         * for(int i = 0;i < playerItems.getLength();i++) {
         * itemList.appendChild(Util.nCreate(doc, "item", (
         * (Item)(playerItems.getNext()) ).vnum + "")); } attribs.add(itemList);
         */
        for(Being roomBeing : roomBeings ) {
            if (!roomBeing.isPlayer()) {
                Node mobile = doc.createElement("mobile");
                mobile.appendChild(Util.nCreate(doc, "vnum",
                        ((Mobile) (roomBeing)).vnum + ""));
                Being.addEquipmentNodes(doc, roomBeing, mobile);
                attribs.add(mobile);
            }
        }

        for (Item roomItem : roomItems) {
            attribs.add(Util.nCreate(doc, "item", roomItem.vnum + ""));
        }

        for (Node newAttrib : attribs) {
            m.appendChild(newAttrib);
        }

        return m;
    }

    public void freeze() {
        Vector<Item> newItemList = new Vector<Item>();
        for (Item i : roomItems) {
            newItemList.add(TrollAttack.getItem(i.vnum));
        }
        roomItems = newItemList;
        Vector<Being> newBeingList = new Vector<Being>();
        for (Being b : roomBeings) {
            if(!b.isPlayer()) {
                newBeingList.add(TrollAttack.getMobile(b.getVnum()));
            } else {
                newBeingList.add(b);
            }
        }
        roomBeings = newBeingList;
    }

    public void unfreeze() {
        // Rewrite this to be more efficient and use resets.
        Integer[] roomVnums = new Integer[roomItems.size()];
        for (int i = 0; i < roomItems.size(); i++) {
            roomVnums[i] = new Integer(((Item) roomItems.get(i)).vnum);
        }
        roomItems = new Vector();
        for (int i = 0; i < roomVnums.length; i++) {
            roomItems.add(new Item(TrollAttack.getItem(roomVnums[i])));
        }
        try {
            Integer[] mobileVnums = new Integer[roomBeings.size()];
            Vector<Being> roomMobiles = new Vector<Being>();
            int i = 0;
            for (Being tmp : roomBeings) {
                if (tmp.isPlayer()) {
                    roomMobiles.add(tmp);
                } else {
                    mobileVnums[i] = new Integer(((Mobile) tmp).vnum);
                }
            }
            roomBeings = roomMobiles;
            for (i = 0; i < mobileVnums.length; i++) {
                if (mobileVnums[i] != null) {
                    roomBeings.add((Mobile)TrollAttack
                            .getMobile(mobileVnums[i]).clone());
                }
                //TrollAttack.message("Recreating mobile..");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String look() {
        return look(null);
    }

    public String look(Being player) {
        String exits = "Exits: ";
        for(Exit exit : roomExits) {
            if (exits.length() > 7) {
                exits += ", ";
            }
            String exitString = Util.uppercaseFirst(exit.getDirectionName());
            if(exit.isDoor()) {
                if(exit.isOpen()) {
                    exitString = "<" + exitString + ">";
                } else {
                    exitString = "[" + exitString + "]";
                }
            }
            exits += exitString;
        }

        if (exits.length() <= 7) {
            exits += " none";
        }
        exits += ".";

        /**
         * Show Items get all of the items in the room, and write their
         * longdesc.
         */
        String objects = "";
        int n = 0;
        for(Item currentItem : roomItems) {
            String color;
            if (currentItem.getType() == Fountain.getItemType()) {
                color = Communication.BLUE;
            } else {
                color = Communication.GREEN;
            }
            if(player instanceof Player && ((Player)player).extraFormatting) {
            	color += "I:";
            }
            objects += color + currentItem.getLong();
            objects += Util.wrapChar;
            n++;

        }

        /**
         * Show Mobs get all mobs from the room, and write their longdesc (as
         * well as any players).
         */
        String mobiles = "";
        //TrollAttack.debug("Printing " + roomBeings.length() + " mobiles...");
        //boolean removeMe = false;
        Iterator<Being> eachBeing = roomBeings.iterator();
        while (eachBeing.hasNext()) {
            Being currentBeing = eachBeing.next();
            //TrollAttack.debug("Found mobile " + currentBeing.toString());
            if (currentBeing != player) {
            	if(player instanceof Player && ((Player)player).extraFormatting) {
                	mobiles += "M:";
                }
                mobiles += Communication.PURPLE + currentBeing.getLong();
                mobiles += Util.wrapChar;
            } else {
            }

        }
        String vnumString = "", extraFormatting = "";
        if(player instanceof Player) {
        	if(((Player)player).showVnum) {
        		vnumString = " <" + vnum + ">";
        	}
        	if(((Player)player).extraFormatting) {
        		extraFormatting = "<>";
        	}
        }
        
        String look = Communication.WHITE + extraFormatting + title + vnumString + extraFormatting + Util.wrapChar +
                    Communication.YELLOW + description + Util.wrapChar +
                    Communication.WHITE + exits + Util.wrapChar + 
                    objects +
                    mobiles;
        return look;
    }

    /**
     * Does a "say" in the room where the message is external to all beings in the room.
     */
    public void say(String s) {
        Being[] pBroadcast = {};
        say(s, pBroadcast);
    }

    /**
     * Does a "say" in the room where the message is external to all 
     * players except the single player listed.  For the single player listed, 
     * @param s Message to be said.
     * @param ignoreSinglePlayer The player for which this "say" will be first person.
     */
    public void say(String s, Being ignoreSinglePlayer) {
        Being[] pBroadcast = { ignoreSinglePlayer };
        say(s, pBroadcast);
    }

    /**
     * The say function works by replacing %1 with being 1 in the list and 
     * then saying each message from the perspective of the recipient being.
     */
    public void say(String s, Being[] players) {
        try {
            for (Being person : roomBeings) {
                //TrollAttack.debug("Displaying mesage for: " + person.getShort());
                String message = s;
                message = Util.replaceBeings(message, players, person);

                //TrollAttack.debug("Message after replaces: " + message);
                if(players.length > 0 && person == players[0]) {
                    //Ignore this person entirely.
                } else {
                    person.interrupt(Util.uppercaseFirst(message));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String setLink(int direction, int destination) {
        if (destination == 0) {
            Exit removeExit = null;
            for(Exit exit : roomExits) {
                if (exit.getDirection() == direction) {
                    removeExit = exit;
                    break;
                }
            }
            if(removeExit != null) {
                roomExits.remove(removeExit);
                return "Exit removed successfully";
            }
            return "Did not find an exit to remove.";
        } else {
            for(Exit exit : roomExits) {
                if(exit.getDirection() == direction){
                    return "Exit already exists in this direction.";
                }
            }
            roomExits.add(new Exit(destination, direction));
            return "Exit added successfully.";
        }
    }

    public int countExactItem(Item item) {
        int count = 0;
        //TrollAttack.message("There are " + roomItems.getLength() + " items in
        // the room." + vnum);
        for(Item currentItem : roomItems) {
            //TrollAttack.message("Looking at " + currentItem.getShort() + "
            // for comparison.");
            if (currentItem == item) {
                count++;
            }
        }
        return count;
    }

    public int countExactMobile(Mobile mobile) {
        int count = 0;
        for(Being currentMobile : roomBeings) {
            if (currentMobile == mobile) {
                count++;
            }
        }
        return count;
    }

    public Item getItem(String name) {
        return getItem(name, false);
    }

    public Item removeItem(String name) {
        return getItem(name, true);
    }
    public Item getItem(String name, boolean remove) {
    	return getItem(name, remove, null);
    }
    public Item getItem(String name, java.lang.Class objectClass) {
    	return getItem(name, false, objectClass);
    }
    
    public Item getItem(String name, boolean remove, java.lang.Class objectClass) {
    	//TrollAttack.debug("looking for a " + name);
        Item newItem = null;        
        for(Item currentItem : roomItems) {
            if (Util.contains(currentItem.name, name)) {
                if(objectClass!= null) {
                	if(objectClass.isInstance(currentItem)) {
                		newItem = currentItem;
                	}
                } else {
                	newItem = currentItem;
                	break;
                }
                
            }
        }
        if(remove && newItem != null) {
            roomItems.remove(newItem);
        }
        return newItem;
    }

    public Being getBeing(String name, boolean remove, Being actor) {

        Being newMobile;
        //TrollAttack.debug("About to search through " + roomBeings.length() +
        // " items in room " + vnum);
        for(Being currentBeing : roomBeings) {

            if (currentBeing != actor
                    && Util.contains(currentBeing.getName(), name)) {
                newMobile = (Being) currentBeing;

                if (remove == true) {
                    roomBeings.remove(newMobile);
                }
                return newMobile;
            } else {
            }
        }
        return null;
    }

    public Being getBeing(String name, Being actor) {
        if (name.compareToIgnoreCase("self") == 0) {
            return actor;
        }
        return this.getBeing(name, false, actor);
    }

    public Being removeBeing(String name) {
        return this.getBeing(name, true, null);
    }

    public Being removeBeing(Being being) {
        for(Being currentBeing : roomBeings ) {
            if (currentBeing == being) {
                roomBeings.remove(being);
                return being;
            }
        }
        return null;
    }

    public void addBeing(Being being) {
        roomBeings.add(being);
    }

    /*
     * public void addMobile( Mobile m) { roomBeings.add(m); }
     */
    public void addItem(Item i) {
        roomItems.add(i);
    }

    public void setTitle(String newTitle) {
        title = newTitle;
    }

    public void setDescription(String newDesc) {
        description = newDesc;
    }

    public void healBeings() {
        int strength;
        for(Being currentBeing : roomBeings) {
            strength = 1 + currentBeing.getState();
            if (currentBeing.thirst > 8) {
                strength = strength / 2;
            }
            if (currentBeing.hunger > 8) {
                strength = strength / 2;
            }
            //TrollAttack.message("Increasing health of " +
            // currentBeing.getShort() + " by " + strength + ".");
            currentBeing.increaseHitPoints(strength);
            currentBeing.increaseManaPoints(strength);
            currentBeing.increaseMovePoints(strength);
        }
    }

    public void replaceItem(Item find, Item replace) {
        if (find == replace) {
            TrollAttack
                    .error("Can't replace with self, otherwise infinite loop!");
        }
        Item foundItem = null;
        for(Item item : roomItems) {
            if (item == find) {
                foundItem = item;
                break;
            }
        }
        if(foundItem != null) {
            roomItems.remove(find);
            roomItems.add(replace);
        }
        
    }

    public Room getLink(int direction) {
        Exit exit = getExit(direction);
        if(exit != null) {
            return exit.getDestinationRoom();
        }
        return null;
    }
    public Exit getExit(int direction) {
        for(Exit exit : roomExits) {
            if(exit.getDirection() == direction) {
                return exit;
            }
        }
        return null;
    }

    public Vector<Being> getRoomBeings() {
        return roomBeings;
    }

	public boolean getNoWander() {
		return noWander;
	}
	public void setNoWander(boolean canWanderIntoThisRoom) {
		this.noWander = canWanderIntoThisRoom;
	}

    // Makes sure that mobiles don't leave their area, or enter a nowander room.
	public Vector<Exit> getWanderableExits() {
		Vector<Exit> exitList = new Vector<Exit>();
		Area thisArea = Area.testRoom(this, TrollAttack.gameAreas);
		for(Exit e : roomExits) {
			if(Area.test(e.getDestination(), TrollAttack.gameAreas) == thisArea && !e.isNoWander() && !e.getDestinationRoom().getNoWander()) {
				exitList.add(e);
			}
		}
		return exitList;
	}

}
