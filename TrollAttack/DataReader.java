/*
 * Created on Jul 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package TrollAttack;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Hashtable;

import org.w3c.dom.Document;

/**
 * @author PeEll
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DataReader {
    private LinkedList items, mobiles, rooms;
    private Hashtable sections;
    public DataReader() {
        File dir = new File("Areas");
        sections = new Hashtable();
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".xml");
            }
        };
        String[] children = dir.list(filter);
        if (children == null) {
            TrollAttack.error("Couldn't find area files.");
            // Either dir does not exist or is not a directory
        } else {
            for (int i=0; i<children.length; i++) {
                // Get filename of file or directory
                String filename = children[i];
                TrollAttack.message("Loading File: " + filename);
                Document doc = Util.xmlize("Areas/" + filename);
        	    XMLHandler handler = new XMLHandler(doc, sections);
            }
        }
    }
    public LinkedList getItems() {
        return readItemData((LinkedList)sections.get("item"));
    }
    public LinkedList getMobiles() {
        return readMobileData((LinkedList)sections.get("mobile"));
    }
    public LinkedList getRooms() {
        return readRoomData((LinkedList)sections.get("room"));
    }
    public LinkedList getAreas() {
        if(sections.get("area").getClass() == LinkedList.class) {
            return readAreaData((LinkedList)sections.get("area"));
        } else if (sections.get("area").getClass() == Hashtable.class) {
            return readAreaData((Hashtable)sections.get("area"));
        } else {
            return readAreaData(new LinkedList());
        }
    }
    public static Player readPlayerData(String playerName) {
	    //TrollAttack.message("Reading a player..");
	    Document doc = Util.xmlize("Players/" + playerName + ".txt");
	    XMLHandler handler = new XMLHandler(doc);
	    Hashtable hash = (Hashtable)handler.sections.get("player");
	    
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
    public static LinkedList readAreaData(Hashtable hash) {
        LinkedList ll = new LinkedList();
        ll.add(hash);
        return readAreaData(ll);
    }
    public static LinkedList readAreaData(LinkedList areaList) {
        
        Hashtable area;
        LinkedList areas = new LinkedList();
        if(areaList == null) {
            return areas;
        }
        for(int i = 0;i < areaList.length(); i++) {
            area = (Hashtable)areaList.getNext();
            Area newArea = new Area(
                    new Integer((String)area.get("low")).intValue(),
                    new Integer((String)area.get("high")).intValue(),
                    (String)area.get("filename"),
                    (String)area.get("name"));
            areas.add(newArea);
                    
        }
        return areas;
    }
	public static LinkedList readItemData(LinkedList itemList) {
	    int vnum = 0, weight = 0, t = 0;
	    Roll hd;
		String shortDesc = "", longDesc = "", itemName = "", type = "";
        LinkedList items = new LinkedList();
        for(int i = 0;i < itemList.length(); i++) {
            Hashtable item = (Hashtable)itemList.getNext();

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
	public static LinkedList readRoomData(LinkedList roomList) {
	    Room newRoom;
	    LinkedList rooms = new LinkedList();
	    
	    int vnum = 0, east = 0, west = 0, north = 0, south = 0, northEast = 0, northWest = 0, southEast = 0, southWest = 0, up = 0, down = 0;
	    String title = "", description = "";
	    String[] defaultNames = {"east", "west", "north", "south", "northEast", "northWest", "southEast", "southWest", "up", "down"};
	    String[] defaultValues = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
	    
        for(int j = 0;j < roomList.length(); j++) {
            Hashtable room = (Hashtable)roomList.getNext();
            Util.fillDefaults(room, defaultNames, defaultValues);
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
            Area.test(vnum)
            .rooms
            .add(
                    newRoom
                    );
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
	static public LinkedList readMobileData(LinkedList mobileList) {
	    Mobile newMobile;
	    LinkedList mobiles = new LinkedList();
	    int vnum, hp, maxhp, hitSkill, rSpawn, level;
	    int mana, maxMana, move, maxMove;
	    String shortDesc, longDesc, mobileName, hitDamage;
	    
        for(int j = 0;j < mobileList.length(); j++) {
            Hashtable mobile = (Hashtable)mobileList.getNext();
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
}
