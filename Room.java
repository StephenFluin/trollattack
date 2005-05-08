import java.io.File;
import java.util.LinkedList;

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
	private int[] roomItems;
	private int[] roomMobiles;
	static String dataFile = "rooms.xml";
	
	public Room(int vnum, String title, String description, int east , int west, int north, int south, int  northEast, int  northWest, int  southEast, int  southWest, int[] items, int[] mobiles) {
	 this.vnum = vnum; this.east = east; this.west = west; this.north = north; this.south = south; 
	 this.northEast = northEast; this.northWest = northWest; this.southEast = southEast; this.southWest = southWest;
	 this.title = title;
	 this.description = description;
	 
	 roomItems = items;
	 roomMobiles = mobiles;
	 //TrollAttack.print("Creating room with south value of " + south );
	 //System.out.println("Creating room object");
	}
	
	public String toString() {
		return vnum + ":" + 
		east + "," +
		west + "," +
		north + "," +
		south + "," +
		northEast + "," +
		northWest + "," +
		southEast + "," +
		southWest;
					
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
            factory.setIgnoringElementContentWhitespace( true );

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
			final Room[] Roomlist = new Room[255];
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
				int[] tmpItems = new int[255];
				int[] tmpMobiles = new int[255];
				if( kid.getNodeType() != Node.TEXT_NODE ) {
					NodeList children = kid.getChildNodes();
					
					for (int i = 1; i < children.getLength(); i += 2) {
						
						Node child = children.item(i);
						if( child.getNodeType() != Node.TEXT_NODE ) {
							//printNode(child, "");
							String name = child.getNodeName();
							String nvalue = child.getChildNodes().item(0) + "";
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
						 		while(tmpItems[n] != 0 ) {
						 			n++;
						 		}
						 		if(n >= tmpItems.length) {
						 			throw( new Error("ITEM LOAD OVERLOAD!!! (255 ITEMS PER ROOM MAX!"));
						 		} else {
						 			tmpItems[n] = new Integer(nodeValue).intValue();
						 		}
						 		
						 	} else if( name.compareTo("mobile") == 0 ) {
						 		int n = 0;
						 		while(tmpMobiles[n] != 0) {
						 			n++;
						 		}
						 		if(n >= tmpMobiles.length) {
						 			throw( new Error("ITEM LOAD OVERLOAD!!! (255 ITEMS PER ROOM MAX!"));
						 		} else {
						 			tmpMobiles[n] = new Integer(nodeValue).intValue();
						 		}
						 	}
				        }
					 }
				}
			//TrollAttack.print("vnum:" + vnum + ", south: " + south );
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
			//TrollAttack.print("About to get room " + i + " of " + roomItems[i]);
			if(roomItems[i] != 0) {
				objects[i] = TrollAttack.gameItems[roomItems[i]].getLong();
				n++;
			}
		}
		
		/**
		 * Show Mobs
		 * get all mobs from the room, and write their longdesc.
		 */
		String[] mobiles = new String[roomMobiles.length];
		int m = 0;
		for(int i = 0; i < roomMobiles.length; i++) {
			if(roomMobiles[i] != 0) {
				mobiles[i] = TrollAttack.gameMobiles[roomMobiles[i]].getLong();
				m++;
			}
		}
		String[] firsts = { title , description , exits };
		//TrollAttack.print("3 firsts, " + i + " objects.");
		String[] myReturn = new String[n + m + firsts.length];
		System.arraycopy(firsts, 0, myReturn, 0, firsts.length);
		System.arraycopy(objects, 0, myReturn, firsts.length, n);
		System.arraycopy(mobiles, 0, myReturn, firsts.length + n, m);
		
		return myReturn;
	}
	public void pLook() {
		String[] looks = look();
		for(int i = 0; i < looks.length; i++ ) {
				/*TrollAttack.print( i + "!!!" );
				if(looks[i] == null) {
					TrollAttack.print( "IS GOING TO CRASH< IS NULL!");
				}*/
				TrollAttack.print( looks[i] );
		}
	}
	public int followLink (int direction) {
		if(direction == Dirs.EAST) {
			return east;
		} else if (direction == Dirs.NORTH ) {
			return north;
		} else if (direction == Dirs.WEST) {
			return west;
		} else if (direction == Dirs.SOUTH) {
			return south;
		} else {
			return 0;
		}
		
	}
	public Item getItem(String name) {
		Item newItem;
		for(int i = 0; i < roomItems.length; i++) {
			if(TrollAttack.gameItems[roomItems[i]] != null) {
				//TrollAttack.print(roomItems[i] + TrollAttack.gameItems[roomItems[i]].name);
				
				if(TrollAttack.gameItems[roomItems[i]].name.compareToIgnoreCase(name) == 0) {
					newItem = TrollAttack.gameItems[roomItems[i]];
					roomItems[i] = 0;
					return newItem;
					
				} else {
					//TrollAttack.print("looking at object i in room " + TrollAttack.player.getCurrentRoom());
				}
			}
		}
		return null; 
	}
	public void addItem(Item i) {
		for(int j = 0;j < roomItems.length;j++) {
			if(j == 0) {
				roomItems[j] = i.vnum;
				return;
			}
		}
		throw(new Error("ROOM OVERFLOW!"));
	}
}
