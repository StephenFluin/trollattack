package TrollAttack;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import TrollAttack.Commands.CommandMove;

public class Room {
	public int vnum, east, west , north, south, northEast, northWest, southEast, southWest, up, down;
	public String description = "", title = "";
	private LinkedList roomItems = new LinkedList();
	public LinkedList roomBeings = new LinkedList();
	
	
	public Room(int vnum, String title, String description, int east , int west, int north, int south, int  northEast, int  northWest, int  southEast, int  southWest, int up, int down) {
	 this.vnum = vnum; this.east = east; this.west = west; this.north = north; this.south = south; 
	 this.northEast = northEast; this.northWest = northWest; this.southEast = southEast; this.southWest = southWest;
	 this.up = up; this.down = down;
	 this.title = title;
	 this.description = description;
	// TrollAttack.message("Creating room with up and down: " + up + " and " + down + ".");
	}
	
	public String toString() {
		String returnValue = vnum + ":" + 
		east + "," +
		west + "," +
		north + "," +
		south + "," +
		northEast + "," +
		northWest + "," +
		southEast + "," +
		southWest + "," +
		up + "," +
		down;
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
	    if(east != 0) attribs.add(Util.nCreate(doc, "east", east + ""));
	    if(west != 0) attribs.add(Util.nCreate(doc, "west", west + ""));
	    if(north != 0) attribs.add(Util.nCreate(doc, "north", north + ""));
	    if(south != 0) attribs.add(Util.nCreate(doc, "south", south + ""));
	    if(up != 0) attribs.add(Util.nCreate(doc, "up", up + ""));
	    if(down != 0) attribs.add(Util.nCreate(doc, "down", down + ""));
	    if(northEast != 0) attribs.add(Util.nCreate(doc, "northeast", northEast + ""));
	    if(northWest != 0) attribs.add(Util.nCreate(doc, "northwest", northWest + ""));
	    if(southEast != 0) attribs.add(Util.nCreate(doc, "southeast", southEast + ""));
	    if(southWest != 0) attribs.add(Util.nCreate(doc, "southwest", southWest + ""));
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
		if(east != 0 ) {
			exits += "East";
		}
		if(north != 0 ) {
			if(exits.length() > 7) {
				exits += ", ";
			}
			exits += "North";
		}
		if(west != 0 ) {
			if(exits.length() > 7) {
				exits += ", ";
			}
			exits += "West";
		}
		if(south != 0 ) {
			if(exits.length() > 7) {
				exits += ", ";
			}
			exits += "South";
		}
		if(up != 0) {
		    if(exits.length() > 7) {
		        exits += ", ";
		    }
		    exits += "Up";
		}
		if(down != 0) {
		    if(exits.length() > 7) {
		        exits += ", ";
		    }
		    exits += "Down";
		}
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
		for(int i = 0; i < roomItems.length(); i++) {
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
	    say(s, null);
	}
	public void say(String s, Player ignorePlayer) {
	    Being temporaryBeing;
	    Player player;
	    try{
	        for(int i = 1;i <= roomBeings.length(); i++ ) {
	           // TrollAttack.message("Found a being...");
		        temporaryBeing = (Being)roomBeings.getNext();
		        if(temporaryBeing.isPlayer()) {
		            player = (Player)temporaryBeing;
		            //TrollAttack.message("found a player..");
			        if(player != ignorePlayer) {
			            player.tell(s);
			           // TrollAttack.message("telling " + player.getShort() + " " + s);
			        }
		        }
	        }
	    } catch(Exception e) {
	        e.printStackTrace();
	    } finally {
	        roomBeings.reset();
	    }
	}
	public int followLink (int direction) {
		if(direction == CommandMove.EAST) {
			return east;
		} else if (direction == CommandMove.NORTH ) {
			return north;
		} else if (direction == CommandMove.WEST) {
			return west;
		} else if (direction == CommandMove.SOUTH) {
			return south;
		} else if(direction == CommandMove.UP) {
			return up;
		} else if (direction == CommandMove.DOWN ) {
			return down;
		} else if (direction == CommandMove.NORTHEAST) {
			return northEast;
		} else if (direction == CommandMove.NORTHWEST) {
			return northWest;
		} else if (direction == CommandMove.SOUTHEAST) {
			return southEast;
		} else if (direction == CommandMove.SOUTHWEST) {
			return southWest;
		} else {
			return 0;
		}
	}
	public void setLink( int direction, int destination) {
			if(direction == CommandMove.EAST) {
				east = destination;
			} else if (direction == CommandMove.NORTH ) {
				north = destination;
			} else if (direction == CommandMove.WEST) {
				west = destination;
			} else if (direction == CommandMove.SOUTH) {
				south = destination;
			} else if(direction == CommandMove.UP) {
				up = destination;
			} else if (direction == CommandMove.DOWN ) {
				down = destination;
			} else if (direction == CommandMove.NORTHEAST) {
				northEast = destination;
			} else if (direction == CommandMove.NORTHWEST) {
				northWest = destination;
			} else if (direction == CommandMove.SOUTHEAST) {
				southEast = destination;
			} else if (direction == CommandMove.SOUTHWEST) {
				southWest = destination;
			}
	}
	public Item removeItem(String name) {
		Item newItem;
		for(int i = 0; i < roomItems.length(); i++) {
			Item currentItem = (Item)roomItems.getNext();
		    if(Util.contains(currentItem.name, name)) {
				newItem = currentItem;
				roomItems.delete(currentItem);
				
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

}
