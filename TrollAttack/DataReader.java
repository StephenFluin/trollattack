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

import TrollAttack.Items.*;

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
	   if(doc == null) {
	       return null;
	   }
	    XMLHandler handler = new XMLHandler(doc);
	    Hashtable hash = (Hashtable)handler.sections.get("player");
	    
	    if(hash.get("hitLevel") == null) {
	        hash.put("hitLevel", 3 + "");
	    }
	    if(hash.get("gold") == null) {
	        hash.put("gold", 0 + "");
	    }
	    if(hash.get("name") == null) {
	        hash.put("name", playerName + "");
	    }
	   // TrollAttack.message("About to createa new player (Area " + (String)hash.get("area") + "...");
	    Player p = null;
        p = new Player(
            
            new Integer((String)hash.get("gold")).intValue(),
            new Integer((String)hash.get("hitPoints")).intValue(),
            new Integer((String)hash.get("maxHitPoints")).intValue(),
            new Integer((String)hash.get("manaPoints")).intValue(),
            new Integer((String)hash.get("maxManaPoints")).intValue(),
            new Integer((String)hash.get("movePoints")).intValue(),
            new Integer((String)hash.get("maxMovePoints")).intValue(),
            new Integer((String)hash.get("hitLevel")).intValue(),
            (String)hash.get("hitSkill"),
            (String)hash.get("hitDamage"),
            new Integer((String)hash.get("level")).intValue(),
            new Integer((String)hash.get("room")).intValue(),
            new Integer((String)hash.get("experience")).intValue(),
            new Boolean((String)hash.get("builder")).booleanValue(),
            Area.findArea((String)hash.get("area")),
            (String)hash.get("name"),
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
                    (String)area.get("name"), 15);
            areas.add(newArea);
                    
        }
        return areas;
    }
	public static LinkedList readItemData(LinkedList itemList) {
	    int vnum = 0, weight = 0, t = 0, cost = 0, wearLoc = 0;
	    Item newItem;
	    Roll hd;
		String shortDesc = "", longDesc = "", itemName = "", type = "";
        LinkedList items = new LinkedList();
        LinkedList affects = new LinkedList();
        for(int i = 0;i < itemList.length(); i++) {
            vnum = weight = t = cost = wearLoc = 0;
            Hashtable item = (Hashtable)itemList.getNext();

            vnum = new Integer((String)item.get("vnum")).intValue();
            if(item.get("cost") != null) cost = new Integer((String)item.get("cost")).intValue();
            shortDesc = (String)item.get("short");
            longDesc = (String)item.get("long");
            
            weight = new Integer((String)item.get("weight")).intValue();
            itemName = (String)item.get("name");
            
            type = (String)item.get("type");
		if(type.compareToIgnoreCase(Weapon.getItemType()) == 0) {
		    Weapon newWeapon = new Weapon(vnum, weight, cost, itemName, shortDesc, longDesc );
		    newWeapon.setDamage((String)item.get("hitDamage"));
		    newItem = newWeapon;
		} else if(type.compareToIgnoreCase(Armor.getItemType()) == 0 ) {
		    Armor newArmor = new Armor(vnum, weight, cost, itemName, shortDesc, longDesc);
		    newArmor.setupArmor(new Integer((String)item.get("armorClass")).intValue(), (String)item.get("wearLocation"));
		    newItem = newArmor;
		} else {
		    newItem = new Item(vnum, weight, cost, itemName, shortDesc, longDesc);
		}
		//TrollAttack.error("vnum:" + vnum + ", south: " + south );
		 items.add( newItem );
		 //new Item(vnum,weight, cost, itemName, shortDesc, longDesc) );
		 //System.out.println("Created: " + itemList[vnum].toString());
		}
        TrollAttack.message("Loaded " + items.length() + " items.");
		return items;
	}
	public static LinkedList readRoomData(LinkedList roomList) {
	    Room newRoom;
	    LinkedList rooms = new LinkedList();
	    
	    int vnum = 0;
	    //Exit east = null, west = null, north = null, south = null, northEast = null, northWest = null, southEast = null, southWest = null, up = null, down = null;
	    LinkedList exits = new LinkedList();
	    String title = "", description = "";
	    // Defaults aren't necessary with null checks.
	    //String[] defaultNames = {"east", "west", "north", "south", "northEast", "northWest", "southEast", "southWest", "up", "down"};
	    //String[] defaultValues = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
	    
        for(int j = 0;j < roomList.length(); j++) {
            exits = new LinkedList();
            //east = west = north = south = northEast = northWest = southEast = southWest = up = down = null;
            Hashtable room = (Hashtable)roomList.getNext();
            //Util.fillDefaults(room, defaultNames, defaultValues);
            vnum = new Integer((String)room.get("vnum")).intValue();
            title = (String)room.get("title");
            description = (String)room.get("description");
            for(int i = 1;i < Exit.directionList.length;i++) {
                //TrollAttack.message("looping " + i);
                if((Exit.directionList[i] != null) && room.get(Exit.directionList[i]) != null) {
                    if(room.get(Exit.directionList[i]).getClass() == Hashtable.class) {
                        
                        Hashtable exitData = (Hashtable)room.get(Exit.directionList[i]);
                        int ivnum = new Integer((String)exitData.get("vnum")).intValue();
                        Exit thisExit = new Exit(ivnum, i);
                        if(exitData.get("door") != null) {
                            thisExit.setDoor(true);
                            TrollAttack.gameResets.add(
                                    new Reset.ExitReset(
                                            thisExit,
                                            ((String)exitData.get("door")).
                                            	compareToIgnoreCase("open") == 0 
                                            	? true : false,
                                            false,
                                            15)
                                            );
                        }
                        exits.add(thisExit);
                    } else {
                      TrollAttack.error("Wrong type of information for exit " + Exit.directionName(i) + " for room " + title + ", DITCHING IT.");
                        //exits.add( new Exit( new Integer((String)room.get(Exit.directionList[i])).intValue(), i) );
	                    //TrollAttack.message("adding exit..");
                    }
                } else {
                    if(Exit.directionList[i] != null) {
                        //TrollAttack.message("Didn't find an exit type.");
                    }
                }
            }
            
            newRoom = new Room(vnum, title, description, exits);
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
    	            TrollAttack.gameResets.add(new Reset.ItemReset(new Item(TrollAttack.getItem(new Integer((String)items.getNext()))), newRoom, 15));
    	        }
    	    } else {
    	        //TrollAttack.message("single item found");
    	        newRoom.addItem(new Item(TrollAttack.getItem(new Integer((String)tmpRooms))));
    	        TrollAttack.gameResets.add(new Reset.ItemReset(new Item(TrollAttack.getItem(new Integer((String)tmpRooms))), newRoom, 15));
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
    	            TrollAttack.gameResets.add(new Reset.MobileReset(new Mobile(TrollAttack.getMobile(new Integer((String)mobiles.getNext()))), newRoom, 1));
    	        }
    	        mobiles.reset();
    	    } else {
    	        //TrollAttack.message("single mobile found");
    	        TrollAttack.gameResets.add(new Reset.MobileReset(new Mobile(TrollAttack.getMobile(new Integer((String)tmpMobiles))), newRoom, 5));
    	        //newRoom.addMobile(new Mobile(TrollAttack.getMobile(new Integer((String)tmpMobiles))));
    	    }
            
        }
        TrollAttack.message("Loaded " + rooms.length() + " rooms.");
		return rooms;

	}
	static public LinkedList readMobileData(LinkedList mobileList) {
	    Mobile newMobile;
	    LinkedList mobiles = new LinkedList();
	    int vnum, hp, maxhp, clicks, level, hitLevel;
	    int mana, maxMana, move, maxMove;
	    String shortDesc, longDesc, mobileName, hitDamage, hitSkill;
	    
        for(int j = 0;j < mobileList.length(); j++) {
            hitLevel = 0; clicks = 8;
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
            if(mobile.get("clicks") != null) clicks = new Integer((String)mobile.get("clicks")).intValue();
            if(mobile.get("respawn") != null) clicks = new Integer((String)mobile.get("respawn")).intValue();
            if(mobile.get("hitlevel") != null) hitLevel = new Integer((String)mobile.get("hitlevel")).intValue();
            hitSkill = (String)mobile.get("hitskill");
            hitDamage = (String)mobile.get("hitdamage");
            shortDesc = (String)mobile.get("short");
            longDesc = (String)mobile.get("long");
            mobileName = (String)mobile.get("name");
            newMobile = new Mobile(vnum, level, mobileName, hp, maxhp, hitLevel, hitSkill, hitDamage, clicks, shortDesc, longDesc);
	    		 //System.out.println("Created: " + itemList[vnum].toString());
            mobiles.add(newMobile);
	    }
        TrollAttack.message("Loaded " + mobiles.length() + " mobiles.");
		return mobiles;

	}
}
