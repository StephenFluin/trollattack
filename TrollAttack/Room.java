package TrollAttack;

import java.util.Iterator;
import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import TrollAttack.Items.Fountain;
import TrollAttack.Items.Item;

public class Room {
    public int vnum;

    //public Exit east, west , north, south, northEast, northWest, southEast,
    // southWest, up, down;
    private boolean noWander = false;

    public String description = "", title = "";

    public LinkedList<Item> roomItems = new LinkedList<Item>();

    public LinkedList<Being> roomBeings = new LinkedList<Being>();

    public LinkedList<Exit> roomExits = new LinkedList<Exit>();

    
    public Room(int vnum, String title, String description, LinkedList<Exit> exits) {
        this.vnum = vnum;
        //this.east = east; this.west = west; this.north = north; this.south =
        // south;
        //this.northEast = northEast; this.northWest = northWest;
        // this.southEast = southEast; this.southWest = southWest;
        //this.up = up; this.down = down;
        roomExits = exits;
        this.title = title;
        this.description = description;
        // TrollAttack.message("Creating room with up and down: " + up + " and "
        // + down + ".");
        Iterator<Exit> eachExit = roomExits.iterator();
        while (eachExit.hasNext()) {
            Exit exit = eachExit.next();
            exit.setRoom(this);
        }
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
        LinkedList<Node> attribs = new LinkedList<Node>();
        attribs.add(Util.nCreate(doc, "vnum", vnum + ""));
        attribs.add(Util.nCreate(doc, "title", title));
        attribs.add(Util.nCreate(doc, "description", description + ""));
        if(getNoWander() ) {
        	attribs.add(Util.nCreate(doc, "nowander", getNoWander() ? "true" : "false"));        	
        }
        Iterator<Exit> eachExit = roomExits.iterator();
        while (eachExit.hasNext()) {
            Exit exit = eachExit.next();
            attribs.add(exit.toNode(doc));
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
        LinkedList<Item> newItemList = new LinkedList<Item>();
        for (Item i : roomItems) {
            newItemList.add(TrollAttack.getItem(i.vnum));
        }
        roomItems = newItemList;
        LinkedList<Being> newBeingList = new LinkedList<Being>();
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
        roomItems = new LinkedList();
        for (int i = 0; i < roomVnums.length; i++) {
            roomItems.add(new Item(TrollAttack.getItem(roomVnums[i])));
        }
        try {
            Integer[] mobileVnums = new Integer[roomBeings.size()];
            LinkedList<Being> roomMobiles = new LinkedList<Being>();
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
                mobiles += Communication.PURPLE + currentBeing.getLong();
                mobiles += Util.wrapChar;
            } else {
            }

        }
        String look = Communication.WHITE + title + Util.wrapChar +
                    Communication.YELLOW + description + Util.wrapChar +
                    Communication.WHITE + exits + Util.wrapChar + 
                    objects +
                    mobiles;
        return look;
    }

    /*
     * public void addPlayer(Player player) { roomBeings.add(player); } public
     * void removePlayer(Player player) { roomBeings.delete(player); }
     */
    public void say(String s) {
        Being[] pBroadcast = {};
        say(s, pBroadcast);
    }

    public void say(String s, Being ignoreSinglePlayer) {
        Being[] pBroadcast = { ignoreSinglePlayer };
        say(s, pBroadcast);
    }

    // players works like this:
    // 1 is ignored
    // 1 is %1
    // 2 is %2 ...
    // Any player that matches who they are talking to
    // is replaced with you.
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

    /*public String open(int direction) {
        while (roomExits.itemsRemain()) {
            Exit exit = (Exit) roomExits.getNext();
            if (exit.getDirection() == direction) {
                roomExits.reset();

                return exit.open();
            } else {
                //TrollAttack.message(direction + "!=" +
                // exit.getDirectionName());
            }
        }
        roomExits.reset();
        return "You can't find that door.";
    }

    public String close(int direction) {
        while (roomExits.itemsRemain()) {
            Exit exit = (Exit) roomExits.getNext();
            if (exit.getDirection() == direction) {
                roomExits.reset();
                return exit.close();
            }
        }
        roomExits.reset();
        return "You can't find that door.";
    }*/

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

    public Item findItem(String name) {
        return getItem(name);
    }

    public Item removeItem(String name) {
        return getItem(name, true);
    }

    public Item getItem(String name, boolean remove) {
        Item newItem = null;        
        for(Item currentItem : roomItems) {
            if (Util.contains(currentItem.name, name)) {
                newItem = currentItem;
                break;
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

    public LinkedList<Being> getRoomBeings() {
        return roomBeings;
    }

	public boolean getNoWander() {
		return noWander;
	}
	public void setNoWander(boolean canWanderIntoThisRoom) {
		this.noWander = canWanderIntoThisRoom;
	}

    // Makes sure that mobiles don't leave their area, or enter a nowander room.
	public LinkedList<Exit> getWanderableExits() {
		LinkedList<Exit> exitList = new LinkedList<Exit>();
		Area thisArea = Area.testRoom(this, TrollAttack.gameAreas);
		for(Exit e : roomExits) {
			if(Area.test(e.getDestination(), TrollAttack.gameAreas) == thisArea && !e.isNoWander()) {
				exitList.add(e);
			}
		}
		return exitList;
	}

}
