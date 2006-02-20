package TrollAttack;
/*
 * Created on Jul 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

import java.io.File;
import java.io.FilenameFilter;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import org.w3c.dom.Document;

import TrollAttack.Classes.Class;
import TrollAttack.Classes.AbilityData;
import TrollAttack.Commands.Ability;
import TrollAttack.Commands.AbilityHandler;
import TrollAttack.Items.*;

/**
 * @author PeEll
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class DataReader {
    private Hashtable areaData, classData;

    public DataReader() {
        areaData = readFolder("Areas");
        classData = readFolder("Classes");
    }

    private Hashtable readFolder(String folderName) {
        File dir = new File(folderName);
        Hashtable data = new Hashtable();
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".xml");
            }
        };
        String[] children = dir.list(filter);
        if (children == null) {
            TrollAttack.error("Couldn't find area files.");
            return null;
            // Either dir does not exist or is not a directory
        } else {
            for (int i = 0; i < children.length; i++) {
                // Get filename of file or directory
                String filename = children[i];
                TrollAttack.message("Loading File: " + filename);
                Document doc = Util.xmlize(folderName + "/" + filename);
                XMLHandler.handle(doc, data);
            }
            TrollAttack.message("Read " + children.length + " " + folderName + " files.");
        }
        return data;
    }
    
    public LinkedList<Item> getItems() {
        return readItemData((LinkedList) areaData.get("item"));
    }

    public LinkedList<Mobile> getMobiles() {
        return readMobileData((LinkedList) areaData.get("mobile"));
    }

    public LinkedList<Room> getRooms() {
        return readRoomData((LinkedList) areaData.get("room"));
    }
    
    public LinkedList<Class> getClasses() {
        LinkedList<Hashtable> classesData = linkedListify(classData.get("class"));
        LinkedList<Class> classes = new LinkedList<Class>();
        for(Hashtable hash : classesData) {
            classes.add(readClassData(hash));
        }
        return classes;
        
    }

    public LinkedList<Area> getAreas() {
        if(areaData.get("area") == null) {
            TrollAttack.error("No data exists for areas.");
            return null;
        }
        if (areaData.get("area").getClass() == LinkedList.class) {
            return readAreaData((LinkedList) areaData.get("area"));
        } else if (areaData.get("area").getClass() == Hashtable.class) {
            return readAreaData((Hashtable) areaData.get("area"));
        } else {
            return readAreaData(new LinkedList());
        }
    }

    public static Player readPlayerFile(String playerName) {
        Hashtable sections = new Hashtable();
        //TrollAttack.message("Reading a player..");
        if (playerName.length() < 2) {
            return null;
        }
        Document doc = Util.xmlize("Players/" + playerName + ".player.xml");
        if (doc == null) {
            doc = Util.xmlize("Players/" + playerName + ".txt");
            if (doc == null) {
                return null;
            }
        }
        
        XMLHandler.handle(doc, sections);
        Hashtable<String, String> hash = (Hashtable<String, String>) sections.get("player");

        if (hash.get("hitLevel") == null) {
            hash.put("hitLevel", 3 + "");
        }
        if (hash.get("gold") == null) {
            hash.put("gold", 0 + "");
        }
        if (hash.get("name") == null) {
            hash.put("name", playerName + "");
        }
        if (hash.get("hunger") == null) {
            hash.put("hunger", 0 + "");
        }
        if (hash.get("thirst") == null) {
            hash.put("thirst", 0 + "");
        }
        if (hash.get("shouldcolor") == null) {
        	hash.put("shouldcolor", "true");
        }
        // TrollAttack.message("About to createa new player (Area " +
        // (String)hash.get("area") + "...");

        Player p = null;
        p = new Player(

        new Integer(hash.get("gold")).intValue(), new Integer(
                hash.get("hitPoints")).intValue(), new Integer(
                hash.get("maxHitPoints")).intValue(), new Integer(
                hash.get("manaPoints")).intValue(), new Integer(
                hash.get("maxManaPoints")).intValue(), new Integer(
                hash.get("movePoints")).intValue(), new Integer(
                hash.get("maxMovePoints")).intValue(), new Integer(
                hash.get("hitLevel")).intValue(), (String) hash
                .get("hitSkill"), hash.get("hitDamage"), new Integer(
                hash.get("level")).intValue(), new Integer(
                hash.get("room")).intValue(), new Integer(
                hash.get("experience")).intValue(), new Boolean(
                hash.get("builder")).booleanValue(), (hash
                .get("title") != null ? hash.get("title") : ""), (hash
                .get("prompt") != null ? hash.get("prompt") : ""), (hash
                .get("timePlayed") != null ? new Double( hash
                .get("timePlayed")).doubleValue() : 0), Area.findArea(
                hash.get("area"), TrollAttack.gameAreas), Util
                .intize(hash.get("hunger")), Util.intize(hash
                .get("thirst")), hash.get("name"), hash
                .get("password"), 
                (hash.get("class") != null ? hash.get("class") : "" ) 
                );
        p.setFavor( hash.get("favor") != null ? new Integer(hash.get("favor")).intValue() : 0);
        p.setStats(		hash.get("strength") != null ? new Integer(hash.get("strength")).intValue() : 0,
        				hash.get("constitution") != null ? new Integer(hash.get("constitution")).intValue() : 0,
        				hash.get("charisma") != null ? new Integer(hash.get("charisma")).intValue() : 0,
						hash.get("dexterity") != null ? new Integer(hash.get("dexterity")).intValue() : 0,
						hash.get("intelligence") != null ? new Integer(hash.get("intelligence")).intValue() : 0,
						hash.get("wisdom") != null ? new Integer(hash.get("wisdom")).intValue() : 0);
        p.shouldColor = new Boolean(hash.get("shouldcolor")).booleanValue();
        
        //TrollAttack.debug("Player's class is turning out to be..." + ( hash.get("class") != null ? hash.get("class") : "" ) );
        
        Object tmpAbilities = hash.get("ability");
        LinkedList abilities = linkedListify(tmpAbilities);
        for(Object current : abilities) {
            if(current == null) {
                TrollAttack.error("Current ability is null when loading pfile!");
            } else {
                Hashtable<String, String> data = null;
                try {
                     data = (Hashtable<String, String>)current;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Ability ability = TrollAttack.abilityHandler.find(data.get("name"));
                if(ability == null) {
                    TrollAttack.error("Non-existant ability found in pfile, '" + data.get("name") + "'.");
                }
                float proficiency = new Float(data.get("proficiency"));
                p.practice(ability, proficiency);
            }
        }
        p.rehash();
        
        Object tmpRooms = hash.get("item");
        LinkedList items = linkedListify(tmpRooms);
        for(Object current : items) {
            if (current == null) {
                TrollAttack.error("Current object is a NULL!");
            } else {
                Item newItem = getItemFromObject(current);
                p.addItem((Item) newItem.clone());
            }

        }

        Object tmpItems = hash.get("equipment");
        items = linkedListify(tmpItems);
        Iterator itemIterator = items.iterator();
        while (itemIterator.hasNext()) {
            Item newItem = TrollAttack.getItem(new Integer((String) itemIterator
                    .next()));
            p.wearItem((Item) newItem.clone(), false);
        }
        return p;

    }

    public static LinkedList<Area> readAreaData(Hashtable hash) {
        LinkedList ll = new LinkedList();
        ll.add(hash);
        return readAreaData(ll);
    }

    public static LinkedList<Area> readAreaData(LinkedList<Hashtable> areaList) {

        LinkedList<Area> areas = new LinkedList<Area>();
        if (areaList == null) {
            return areas;
        }
        for(Hashtable area : areaList) {
            Area newArea = new Area(new Integer((String) area.get("low"))
                    .intValue(), new Integer((String) area.get("high"))
                    .intValue(), (String) area.get("filename"), (String) area
                    .get("name"), 8, (area.get("frozen") == null) ? false
                    : (((String) area.get("frozen"))
                            .compareToIgnoreCase("true") == 0 ? true : false));
            areas.add(newArea);

        }
        return areas;
    }

    //Reads in a list of hashtables with item data and creates a list of Items.
    public static LinkedList<Item> readItemData(LinkedList<Hashtable> itemList) {
        int vnum = 0, weight = 0, cost = 0;
        Item newItem;
        String shortDesc = "", longDesc = "", itemName = "", type = "";
        LinkedList items = new LinkedList();
        for(Hashtable item : itemList) {
            vnum = weight = cost = 0;

            vnum = new Integer((String) item.get("vnum")).intValue();
            if (item.get("cost") != null)
                cost = new Integer((String) item.get("cost")).intValue();
            if (item.get("short") != null)
                shortDesc = (String) item.get("short");
            if (item.get("long") != null)
                longDesc = (String) item.get("long");

            if (item.get("weight") != null)
                weight = new Integer((String) item.get("weight")).intValue();
            if (item.get("name") != null)
                itemName = (String) item.get("name");

            type = (String) item.get("type");
            if (type == null) {
                type = "";
            }
            if (type.compareToIgnoreCase(Weapon.getItemType()) == 0) {
                Weapon newWeapon = new Weapon(vnum, weight, cost, itemName,
                        shortDesc, longDesc);
                Hashtable wep = (Hashtable) item.get("typeData");
                if (wep.get("damage") != null) {
                    newWeapon.setDamage((String) wep.get("damage"));
                    //TrollAttack.message("Gave a weapon " +
                    // (String)wep.get("damage"));
                }
                newItem = newWeapon;
            } else if (type.compareToIgnoreCase(Armor.getItemType()) == 0) {
                Armor newArmor = new Armor(vnum, weight, cost, itemName,
                        shortDesc, longDesc);
                Hashtable arm = (Hashtable) item.get("typeData");
                newArmor.setupArmor(new Integer((String) arm.get("armorClass"))
                        .intValue(), (String) arm.get("wearLocation"));
                newItem = newArmor;
            } else if (type.compareToIgnoreCase(Food.getItemType()) == 0) {
                Food newFood = new Food(vnum, weight, cost, itemName,
                        shortDesc, longDesc);
                Hashtable food = (Hashtable) item.get("typeData");
                newFood.setQuality(Util.intize(null, (String) food
                        .get("quality")));
                newItem = newFood;
            } else if (type.compareToIgnoreCase(DrinkContainer.getItemType()) == 0) {
                DrinkContainer newDrink = new DrinkContainer(vnum, weight,
                        cost, itemName, shortDesc, longDesc);
                Hashtable drink = (Hashtable) item.get("typeData");
                newDrink.setup(Util.intize(null, (String) drink.get("volume")),
                        Util.intize(null, (String) drink.get("capacity")));
                newItem = newDrink;
            } else if (type.compareToIgnoreCase(Gold.getItemType()) == 0) {
                Hashtable gold = (Hashtable) item.get("typeData");
                Gold coin = new Gold(Util.intize(null, (String) gold
                        .get("amount")));

                newItem = coin;
            } else if (type.compareToIgnoreCase(Fountain.getItemType()) == 0) {
                Fountain fountain = new Fountain(vnum, weight, cost, itemName,
                        shortDesc, longDesc);
                newItem = fountain;
            } else {
                newItem = new Item(vnum, weight, cost, itemName, shortDesc,
                        longDesc);
            }
            //TrollAttack.error("vnum:" + vnum + ", south: " + south );
            items.add(newItem);
            //new Item(vnum,weight, cost, itemName, shortDesc, longDesc) );
            //System.out.println("Created: " + itemList[vnum].toString());
        }
        TrollAttack.message("Loaded " + items.size() + " items.");
        return items;
    }

    // This function reads in a list of rooms (Hashtable objects) and
    // creates a list of Rooms
    public static LinkedList<Room> readRoomData(LinkedList<Hashtable> roomList) {
        Room newRoom;

        LinkedList rooms = new LinkedList();
        int vnum = 0;
        LinkedList<Exit> exits;
        String title = "", description = "";
        boolean noWander = false;

        // Cycle through each of the rooms in the linked list.
        for(Hashtable room : roomList) {
            exits = new LinkedList<Exit>();
            vnum = new Integer((String) room.get("vnum")).intValue();
            title = (String) room.get("title");
            description = (String) room.get("description");
            if(room.get("nowander") == null) {
            	noWander = false;
            } else {
            	noWander = new Boolean(((String)room.get("nowander"))).booleanValue();
            }
            if(room.get("shop") != null) {
            	newRoom = new Shop(vnum, title, description, exits);
            	//LinkedList<HashTable<String,Object>> = new
            	//TrollAttack.debug(room.get("shop").toString());
            	Hashtable h = (Hashtable)room.get("shop");
            	if(h.get("item") != null) {
            		LinkedList<String> items = linkedListify(((Hashtable)room.get("shop")).get("item"));
                	for(String s : items) {
                		int i = new Integer(s).intValue();
                		((Shop)newRoom).addShopItem(TrollAttack.getItem(i));
                	}
            	}
            } else {
            	newRoom = new Room(vnum, title, description, exits);
            }
            newRoom.setNoWander(noWander);
            rooms.add(newRoom);
            
            // Goes through all known exits and checks the hash for them.
            for (int i = 1; i < Exit.directionList.length; i++) {
                // Makes sure the exit is real and that it doesn't already exist.
                if ((Exit.directionList[i] != null)
                        && room.get(Exit.directionList[i]) != null) {
                    if (room.get(Exit.directionList[i]) instanceof Hashtable) {
                        Hashtable exitData = (Hashtable)room
                                .get(Exit.directionList[i]);
                        int ivnum = new Integer((String) exitData.get("vnum"))
                                .intValue();
                        Exit thisExit = new Exit(ivnum, i);
                        thisExit.setRoom(newRoom);
                        if (exitData.get("door") != null) {
                            thisExit.setDoor(true);
                            if (Area.test(vnum, TrollAttack.gameAreas).frozen) {
                                if (((String) exitData.get("door"))
                                        .compareToIgnoreCase("open") == 0) {
                                    thisExit.open(false);
                                } else {
                                    thisExit.close(false);
                                }
                            } else {
                                TrollAttack.gameResets
                                        .add(new Reset.ExitReset(
                                                thisExit,
                                                ((String) exitData.get("door"))
                                                        .compareToIgnoreCase("open") == 0 ? true
                                                        : false, false, 8));
                            }
                        }
                        exits.add(thisExit);
                    } else {
                        TrollAttack.error("Wrong type of information for exit "
                                + Exit.directionName(i) + " for room " + title
                                + ", DITCHING IT.");
                        //exits.add( new Exit( new
                        // Integer((String)room.get(Exit.directionList[i])).intValue(),
                        // i) );
                        //TrollAttack.message("adding exit..");
                    }
                } else {
                    if (Exit.directionList[i] != null) {
                        //TrollAttack.message("Didn't find an exit type.");
                    }
                }
            }
            // End of finding exits.

            // Ads each room to a linkedlist in its appropriate area.
            Area.test(vnum, TrollAttack.gameAreas).areaRooms.add(newRoom);

            // Loads all information about items into the 'tmpRooms' variable,
            // and
            // Creates item resets or placements.
            // It is a bad variable name, but too much work to change.
            Object tmpItems = room.get("item");
            LinkedList roomItems = linkedListify(tmpItems);

            for(Object current : roomItems) {
                //TrollAttack.debug(current.toString());
                Integer ivnum = new Integer((String) current);
                Item resetItem = TrollAttack.getItem(ivnum);
                if (resetItem == null) {
                    TrollAttack
                            .error("The item we are attempting to load is null.");
                    TrollAttack.error(ivnum.toString());
                } else {
                    //TrollAttack.error("loaded an item");
                }
                if (Area.testItem(resetItem, TrollAttack.gameAreas).frozen) {
                    newRoom.addItem(resetItem);
                } else {
                    TrollAttack.gameResets.add(new Reset.ItemReset(
                            (Item) resetItem.clone(), newRoom, 8));
                }
            }

            // Finished creating all resets and placements for items that belong
            // in the room.

            // Starting to create all resets for mobiles, and mobile wear
            // or give resets and placements.
            Object tmpMobiles = room.get("mobile");
            LinkedList mobiles = linkedListify(tmpMobiles);
            //TrollAttack.debug("List has " + mobiles.length() + "elements");
            for(Object currentMobile : mobiles) {
                Mobile resetMobile;

                // 'hash' contains all data about a mobile reset or placement.
                Hashtable hash = (Hashtable) currentMobile;
                if (hash == null) {
                    TrollAttack
                            .error("The hash that should contain all of the information for this mobile is null!");
                    continue;
                    // This shouldn't happen.
                }

                resetMobile = TrollAttack.getMobile(new Integer((String) hash
                        .get("vnum")));

                // The area of the mobile is frozen, so don't create resets.
                if (Area.testMobile(resetMobile, TrollAttack.gameAreas).frozen) {
                    newRoom.addBeing(resetMobile);
                    resetMobile.setCurrentRoom(newRoom.vnum);

                    Object itemsOfMobile = hash.get("item");
                    LinkedList mobileItems = linkedListify(itemsOfMobile);

                    for(Object currentMobileItem : mobileItems) {
                        Item newItem = getItemFromObject(currentMobileItem);
                        resetMobile.addItem((Item) newItem);
                    }

                    tmpItems = hash.get("equipment");
                    mobileItems = linkedListify(tmpItems);
                    for(Object currentMobileItem : mobileItems) {
                        Item newItem = TrollAttack.getItem(new Integer(
                                (String) currentMobileItem));
                        resetMobile.wearItem((Item) newItem, false);
                    }

                } else {
                    resetMobile = (Mobile) resetMobile.clone();
                    TrollAttack.gameResets.add(new Reset.MobileReset(
                            resetMobile, newRoom, 1));

                    Object itemsOfMobile = hash.get("item");
                    LinkedList mobileItems = linkedListify(itemsOfMobile);

                    for(Object currentMobileItem : mobileItems) {
                        Item newItem = getItemFromObject(currentMobileItem);
                        TrollAttack.gameResets.add(new Reset.GiveReset(
                                (Item) newItem.clone(), resetMobile, 1));
                    }

                    tmpItems = hash.get("equipment");
                    mobileItems = linkedListify(tmpItems);
                    for(Object currentMobileItem : mobileItems) {
                        Item newItem = TrollAttack.getItem(new Integer(
                                (String)currentMobileItem));
                        TrollAttack.gameResets.add(new Reset.WearReset(
                                (Equipment) newItem.clone(), resetMobile, 1));
                    }
                }
            }

        }
        TrollAttack.message("Loaded " + rooms.size() + " rooms.");
        return rooms;

    }

    static public LinkedList readMobileData(LinkedList<Hashtable<String,String>> mobileList) {
        Mobile newMobile;
        LinkedList mobiles = new LinkedList();
        boolean canTeach = false, wander = false;
        int vnum, hp, maxhp, clicks, level, hitLevel;
        int mana, maxMana, move, maxMove;
        String shortDesc, longDesc, mobileName, hitDamage, hitSkill;

        for (Hashtable<String,String> mobile : mobileList) {
            hitLevel = 0;
            clicks = 8;
            //TrollAttack.message(mobile.toString());
            vnum = new Integer(mobile.get("vnum")).intValue();
            level = new Integer(mobile.get("level")).intValue();
            hp = new Integer(mobile.get("hp")).intValue();
            maxhp = new Integer(mobile.get("maxhp")).intValue();
            mana = new Integer(mobile.get("mana")).intValue();
            maxMana = new Integer(mobile.get("maxmana")).intValue();
            move = new Integer(mobile.get("move")).intValue();
            maxMove = new Integer(mobile.get("maxmove")).intValue();
            if (mobile.get("clicks") != null)
                clicks = new Integer(mobile.get("clicks")).intValue();
            if (mobile.get("respawn") != null)
                clicks = new Integer(mobile.get("respawn")).intValue();
            if (mobile.get("hitlevel") != null)
                hitLevel = new Integer(mobile.get("hitlevel"))
                        .intValue();
            if (mobile.get("canTeach") != null)
                canTeach = new Boolean(mobile
                        .get("canTeach")).booleanValue();
            if (mobile.get("wander") != null)
                wander = new Boolean(mobile.get("wander")).booleanValue();
            hitSkill = mobile.get("hitskill");
            hitDamage = mobile.get("hitdamage");
            shortDesc = mobile.get("short");
            longDesc = mobile.get("long");
            mobileName = mobile.get("name");
            newMobile = new Mobile(vnum, level, mobileName, hp, maxhp, mana, maxMana, move, maxMove,
                    hitLevel, hitSkill, hitDamage, clicks, shortDesc, longDesc, wander);
            newMobile.canTeach = canTeach;
            newMobile.setBeingClass( ( mobile.get("class") != null ? mobile.get("class") : "" ) );
            
            //System.out.println("Created: " + itemList[vnum].toString());
            mobiles.add(newMobile);
        }
        TrollAttack.message("Loaded " + mobiles.size() + " mobiles.");
        return mobiles;

    }
    
    public static Class readClassData(Hashtable hash) {
        // Class data looks like this: 
        // "<name>name</name><ability><name>x</name><level>#</level><maxProficiency>#</level>"
        
        Class result;
        String name = (String)hash.get("name");
        LinkedList<Hashtable<String, String>> abilities = linkedListify(hash.get("ability"));
        Hashtable<Ability, AbilityData> abilitiesData = new Hashtable<Ability, AbilityData>();
        for(Hashtable<String, String> abilityData : abilities) {
            Ability ability = TrollAttack.abilityHandler.find(abilityData.get("name"));
            AbilityData data = new AbilityData(Util.intize(abilityData.get("level")), new Float(abilityData.get("maxProficiency")).floatValue());
            abilitiesData.put(ability, data);
        }
        result = new Class(name);
        result.setAbilityData(abilitiesData);
        return result;
    }

    public static Item getItemFromObject(Object newItem) {
        Item resultingItem = null;
        //TrollAttack.debug("Trying to get item from object: " +
        // newItem.toString());
        try {
            int vnum = Util.intize((String) newItem);
            resultingItem = TrollAttack.getItem(new Integer(vnum));
        } catch (ClassCastException e) {
            // Was not a string (not directly a vnum, so we need to use the
            // hashtable to grab the vnum, and then once we have loaded the
            // item,
            // check the rest of the hashtable for which attributes to set on
            // the item. We do this so that containers and wands and junk don't
            // get refilled when you save and quit.
            Hashtable hash = (Hashtable) newItem;
            Integer vnum = new Integer((String) hash.get("vnum"));
            if (vnum == null) {
                TrollAttack
                        .error("An item in a players file did not have a vnum!");
                throw (new NullPointerException());

            }
            resultingItem = TrollAttack.getItem(vnum);
            resultingItem.setAttributesFromHash(hash);
        }
        return resultingItem;
    }

    // Takes data generated by my xmlizer (combination of linked lists
    // and hashtables and data) and makes the data into a linked list
    // of length 0 for null, 1 for direct data, and x for things
    // that are already linked lists.
    public static LinkedList linkedListify(Object wannabeList) {
        if (wannabeList == null) {
            return new LinkedList();
        } else if (wannabeList.getClass() == LinkedList.class) {
            LinkedList ll = new LinkedList();
            for(Object o : ((LinkedList)wannabeList)) {
                ll.add(o);
            }
            return ll;
        } else {
            LinkedList ll = new LinkedList();
            ll.add(wannabeList);
            return ll;

        }
    }


}
