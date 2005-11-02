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

import org.w3c.dom.Document;

import TrollAttack.Items.*;

/**
 * @author PeEll
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
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
            for (int i = 0; i < children.length; i++) {
                // Get filename of file or directory
                String filename = children[i];
                TrollAttack.message("Loading File: " + filename);
                Document doc = Util.xmlize("Areas/" + filename);
                XMLHandler handler = new XMLHandler(doc, sections);
            }
        }
    }

    public LinkedList getItems() {
        return readItemData((LinkedList) sections.get("item"));
    }

    public LinkedList getMobiles() {
        return readMobileData((LinkedList) sections.get("mobile"));
    }

    public LinkedList getRooms() {
        return readRoomData((LinkedList) sections.get("room"));
    }

    public LinkedList getAreas() {
        if (sections.get("area").getClass() == LinkedList.class) {
            return readAreaData((LinkedList) sections.get("area"));
        } else if (sections.get("area").getClass() == Hashtable.class) {
            return readAreaData((Hashtable) sections.get("area"));
        } else {
            return readAreaData(new LinkedList());
        }
    }

    public static Player readPlayerData(String playerName) {
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
        XMLHandler handler = new XMLHandler(doc);
        Hashtable hash = (Hashtable) handler.sections.get("player");

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
        // TrollAttack.message("About to createa new player (Area " +
        // (String)hash.get("area") + "...");

        Player p = null;
        p = new Player(

        new Integer((String) hash.get("gold")).intValue(), new Integer(
                (String) hash.get("hitPoints")).intValue(), new Integer(
                (String) hash.get("maxHitPoints")).intValue(), new Integer(
                (String) hash.get("manaPoints")).intValue(), new Integer(
                (String) hash.get("maxManaPoints")).intValue(), new Integer(
                (String) hash.get("movePoints")).intValue(), new Integer(
                (String) hash.get("maxMovePoints")).intValue(), new Integer(
                (String) hash.get("hitLevel")).intValue(), (String) hash
                .get("hitSkill"), (String) hash.get("hitDamage"), new Integer(
                (String) hash.get("level")).intValue(), new Integer(
                (String) hash.get("room")).intValue(), new Integer(
                (String) hash.get("experience")).intValue(), new Boolean(
                (String) hash.get("builder")).booleanValue(), (hash
                .get("title") != null ? (String) hash.get("title") : ""), (hash
                .get("timePlayed") != null ? new Double((String) hash
                .get("timePlayed")).doubleValue() : 0), Area.findArea(
                (String) hash.get("area"), TrollAttack.gameAreas), Util
                .intize((String) hash.get("hunger")), Util.intize((String) hash
                .get("thirst")), (String) hash.get("name"), (String) hash
                .get("password"));

        //TrollAttack.message("ZIZZAPED PLAYER " + p.getShort());
        Object tmpRooms = hash.get("item");
        // This is going to get complicated until I figure out attributes,
        // TODO because I am going to have to deal with 4 cases, single
        // item with attributes, without attributes, multiple items with
        // attributes, and multiple without, ack!
        //if(tmpRooms != null) TrollAttack.debug(tmpRooms.toString());
        if (tmpRooms == null) {
            // No items
        } else if (tmpRooms.getClass() == LinkedList.class) {
            // linked list of items found, indicating more than one item found.
            LinkedList items = (LinkedList) (tmpRooms);
            items.reset();
            for (int i = 0; i < items.length(); i++) {
                Object current = items.getNext();
                if (current == null) {
                    TrollAttack.error("Current object is a NULL!");
                } else {
                    //TrollAttack.debug("Current object is a " +
                    // current.getClass());
                    Item newItem = getItemFromObject(current);
                    //Item newItem = TrollAttack.getItem(new
                    // Integer((String)items.getNext()));
                    //addItemDataToPlayer(newItem);
                    p.addItem((Item) newItem.clone());
                }

            }
        } else {
            //TrollAttack.debug("single item found");
            Item newItem = getItemFromObject(tmpRooms);
            //Item newItem = (Item)TrollAttack.getItem(new
            // Integer((String)tmpRooms));
            //addItemDataToPlayer(newItem);
            p.addItem((Item) newItem.clone());
        }

        Object tmpItems = hash.get("equipment");
        if (tmpItems == null) {
            // No equipment
        } else if (tmpItems.getClass() == LinkedList.class) {
            // More than 1 piece of equipment.
            LinkedList items = (LinkedList) tmpItems;
            while (items.itemsRemain()) {
                Item newItem = TrollAttack.getItem(new Integer((String) items
                        .getNext()));
                p.wearItem((Item) newItem.clone());
            }
        } else {
            Item newItem = TrollAttack.getItem(new Integer((String) tmpItems));
            p.wearItem((Item) newItem.clone());
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
        if (areaList == null) {
            return areas;
        }
        for (int i = 0; i < areaList.length(); i++) {
            area = (Hashtable) areaList.getNext();
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

    public static LinkedList readItemData(LinkedList itemList) {
        int vnum = 0, weight = 0, t = 0, cost = 0, wearLoc = 0;
        Item newItem;
        Roll hd;
        String shortDesc = "", longDesc = "", itemName = "", type = "";
        LinkedList items = new LinkedList();
        LinkedList affects = new LinkedList();
        for (int i = 0; i < itemList.length(); i++) {
            vnum = weight = t = cost = wearLoc = 0;
            Hashtable item = (Hashtable) itemList.getNext();

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
        TrollAttack.message("Loaded " + items.length() + " items.");
        return items;
    }

    // This function creates a linked list of hashes and linked lists
    // that contain all of the data for rooms, as well as all of the
    // information for mobile in room, door, item, give, and wear resets.
    public static LinkedList readRoomData(LinkedList roomList) {
        Room newRoom;

        LinkedList rooms = new LinkedList();
        int vnum = 0;
        java.util.LinkedList<Exit> exits;
        String title = "", description = "";

        // Cycle through each of the rooms in the linked list.
        for (int j = 0; j < roomList.length(); j++) {
            exits = new java.util.LinkedList<Exit>();

            // puts all of the data about a room in the 'room' variable.
            Hashtable room = (Hashtable) roomList.getNext();

            vnum = new Integer((String) room.get("vnum")).intValue();
            title = (String) room.get("title");
            description = (String) room.get("description");
            newRoom = new Room(vnum, title, description, exits);
            rooms.add(newRoom);
            // Goes through all known exits and checks the hash for them.
            for (int i = 1; i < Exit.directionList.length; i++) {
                //TrollAttack.message("looping " + i);
                if ((Exit.directionList[i] != null)
                        && room.get(Exit.directionList[i]) != null) {
                    if (room.get(Exit.directionList[i]).getClass() == Hashtable.class) {

                        Hashtable exitData = (Hashtable) room
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

            for (int i = 0; i < roomItems.length(); i++) {
                Integer ivnum = new Integer((String) roomItems.getNext());
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
            while (mobiles.itemsRemain()) {
                Mobile resetMobile;

                // 'hash' contains all data about a mobile reset or placement.
                Hashtable hash = (Hashtable) mobiles.getNext();
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

                    while (mobileItems.itemsRemain()) {
                        Item newItem = getItemFromObject(mobileItems.getNext());
                        resetMobile.addItem((Item) newItem);
                    }

                    tmpItems = hash.get("equipment");
                    mobileItems = linkedListify(tmpItems);
                    while (mobileItems.itemsRemain()) {
                        Item newItem = TrollAttack.getItem(new Integer(
                                (String) mobileItems.getNext()));
                        resetMobile.wearItem((Item) newItem);
                    }

                } else {
                    resetMobile = (Mobile) resetMobile.clone();
                    TrollAttack.gameResets.add(new Reset.MobileReset(
                            resetMobile, newRoom, 1));

                    Object itemsOfMobile = hash.get("item");
                    LinkedList mobileItems = linkedListify(itemsOfMobile);

                    while (mobileItems.itemsRemain()) {
                        Item newItem = getItemFromObject(mobileItems.getNext());
                        TrollAttack.gameResets.add(new Reset.GiveReset(
                                (Item) newItem.clone(), resetMobile, 1));
                    }

                    tmpItems = hash.get("equipment");
                    mobileItems = linkedListify(tmpItems);
                    while (mobileItems.itemsRemain()) {
                        Item newItem = TrollAttack.getItem(new Integer(
                                (String) mobileItems.getNext()));
                        TrollAttack.gameResets.add(new Reset.WearReset(
                                (Equipment) newItem.clone(), resetMobile, 1));
                    }
                }
            }
            mobiles.reset();

        }
        TrollAttack.message("Loaded " + rooms.length() + " rooms.");
        return rooms;

    }

    static public LinkedList readMobileData(LinkedList mobileList) {
        Mobile newMobile;
        LinkedList mobiles = new LinkedList();
        boolean canTeachMagic = false;
        int vnum, hp, maxhp, clicks, level, hitLevel;
        int mana, maxMana, move, maxMove;
        String shortDesc, longDesc, mobileName, hitDamage, hitSkill;

        for (int j = 0; j < mobileList.length(); j++) {
            hitLevel = 0;
            clicks = 8;
            Hashtable mobile = (Hashtable) mobileList.getNext();
            //TrollAttack.message(mobile.toString());
            vnum = new Integer((String) mobile.get("vnum")).intValue();
            level = new Integer((String) mobile.get("level")).intValue();
            hp = new Integer((String) mobile.get("hp")).intValue();
            maxhp = new Integer((String) mobile.get("maxhp")).intValue();
            mana = new Integer((String) mobile.get("mana")).intValue();
            maxMana = new Integer((String) mobile.get("maxmana")).intValue();
            move = new Integer((String) mobile.get("move")).intValue();
            maxMove = new Integer((String) mobile.get("maxmove")).intValue();
            if (mobile.get("clicks") != null)
                clicks = new Integer((String) mobile.get("clicks")).intValue();
            if (mobile.get("respawn") != null)
                clicks = new Integer((String) mobile.get("respawn")).intValue();
            if (mobile.get("hitlevel") != null)
                hitLevel = new Integer((String) mobile.get("hitlevel"))
                        .intValue();
            if (mobile.get("canTeachMagic") != null)
                canTeachMagic = new Boolean((String) mobile
                        .get("canTeachMagic")).booleanValue();
            hitSkill = (String) mobile.get("hitskill");
            hitDamage = (String) mobile.get("hitdamage");
            shortDesc = (String) mobile.get("short");
            longDesc = (String) mobile.get("long");
            mobileName = (String) mobile.get("name");
            newMobile = new Mobile(vnum, level, mobileName, hp, maxhp,
                    hitLevel, hitSkill, hitDamage, clicks, shortDesc, longDesc);
            //System.out.println("Created: " + itemList[vnum].toString());
            mobiles.add(newMobile);
        }
        TrollAttack.message("Loaded " + mobiles.length() + " mobiles.");
        return mobiles;

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
            return (LinkedList) wannabeList;
        } else {
            LinkedList ll = new LinkedList();
            ll.add(wannabeList);
            return ll;

        }
    }
}
