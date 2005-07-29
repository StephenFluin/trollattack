package TrollAttack;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import TrollAttack.Items.Item;

public class Room {
	public int vnum;
    //public Exit east, west , north, south, northEast, northWest, southEast, southWest, up, down;
    
	public String description = "", title = "";
	private LinkedList roomItems = new LinkedList();
	public LinkedList roomBeings = new LinkedList();
	public LinkedList roomExits = new LinkedList();
	
	public Room(int vnum, String title, String description, LinkedList exits) {
	 this.vnum = vnum; 
	 //this.east = east; this.west = west; this.north = north; this.south = south; 
	 //this.northEast = northEast; this.northWest = northWest; this.southEast = southEast; this.southWest = southWest;
	 //this.up = up; this.down = down;
	 roomExits = exits;
	 this.title = title;
	 this.description = description;
	// TrollAttack.message("Creating room with up and down: " + up + " and " + down + ".");
	 while(roomExits.itemsRemain()) {
	     Exit exit = (Exit)roomExits.getNext();
	     exit.setRoom(this);
	 }
	 roomExits.reset();
	}
	
	public String toString() {
		String returnValue = vnum + ":" + 
		title;
		for(int i = 0; i < roomItems.length();i++) {
		    Item currentItem = (Item)roomItems.getNext();
	        returnValue += "\n\rroomItems[" + i + "](" + currentItem + ")" + currentItem.getShort();  
		}
		roomItems.reset();
		return returnValue;
					
	}
	public Node toNode(Document doc) {
	   
	    Node m = doc.createElement("room");
	    LinkedList attribs = new LinkedList();
	    attribs.add(Util.nCreate(doc, "vnum", vnum + ""));
	    attribs.add(Util.nCreate(doc, "description", description + ""));
	    attribs.add(Util.nCreate(doc, "title", title));
	    while(roomExits.itemsRemain()) {
	        Exit exit = (Exit)roomExits.getNext();
	        Node n = doc.createElement(exit.getDirectionName());
	        Node ex = Util.nCreate(doc, "vnum", exit.getDestination() + "");
	        n.appendChild(ex);
	        if(exit.isDoor()) n.appendChild(Util.nCreate(doc, "door", exit.isOpen() ? "open" : "closed"));
	        if(exit.isLockable()) n.appendChild(Util.nCreate(doc, "lockable", exit.getKey().vnum + ""));
	        attribs.add(n);
	    }
	    roomExits.reset();
	    /*Node itemList = doc.createElement("itemList");
	    
	    for(int i = 0;i < playerItems.getLength();i++) {
	       itemList.appendChild(Util.nCreate(doc, "item", ( (Item)(playerItems.getNext()) ).vnum + ""));
	    }
	    attribs.add(itemList);*/
	    Being roomBeing;
	    for(int i = 0;i < roomBeings.getLength();i++) {
		    roomBeing = (Being)roomBeings.getNext();
		    if(!roomBeing.isPlayer()) {
		        attribs.add(Util.nCreate(doc, "mobile", ((Mobile)(roomBeing)).vnum + ""));
		    }
		}
	    roomBeings.reset();
	    
	    Item roomItem;
	    for(int i = 0;i < roomItems.getLength();i++) {
	        roomItem = (Item)roomItems.getNext();
	        attribs.add(Util.nCreate(doc, "item", roomItem.vnum + ""));
	    }
	    roomItems.reset();
	    
	    for(int i = 0; i < attribs.length(); i++) {
	        
	        Node newAttrib = (Node)attribs.getNext();
	        m.appendChild(newAttrib);
	    }
	    
	    return m;
	}
	public void freeze() {
	    Integer[] itemVnums = new Integer[roomItems.length()];
	    for(int i = 0; i < roomItems.length();i++) {
	        itemVnums[i] = new Integer(((Item)roomItems.getNext()).vnum);
	    }
	    roomItems = new LinkedList();
	    for(int i = 0; i < itemVnums.length;i++) {
	        roomItems.add(TrollAttack.getItem(itemVnums[i]));
	        //TrollAttack.message("Recreating item..");
	        
	    }
	    try{
		    Integer[] mobileVnums = new Integer[roomBeings.length()];
		    LinkedList roomMobiles = new LinkedList();
		    for(int i = 0; i < roomBeings.length();i++) {
		        Being tmp = (Being)roomBeings.getNext();
		        if(tmp.isPlayer()) {
		            roomMobiles.add(tmp);
		        } else {
		            mobileVnums[i] = new Integer(((Mobile)tmp).vnum);
		        }
		    }
		    roomBeings.reset();
		    roomBeings = roomMobiles;
		    for(int i = 0; i < mobileVnums.length;i++) {
		        if(mobileVnums[i] != null) {
		            roomBeings.add(TrollAttack.getMobile(mobileVnums[i]));
		        }
		        //TrollAttack.message("Recreating mobile..");
		        
		    }
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	    
	}
	public void unfreeze() {
	    Integer[] roomVnums = new Integer[roomItems.length()];
	    for(int i = 0; i < roomItems.length();i++) {
	        roomVnums[i] = new Integer(((Item)roomItems.getNext()).vnum);
	    }
	    roomItems = new LinkedList();
	    for(int i = 0; i < roomVnums.length;i++) {
	        roomItems.add(new Item(TrollAttack.getItem(roomVnums[i])));
	    }
	    try{
		    Integer[] mobileVnums = new Integer[roomBeings.length()];
		    LinkedList roomMobiles = new LinkedList();
		    for(int i = 0; i < roomBeings.length();i++) {
		        Being tmp = (Being)roomBeings.getNext();
		        if(tmp.isPlayer()) {
		            roomMobiles.add(tmp);
		        } else {
		            mobileVnums[i] = new Integer(((Mobile)tmp).vnum);
		        }
		    }
		    roomBeings.reset();
		    roomBeings = roomMobiles;
		    for(int i = 0; i < mobileVnums.length;i++) {
		        if(mobileVnums[i] != null) {
		            roomBeings.add(new Mobile(TrollAttack.getMobile(mobileVnums[i])));
		        }
		        //TrollAttack.message("Recreating mobile..");
		        
		    }
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public String[] look() {
	    return look(null);
	}
	public String[] look(Player player) {
		String exits = "Exits: ";
		while(roomExits.itemsRemain()) {
		    if(exits.length() > 7) {
		        exits += ", ";
		    }
		    Exit exit = (Exit)roomExits.getNext();
		    exits += Util.uppercaseFirst(exit.getDirectionName());
		}
		roomExits.reset();
		
		if(exits.length() <= 7 ) {
			exits += " none";
		}
		exits += ".";
		
		/** 
		 * Show Items
		 * get all of the items in the room, and write their longdesc.
		 */
		String[] objects = new String[roomItems.length()];
		int n = 0;
		while(roomItems.itemsRemain()) {
			//TrollAttack.error("About to get room " + i + " of " + roomItems[i]);
			Item currentItem = (Item)roomItems.getNext();
			objects[n] = Communication.GREEN + currentItem.getLong();
			n++;
			
		}
		roomItems.reset();
		
		/**
		 * Show Mobs
		 * get all mobs from the room, and write their longdesc (as well as any players).
		 */
		String[] mobiles = new String[roomBeings.length() - 1 ];
		// TrollAttack.error("Printing mobiles...");
		int m = 0;
		Being currentBeing = (Being)roomBeings.getNext();
		while(currentBeing != null) {
		    //TrollAttack.error("Found mobile " + roomMobiles[i].vnum + " at position " + m + ", also known as " + roomMobiles[i].getLong());
			if(currentBeing != player) {
			    mobiles[m] = Communication.PURPLE + currentBeing.getLong();
			
				//TrollAttack.error("Adding "+ mobiles[i] + " to print queue.");
				m++;
			}
			currentBeing = (Being)roomBeings.getNext();
			
		}
		roomBeings.reset();
		String[] firsts = { Communication.WHITE + title , Communication.YELLOW + description , Communication.WHITE + exits };
		//TrollAttack.error("3 firsts, " + i + " objects.");
		String[] myReturn = new String[n + m + firsts.length];
		//TrollAttack.error(firsts.length + " title + desc lines, " + n + " item lines, " + m + " mob lines.");
		System.arraycopy(firsts, 0, myReturn, 0, firsts.length);
		System.arraycopy(objects, 0, myReturn, firsts.length, n);
		System.arraycopy(mobiles, 0, myReturn, firsts.length + n, m);
		return myReturn;
	}
	public void addPlayer(Player player) {
	    roomBeings.add(player);
	}
	public void removePlayer(Player player) {
	    roomBeings.delete(player);
	}
	public void say(String s) {
	    Being[] pBroadcast = {};
	    say(s, pBroadcast);
	}
	public void say(String s, Player ignoreSinglePlayer) {
	    Being[] pBroadcast = {ignoreSinglePlayer, ignoreSinglePlayer};
	    say(s, pBroadcast);
	}
	// players works like this:
	// 0 is ignored
	// 1 is %1
	// 2 is %2 ...
	// Any player that matches who they are talking to
	// is replaced with you.
	public void say(String s, Being[] players) {
	    Being person;
	    try{
	        for(int i = 1;i <= roomBeings.length(); i++ ) {
	           // TrollAttack.message("Found a being...");
		        person = (Being)roomBeings.getNext();
		        String message = s;
		        if(players.length < 1 || person != players[0]) {
		            
		            //TrollAttack.message("telling " + person.getShort() + " " + s);
		            for(int j = 1;j < players.length;j++) {
		                //TrollAttack.message(players[j] == null ? "players[j] is null" : "players[j] is not null");
		                message = message.replaceAll("%" + j , players[j].getShort(person));
		            }
		            person.tell(Util.uppercaseFirst(message));
		            
		        }
	        }
	    } catch(Exception e) {
	        e.printStackTrace();
	    } finally {
	        roomBeings.reset();
	    }
	}
	public Exit followLink (int direction) {
		while(roomExits.itemsRemain()) {
		    Exit exit = (Exit)roomExits.getNext();
		    if(exit.getDirection() == direction) {
		        roomExits.reset();
		        return exit;
		    }
		}
		roomExits.reset();
		return null;
	}
	public String open(int direction) {
	    while(roomExits.itemsRemain()) {
	        Exit exit = (Exit)roomExits.getNext();
	        if(exit.getDirection() == direction) {
	            roomExits.reset();
	            
	            return exit.open();
	        } else {
	            //TrollAttack.message(direction + "!=" + exit.getDirectionName());
	        }
	    }
	    roomExits.reset();
	    return "You can't find that door.";
	}
	public String close(int direction) {
	    while(roomExits.itemsRemain()) {
	        Exit exit = (Exit)roomExits.getNext();
	        if(exit.getDirection() == direction) {
	            roomExits.reset();
	            return exit.close();
	        }
	    }
	    roomExits.reset();
	    return "You can't find that door.";
	}
	public void setLink( int direction, int destination) {
		if(destination == 0) {
		    while(roomExits.itemsRemain()) {
		        Exit exit = (Exit)roomExits.getNext();
		        if(exit.getDirection() == direction) {
		            roomExits.delete(exit);
		        }
		    }
		} else {
		    roomExits.add(new Exit(destination, direction));
		}
		roomExits.reset();
	}
	public int countExactItem(Item item) {
	    int count = 0;
	    //TrollAttack.message("There are " + roomItems.getLength() + " items in the room." + vnum);
	    while(roomItems.itemsRemain()) {
	        
	        Item currentItem = (Item)roomItems.getNext();
	        //TrollAttack.message("Looking at " + currentItem.getShort() + " for comparison.");
	        if(currentItem == item) {
	            count++;
	        }
	    }
	    roomItems.reset();
	    return count;
	}
	public int countExactMobile(Mobile mobile) {
	    int count = 0;
	    while(roomBeings.itemsRemain()) {
	        Being currentMobile = (Being)roomBeings.getNext();
	        if(currentMobile == mobile) {
	            count++;
	        }
	    }
	    roomBeings.reset();
	    return count;
	}
	public Item getItem(String name) {
	    return getItem(name, false);
	}
	public Item removeItem(String name) {
	    return getItem(name, true);
	}
	public Item getItem(String name, boolean remove) {
		Item newItem;
		while(roomItems.itemsRemain()) {
			Item currentItem = (Item)roomItems.getNext();
		    if(Util.contains(currentItem.name, name)) {
				newItem = currentItem;
				if(remove) {
				    roomItems.delete(currentItem);
				}
				roomItems.reset();
				return newItem;
			}
		}
		roomItems.reset();
		return null; 
	}
	public Being getBeing(String name, boolean remove, Player ignorePlayer) {
	    Being newMobile;
		for(int i = 0; i < roomBeings.getLength();i++) {
		    Being currentBeing = (Being)roomBeings.getNext();
		
		    if(currentBeing != ignorePlayer && Util.contains(currentBeing.getName(), name)) {
		        newMobile = (Being)currentBeing;
		        if(remove == true) {
		            roomBeings.delete(newMobile);
		        }
		        roomBeings.reset();
		        return newMobile;
		    }
		}
		roomBeings.reset();
		return null;
	}
	public Being getBeing(String name, Player player) {
		return this.getBeing(name, false, player);
	}
	public Being removeBeing(String name) {
		return this.getBeing(name, true, null);
	}
	public Being removeBeing(Being being) {
	    Being currentBeing = null;
	    for(int i = 0; i < roomBeings.length();i++) {
	        currentBeing = (Being)roomBeings.getNext();
	        if(currentBeing == being) {
	            roomBeings.delete(being);
	            return being;
	        }
	    }
	    return null;
	}
	public void addMobile( Mobile m) {
		roomBeings.add(m);
	}
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
	    Mobile m;
	    Being currentBeing = (Being)roomBeings.getNext();
	    int strength = 1;
	    while(currentBeing != null ) {
	       // TrollAttack.message("Healing " + currentBeing.getShort() + ".");
	       currentBeing.increaseHitPoints(strength + currentBeing.getState());
           currentBeing.increaseManaPoints(strength + currentBeing.getState());
           currentBeing.increaseMovePoints(strength + currentBeing.getState());
	        currentBeing = (Being)roomBeings.getNext();
	    }
	    roomBeings.reset();
	}
	
	public void replaceItem(Item find, Item replace) {
		if( find == replace ) {
		    TrollAttack.error("Can't replace with self, otherwise infinite loop!");
		}
	    while( roomItems.itemsRemain() ) {
		    if(roomItems.getNext() == find) {
		        roomItems.delete(find);
		        roomItems.add(replace);
		    }
		}
	    roomItems.reset();
	}

}
