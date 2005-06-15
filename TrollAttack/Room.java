package TrollAttack;
import TrollAttack.Commands.*;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

//import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Room {
	int vnum, east, west , north, south, northEast, northWest, southEast, southWest;
	String description = "", title = "";
	private Item[] roomItems;
	public Mobile[] roomMobiles;
	public LinkedList roomPlayers;
	static String dataFile = "rooms.xml";
	
	public Room(int vnum, String title, String description, int east , int west, int north, int south, int  northEast, int  northWest, int  southEast, int  southWest, Item[] items, Mobile[] mobiles) {
	 this.vnum = vnum; this.east = east; this.west = west; this.north = north; this.south = south; 
	 this.northEast = northEast; this.northWest = northWest; this.southEast = southEast; this.southWest = southWest;
	 this.title = title;
	 this.description = description;
	 
	 roomItems = items;
	 roomMobiles = mobiles;
	 roomPlayers = new LinkedList();
	 //TrollAttack.error("Creating room with vnum value of " + vnum );
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
		southWest;
		for(int i = 0; i < roomItems.length;i++) {
		    if(roomItems[i] != null) {
		        returnValue += "\nroomItems[" + i + "](" + roomItems[i] + ")" + roomItems[i].getShort();  
		    }
		}
		return returnValue;
					
	}
	

	public static Room[] readData() {
		File xmlFile = new File( dataFile );
        try {
        
            // Get Document Builder Factory
            DocumentBuilderFactory factory = 
            DocumentBuilderFactory.newInstance();

            // Turn on validation, and turn off namespaces
            factory.setValidating( false );
            factory.setNamespaceAware(false);
            factory.setIgnoringComments( true ) ;
            
            // Why doesn't this work?
            factory.setIgnoringElementContentWhitespace( false );

            // Obtain a document builder object
            DocumentBuilder builder = factory.newDocumentBuilder();

           // System.out.println();
            //          System.out.println("DataFile : " + xmlFile);
           //System.out.println("Parser Implementation  : " + builder.getClass());
            //System.out.println();

            // Parse the document
            Document doc = builder.parse(xmlFile);
            // Print the document from the DOM tree and feed it an initial 
            // indentation of nothing

			Node node = doc;
			// @TODO: Stop hardcoding of max rooms limit.
			final Room[] Roomlist = new Room[500];
			int vnum = 0, east = 0, west = 0, north = 0, south = 0, northEast = 0, northWest = 0, southEast = 0, southWest = 0;
			String title = "", description = "";
			NodeList kids = node.getChildNodes();
		
	//		 first child of this list is TrollAttack
			Node Kid = kids.item(0);
			// Roomlist must go before other rooms, @TODO!:
			kids = Kid.getChildNodes();
			Node kid = kids.item(1);		
			kids = kid.getChildNodes();
			int itemPtr;
			//Cycle through this for each room
			for(int j = 1; j < kids.getLength(); j += 2) {
				kid = kids.item(j);
				//System.out.println("+");
				//printNode( kid , "" );
				//System.out.println("+" + "has length of " + kid.getChildNodes().getLength());
				east = west = north = south = northEast = northWest = southEast = southWest  = 0;
				title = description = "";
				//FIX THIS HARDCODING!
				Item[] tmpItems = new Item[255];
				Mobile[] tmpMobiles = new Mobile[40];
				if( kid.getNodeType() != Node.TEXT_NODE ) {
					NodeList children = kid.getChildNodes();
					
					for (int i = 1; i < children.getLength(); i += 2) {
						
						Node child = children.item(i);
						if( child.getNodeType() != Node.TEXT_NODE ) {
							//printNode(child, "");
							String name = child.getNodeName();
							String nvalue = child.getChildNodes().item(0).getNodeValue() + "";
							//System.out.println("\"" + name + "->" + nvalue + "\"");
						 	int nodeValue;
						 	if(name.compareTo("title") == 0 || name.compareTo("description") == 0 ) {
						 		nodeValue = 0;
						 	} else {
								if(nvalue.compareTo("null") == 0 ) {
							 		nodeValue = 0;
							 	} else {
							 		//System.out.println("Trying to int " + nvalue );
							 		Integer myInteger = new Integer(nvalue);
							 		nodeValue = myInteger.intValue();
							 	}
						 	}
						 	
						 	
						 	if( name.compareTo("vnum")== 0) {
						 		vnum =  nodeValue;
						 	} else if( name.compareTo("title")==0) {
						 		title = nvalue;
						 	} else if( name.compareTo("description")==0 ) {
						 		description = nvalue;
						 	} else if( name.compareTo("east")== 0) {
						 		east = nodeValue;
					 		} else if( name.compareTo("west")== 0) {
						 		west = nodeValue;
					 		} else if( name.compareTo("north")== 0) {
						 		north = nodeValue;
					 		} else if( name.compareTo("south")== 0) {
						 		south = nodeValue;
					 		} else if( name.compareTo("northEast")== 0) {
					 			northEast = nodeValue;
					 		} else if( name.compareTo("northWest")== 0) {
						 		northWest = nodeValue;
					 		} else if( name.compareTo("southEast")== 0) {
						 		southEast = nodeValue;
					 		} else if( name.compareTo("southWest")== 0) {
						 		southWest =nodeValue;
						 	} else if( name.compareTo("item")== 0 ) {
						 		int n = 0;
						 		while(tmpItems[n] != null ) {
						 			n++;
						 		}
						 		Item newItem = new Item(TrollAttack.gameItems[nodeValue]);
						 		tmpItems[n] = newItem;
						 		
						 	} else if( name.compareTo("mobile") == 0 ) {
						 		int n = 0;
						 		while(tmpMobiles[n] != null) {
						 			n++;
						 		}
						 		if(n >= tmpMobiles.length) {
						 			throw( new Error("ITEM LOAD OVERLOAD!!! (255 ITEMS PER ROOM MAX!"));
						 		} else {
						 		    //TrollAttack.error("Attempting to load mobile # " + nodeValue );
						 		    // I need to make sure that this is instatiating the mobile, not just referencing it.
						 		    Mobile m = new Mobile(TrollAttack.gameMobiles[nodeValue]);
						 		   
						 			tmpMobiles[n] = m;
						 		}
						 	}
				        }
					 }
				}
				for(int i = 0;i < tmpMobiles.length; i++) {
				    if(tmpMobiles[i] != null) {
				        tmpMobiles[i].setCurrentRoom(vnum);
				    }
				}
			//TrollAttack.error("vnum:" + vnum + ", south: " + south );
			 Roomlist[vnum] = new Room(vnum, title, description, east, west, north, south, northEast, northWest, southEast, southWest, tmpItems, tmpMobiles);
			 //System.out.println("Created: " + Roomlist[vnum].toString());
			}
			return Roomlist;
		} catch (ParserConfigurationException e) {
	        System.out.println("The underlying parser does not support " +
	                           "the requested features.");
	        e.printStackTrace();
		} catch (FactoryConfigurationError e) {
		    System.out.println("Error occurred obtaining Document Builder " +
		                       "Factory.");
		    e.printStackTrace();
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return null ;
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
		} else {
		//	System.out.println(this.toString()  );
		}
		if(exits.length() <= 7 ) {
			exits += " none";
		}
		exits += ".";
		
		/** 
		 * Show Items
		 * get all of the items in the room, and write their longdesc.
		 */
		String[] objects = new String[roomItems.length];
		int n = 0;
		for(int i = 0; i < roomItems.length; i++) {
			//TrollAttack.error("About to get room " + i + " of " + roomItems[i]);
			if(roomItems[i] != null) {
				objects[n] = Communication.GREEN + roomItems[i].getLong();
				n++;
			}
		}
		
		/**
		 * Show Mobs
		 * get all mobs from the room, and write their longdesc (as well as any players).
		 */
		String[] mobiles = new String[roomMobiles.length + roomPlayers.length() - 1 ];
		// TrollAttack.error("Printing mobiles...");
		int m = 0;
		for(int i = 0; i < roomMobiles.length; i++) {
			if(roomMobiles[i] != null) {
			    //TrollAttack.error("Found mobile " + roomMobiles[i].vnum + " at position " + m + ", also known as " + roomMobiles[i].getLong());
				mobiles[m] = Communication.PURPLE + roomMobiles[i].getLong();
				//TrollAttack.error("Adding "+ mobiles[i] + " to print queue.");
				m++;
			}
		}
		for(int i = 1;i <= roomPlayers.length();i++) {
		    Player foundPlayer = (Player)roomPlayers.find(i);
		    if( foundPlayer != player) {
		        mobiles[m] = Communication.PURPLE + Util.uppercaseFirst(foundPlayer.getShort()) + " is " + foundPlayer.getDoing() + " here";
		        m++;
		    }
		}
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
	    roomPlayers.add(player);
	}
	public void removePlayer(Player player) {
	    roomPlayers.delete(player);
	}
	public void say(String s) {
	    say(s, null);
	}
	public void say(String s, Player ignorePlayer) {
	    Player player;
	    try{
	        for(int i = 1;i <= roomPlayers.length(); i++ ) {
	    
		        player = (Player)roomPlayers.find(i);
		        if(player != ignorePlayer) {
		            player.tell(s);
		        }
	        }
	    } catch(Exception e) {
	        e.printStackTrace();
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
		} else {
			return 0;
		}
	}
	public Item removeItem(String name) {
		Item newItem;
		for(int i = 0; i < roomItems.length; i++) {
			if(roomItems[i] != null) {
				if(Util.contains(roomItems[i].name, name)) {
					newItem = roomItems[i];
					roomItems[i] = null;
					return newItem;
				}
			}
		}
		return null; 
	}
	public Mobile getMobile(String name, boolean remove) {
		Mobile newMobile;
		for(int i = 0; i < roomMobiles.length; i++) {
			if( roomMobiles[i] != null) {
				if( Util.contains(roomMobiles[i].name, name)) {
					newMobile = roomMobiles[i];
					if(remove == true) {
						roomMobiles[i] = null;
					}
					return newMobile;
				}
				
			}
		}
		return null;
	}
	public Mobile getMobile(String name) {
		return this.getMobile(name, false);
	}
	public Mobile removeMobile(String name) {
		return this.getMobile(name, true);
	}
	public void addMobile( Mobile m) {
		int i = 0;
		while(roomMobiles[i] != null ) {
			i++;
		}
		roomMobiles[i] = m;
	}
	public void addItem(Item i) {
		for(int j = 0;j < roomItems.length;j++) {
			if(roomItems[j] == null) {
				roomItems[j] = i;
				return;
			}
		}
		throw(new Error("ROOM OVERFLOW!"));
	}
	public void healMobiles() {
	    Mobile m;
	    for( int i = 0 ; i < roomMobiles.length ; i++ ) {
	        m = roomMobiles[i];
	        if(m != null) {
	           // TrollAttack.error("A healing wind sweeps through " + m.getShort() + " at pos (" + i + ".");
	            //TrollAttack.error("m HP was " + m.hitPoints);
	            m.hitPoints++;
	            //TrollAttack.error("m HP is " + m.hitPoints);
	            m.movePoints++;
	            m.manaPoints++;
	            if( m.hitPoints > m.maxHitPoints ) {
	                m.hitPoints = m.maxHitPoints;
	            }
	            //TrollAttack.error("m HP will be " + m.hitPoints);
	            if( m.movePoints > m.movePoints ) {
	                m.movePoints = m.maxMovePoints;
	            }
	            if( m.manaPoints > m.manaPoints ) {
	                m.manaPoints = m.maxManaPoints;
	            }
	        }
	    }
	}

}
