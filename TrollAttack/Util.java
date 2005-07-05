package TrollAttack;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;


/*
 * Created on May 29, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author PeEll
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Util {
	static public boolean contains(String s, String d) {
	    try {
		    if( s.length() < d.length() ) {
		        return false;
		    }
	    } catch( NullPointerException e ) {
	        TrollAttack.error("Attempting to see if  '" + s + "' contains '" + d + "'.");
	        throw(new NullPointerException());
	    }
	    if( s.toLowerCase().startsWith( d.toLowerCase() ) ) {
	        return true;
	    } else {
	        return contains( s.substring( 1 , s.length()), d );
	    }
	}
    //How to write a file (easy):
    // FileOutputStream underlyingStream = new FileOutputStream("Players/" + player.getShort() + ".txt");
    // DataOutputStream fileOut = new DataOutputStream(underlyingStream);
	// fileOut.writeBytes( player.getDocument() );
	static public void savePlayer(Player player) {
		try{
		    XMLPrint(player.toDocument(), "Players/" + player.getShort() + ".txt");
		} catch(Exception e) {
		    TrollAttack.error("There was a problem writing the player's file, or create the player's file.");
		    e.printStackTrace();
		}
	}
	static public int experienceLevel(int level) {
	    return (int)Math.pow(Math.log(level)/Math.log(2), level - 2) * 2000;
	}
	public static Player readPlayerData(String playerName) {
	    //TrollAttack.message("Reading a player..");
	    Document doc = xmlize("Players/" + playerName + ".txt");
	    XMLHandler handler = new XMLHandler(doc);
	    Hashtable hash = (Hashtable)handler.sections.getNext();
	    //TrollAttack.message("About to createa new player...");
	    Player p = null;
        p = new Player(
            new Integer((String)hash.get("hitPoints")).intValue(),
            new Integer((String)hash.get("maxHitPoints")).intValue(),
            new Integer((String)hash.get("manaPoints")).intValue(),
            new Integer((String)hash.get("maxManaPoints")).intValue(),
            new Integer((String)hash.get("movePoints")).intValue(),
            new Integer((String)hash.get("maxMovePoints")).intValue(),
            new Integer((String)hash.get("hitSkill")).intValue(),
            new Roll((String)hash.get("hitDamage")),
            new Integer((String)hash.get("level")).intValue(),
            new Integer((String)hash.get("room")).intValue(),
            new Integer((String)hash.get("experience")).intValue(),
            new Boolean((String)hash.get("builder")).booleanValue(),
            playerName,
            (String)hash.get("password")
            );
        //TrollAttack.message("ZIZZAPED PLAYER " + p.getShort());
	    Object tmpRooms = hash.get("item");
	    if(tmpRooms == null) {
	        // No items
	    } else if(tmpRooms.getClass() == LinkedList.class) {
	        //TrollAttack.message("linked list of items found.");
	        LinkedList items = (LinkedList)(tmpRooms);
	        for(int i = 0; i < items.length();i++) {
	            p.addItem(new Item(TrollAttack.getItem(new Integer((String)items.getNext()))));
	        }
	    } else {
	        //TrollAttack.message("single item found");
	        p.addItem(new Item(TrollAttack.getItem(new Integer((String)tmpRooms))));
	    }
	    return p;
	    
	}
	public static LinkedList readItemData() {
	    int vnum = 0, weight = 0, t = 0;
	    Roll hd;
		String shortDesc = "", longDesc = "", itemName = "", type = "";
        Document doc = xmlize("items.xml");
        XMLHandler handler = new XMLHandler(doc);
        LinkedList items = new LinkedList();
        
        for(int i = 0;i < handler.sections.length(); i++) {
            Hashtable item = (Hashtable)handler.sections.getNext();

            vnum = new Integer((String)item.get("vnum")).intValue();
            shortDesc = (String)item.get("short");
            longDesc = (String)item.get("long");
            weight = new Integer((String)item.get("weight")).intValue();
            itemName = (String)item.get("name");
            hd = new Roll((String)item.get("hitDamage"));
            type = (String)item.get("type");
 		    if(type.compareToIgnoreCase("sword") == 0) {
 		        t = Item.SWORD;
 		    } else if(type.compareToIgnoreCase("helm") == 0) {
 		        t = Item.HELM;
 		    } else if(type.compareToIgnoreCase("boots") == 0) {
 		        t = Item.BOOTS;
 		    } else if(type.compareToIgnoreCase("greaves") == 0) {
 		        t = Item.GREAVES;
 		    } else if(type.compareToIgnoreCase("ring") == 0) {
 		        t = Item.RING;
 		    }
			
		//TrollAttack.error("vnum:" + vnum + ", south: " + south );
		 items.add( new Item(vnum, itemName, weight, shortDesc, longDesc, hd, t) );
		 //System.out.println("Created: " + itemList[vnum].toString());
		}
        TrollAttack.message("Loaded " + items.length() + " items.");
		return items;
	}
	public static LinkedList readRoomData() {
	    Room newRoom;
	    LinkedList rooms = new LinkedList();
	    int vnum = 0, east = 0, west = 0, north = 0, south = 0, northEast = 0, northWest = 0, southEast = 0, southWest = 0, up = 0, down = 0;
	    String title = "", description = "";
	    String[] defaultNames = {"east", "west", "north", "south", "northEast", "northWest", "southEast", "southWest", "up", "down"};
	    String[] defaultValues = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
	    
	    String dataFile = "rooms.xml";
	    Document doc = xmlize( dataFile );
	    
        XMLHandler handler = new XMLHandler(doc);
        
        for(int j = 0;j < handler.sections.length(); j++) {
            Hashtable room = (Hashtable)handler.sections.getNext();
            fillDefaults(room, defaultNames, defaultValues);
            vnum = new Integer((String)room.get("vnum")).intValue();
            title = (String)room.get("title");
            description = (String)room.get("description");
            east = new Integer((String)room.get("east")).intValue();
            west = new Integer((String)room.get("west")).intValue();
            north = new Integer((String)room.get("north")).intValue();
            south = new Integer((String)room.get("south")).intValue();
            northEast = new Integer((String)room.get("northEast")).intValue();
            northWest = new Integer((String)room.get("northWest")).intValue();
            southEast = new Integer((String)room.get("southEast")).intValue();
            southWest = new Integer((String)room.get("southWest")).intValue();
            up = new Integer((String)room.get("up")).intValue();
            down = new Integer((String)room.get("down")).intValue();
            
            newRoom = new Room(vnum, title, description, east, west, north, south, northEast, northWest, southEast, southWest, up, down);
            rooms.add(newRoom);
            
            Object tmpRooms = room.get("item");
    	    if(tmpRooms == null) {
    	        // No items
    	    } else if(tmpRooms.getClass() == LinkedList.class) {
    	        //TrollAttack.message("linked list of items found.");
    	        LinkedList items = (LinkedList)(tmpRooms);
    	        for(int i = 0; i < items.length();i++) {
    	            newRoom.addItem(new Item(TrollAttack.getItem(new Integer((String)items.getNext()))));
    	        }
    	    } else {
    	        //TrollAttack.message("single item found");
    	        newRoom.addItem(new Item(TrollAttack.getItem(new Integer((String)tmpRooms))));
    	    }
    	    Object tmpMobiles = room.get("mobile");
    	    if(tmpMobiles == null) {
    	        // No Mobiles
    	    } else if(tmpMobiles.getClass() == LinkedList.class) {
    	        LinkedList mobiles = (LinkedList)(tmpMobiles);
    	        //int Mobilenum = new Integer((String)mobiles.getNext()).intValue();
    	        //TrollAttack.message(Mobilenum + "");
    	        //TrollAttack.message(TrollAttack.getMobile(new Integer(Mobilenum)).toString());
    	        for(int i = 0; i < mobiles.length();i++) {
    	            newRoom.addMobile(new Mobile(TrollAttack.getMobile(new Integer((String)mobiles.getNext()))));
    	        }
    	        mobiles.reset();
    	    } else {
    	        //TrollAttack.message("single mobile found");
    	        newRoom.addMobile(new Mobile(TrollAttack.getMobile(new Integer((String)tmpMobiles))));
    	    }
            
        }
        TrollAttack.message("Loaded " + rooms.length() + " rooms.");
		return rooms;

	}
	static public LinkedList readMobileData() {
	    Mobile newMobile;
	    LinkedList mobiles = new LinkedList();
	    int vnum, hp, maxhp, hitSkill, rSpawn, level;
	    int mana, maxMana, move, maxMove;
	    String shortDesc, longDesc, mobileName, hitDamage;
	    
	    String dataFile = "mobiles.xml";
	    Document doc = xmlize( dataFile );
	    
        XMLHandler handler = new XMLHandler(doc);
        
        for(int j = 0;j < handler.sections.length(); j++) {
            Hashtable mobile = (Hashtable)handler.sections.getNext();
            //TrollAttack.message(mobile.toString());
            vnum = new Integer((String)mobile.get("vnum")).intValue();
            level = new Integer((String)mobile.get("level")).intValue();
            hp = new Integer((String)mobile.get("hp")).intValue();
            maxhp = new Integer((String)mobile.get("maxhp")).intValue();
            mana = new Integer((String)mobile.get("mana")).intValue();
            maxMana = new Integer((String)mobile.get("maxmana")).intValue();
            move = new Integer((String)mobile.get("move")).intValue();
            maxMove = new Integer((String)mobile.get("maxmove")).intValue();
            rSpawn = new Integer((String)mobile.get("respawn")).intValue();
            hitSkill = new Integer((String)mobile.get("hitskill")).intValue();
            hitDamage = (String)mobile.get("hitdamage");
            shortDesc = (String)mobile.get("short");
            longDesc = (String)mobile.get("long");
            mobileName = (String)mobile.get("name");
            newMobile = new Mobile(vnum, level, mobileName, hp, maxhp, hitSkill, hitDamage, rSpawn, shortDesc, longDesc);
	    		 //System.out.println("Created: " + itemList[vnum].toString());
            mobiles.add(newMobile);
	    }
        TrollAttack.message("Loaded " + mobiles.length() + " mobiles.");
		return mobiles;

	}

	static public String uppercaseFirst(String string) {
	    return string.substring(0,1).toUpperCase() + string.substring(1);
	}
	static public Document xmlize(String dataFile) {
	    try {
		    File xmlFile = new File( dataFile );
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
	        factory.setValidating( false );
	        factory.setNamespaceAware(false);
	        factory.setIgnoringComments( true ) ;
	        
	        factory.setIgnoringElementContentWhitespace( true );
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        return builder.parse(xmlFile);
	        
	        
		} catch(FileNotFoundException e) {
		    //TrollAttack.error("File not found in xmlize...");
		} catch (ParserConfigurationException e) {
	        TrollAttack.error("The underlying parser does not support " +
	                           "the requested features.");
	        e.printStackTrace();
		} catch (FactoryConfigurationError e) {
		    System.out.println("Error occurred obtaining Document Builder " +
		                       "Factory.");
		    e.printStackTrace();
		} catch (Exception e) {
		    e.printStackTrace();
		}
        // Parse the document
       
       return null;
	}
	static public void XMLPrint(Document doc, String filename) {
	    try {  
	            Source source = new DOMSource(doc);
	            File file = new File(filename);
	            Result result = new StreamResult(file);
	            //result = new StreamResult(System.out);
	            
	            Transformer trox = TransformerFactory.newInstance().newTransformer();
	            trox.transform(source,result);
	            
	            
	            
	         } catch(Exception e) {
	             TrollAttack.error("There was a problem in XMLPrint when writing the file.");
	             e.printStackTrace();
	         }
	}
	static public void fillDefaults(Hashtable hash, String[] fillNames, String[] fillValues) {
	    Object tmp;
	    for(int i = 0;i < fillNames.length;i++) {
	        tmp = hash.get(fillNames[i]);
	        if(tmp == null) {
	            hash.put(fillNames[i], fillValues[i]);
	        }
	    }
	}
	static public Node nCreate(Document doc, String name, String value) {
	    Node n = doc.createElement(name);
	    n.appendChild(doc.createTextNode(value));
	    
	    return n;
	}
	static public void Savearea() {
	    DocumentBuilderFactory factory = 
            DocumentBuilderFactory.newInstance();

	    DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Document doc = builder.newDocument();
	    Node n = doc.createElement("TrollAttack");
	    doc.appendChild(n);
	    Room currentRoom = null;
	    for(int i = 0; i < TrollAttack.gameRooms.length(); i++ ) {
	        currentRoom = (Room)TrollAttack.gameRooms.getNext();
	        n.appendChild(currentRoom.toNode(doc));
	    }
	    TrollAttack.gameRooms.reset();
		try{
		    Util.XMLPrint(doc, "rooms.xml");
		} catch(Exception e) {
		    TrollAttack.error("There was a problem writing the area file.");
		    e.printStackTrace();
		}
	}
}
