package TrollAttack;

import java.util.*;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


import TrollAttack.Classes.*;
import TrollAttack.Classes.Class;
import TrollAttack.Commands.Ability;
import TrollAttack.Commands.CommandHandler;
import TrollAttack.Items.*;

/*
 * Created on May 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author PeEll
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Being implements Cloneable {
    public int hitPoints;
    public int maxHitPoints;
    public int manaPoints;
    public int maxManaPoints;
    public int movePoints;
    public int maxMovePoints;

    public int experience;
    public int gold;
    public int weight;
    public int level;
    /**
     * The amount of favor this being has with its deity.
     */
    public int favor;
    
    /**
     * Believed to be currently unimplmented
     */
    public int hitLevel;


    public int currentRoom = 1, state = 0, hunger = 0, thirst = 0,
            practiceSessions;

    public int strength, intelligence, wisdom, dexterity, constitution, charisma,
            luck;

    public Vector<Item> beingItems = new Vector<Item>();
    public Vector<Being> followers = new Vector<Being>();
    
    public Being switched = null;
    private Being following;

    public boolean canTeach = false;
    
    private Class beingClass = null;
    public String busyDoing = "";

    Room actualRoom = null;

    private Hashtable<Ability, AbilityData> abilitiesData = new Hashtable<Ability, AbilityData>();
    
    public Hashtable<Ability, AbilityData> getAbilitiesData() {
        return abilitiesData;
    }
    public Vector<Equipment> equipment = new Vector<Equipment>();

    // Allows mobiles to do anything a player can do, is this dangerous or
    // memory hoggery?
    public CommandHandler ch = null;
    private CommandHandler backupCH = null;

    /**
     * A dice roll that determines how much damage the player does 
     * regardless of their weapons.
     */
    public Roll hitDamage;

    public Roll hitSkill;



    public String shortDescription, name, longDesc;

    boolean isPlayer = false;

    Being beingFighting = null;

    private Prompt myPrompt = new Prompt();

    public int getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Returns the room that the being currently occupies.  Figures out
     * which room the being is in by asking the game to return the room that
     * matches the id stored in currentRoom.  If for some reason the room 
     * can't be figured out, the being will be moved to the index room.
     * @return
     */
    public Room getActualRoom() {
        Room result = TrollAttack.getRoom(currentRoom);
        // can't optimize with actualRoom because reloadWorld can't update it..
        // this is a lie, we just need to make reloadWorld smarter. (we = i).
        if (result == null) {
            TrollAttack.error("Being doesn't have a correct currentroom ("
                    + currentRoom + ").");
            setCurrentRoom(1);
            return actualRoom;
        }
        return result;
    }

    /**
     * Returns the area discovered using the currentRoom VNUM from the list 
     * of areas in gameAreas.
     * @return The Area found using the currentRoom vnum and the game areas.
     */
    public Area getActualArea() {
        return Area.test(currentRoom, TrollAttack.gameAreas);
    }

    public void setCurrentRoom(int r) {
        currentRoom = r;
        if (TrollAttack.gameRooms != null) {
            actualRoom = TrollAttack.getRoom(currentRoom);
        }
        if (r < 1) {
            throw (new NullPointerException());
        } else {
        }
    }

    public void setCurrentRoom(Room r) {
        actualRoom = r;
        currentRoom = r.vnum;
    }

    
    /**
     * States: 0 Awake/Alert 1 Sitting 2 Lying down 3 Sleeping Should inverse
     * this list, and add fighting as a state so we can have "minpos"
     */
    public int getState() {
        return state;
    }

    public void setState(int newState) {
        state = newState;
    }

    public String getDoing() {
        if (isFighting()) {
            return "fighting";
        } else if (state == 0) {
            return "standing";
        } else if (state == 1) {
            return "sitting";
        } else if (state == 2) {
            return "lying down";
        } else if (state == 3) {
            return "sleeping";
        } else {
            return "something secret";
        }

    }

    public int[] getStatistics() {
        int[] stats = { strength, intelligence, wisdom, dexterity,
                constitution, charisma, luck };
        return stats;
    }

    public void setStatistics(int str, int intel, int wis, int dex, int con,
            int cha, int lck) {
        int[] stats = { str, intel, wis, dex, con, cha, lck };
        setStatistics(stats);
    }

    public void setStatistics(int[] stats) {
        strength = stats[0];
        intelligence = stats[1];
        wisdom = stats[2];
        dexterity = stats[3];
        constitution = stats[4];
        charisma = stats[5];
        luck = stats[6];
    }

    /**
     * Send the being the visible information about 
     * the room they are currently in.
     *
     */
    public void look() {
        look(getActualRoom());
    }
    /**
     * Send the being information about the given room.
     * @param room
     */
    public void look(Room room) {
        tell(room.look(this), false);
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public String getShort() {
        return getShort(null);
    }

    /**
     * Gets the short of this being from the perspective of the given being.
     * @param whoAmI The being this is being generated for.
     * @return Returns the short description of the being, unless this is the being it is for.  Then "you".
     */
    public String getShort(Being whoAmI) {
        if (whoAmI == this) {
            return "you";
        } else {
            return shortDescription;
        }
    }

    public String getLong() {
        return Util.uppercaseFirst(getShort()) + " is "
                + getDoing() + " here";
    }

    public String getName() {
        return name;
    }

    public Class getBeingClass() {
        return beingClass;
    }
    public String getClassName() {
        if(beingClass == null) {
            return "Unclassed";
        } else {
            return beingClass.getName();
        }
    }
    
    public void setBeingClass(Class newClass) {
        beingClass = newClass;
    }
    public String setBeingClass(String className) {
        for(Class beingClass : TrollAttack.gameClasses) {
            if(beingClass.getName() == null) {
                TrollAttack.error("This class has no name, '" + beingClass.toString() + "'!");
            }
            if(beingClass.getName().toLowerCase().startsWith(className.toLowerCase())) {
                setBeingClass(beingClass);
                return "Setting " + getShort() + " to be a " + getClassName() + ".";
            }
        }
        Class notClass = null;
        setBeingClass(notClass);
        return "Setting " + getShort() + " to be a " + getClassName() + ".";
        
    }
    
    public int getHitDamage() {
    	int result = hitDamage.roll();
    	for(Equipment tmpEq : equipment) {
    		if(tmpEq instanceof Weapon) {
    			Weapon w = (Weapon)tmpEq;
    			result += w.damage.roll();
    		}
    	}
    	result += strength / 4;
    	return hitDamage.roll();
    }

    /**
     * Calculates the average hit damage this being will do based on their hit damage and their weapons.
     * @return The average hit damage this being will do.
     */
    public int getAverageHitDamage() {
        return hitDamage.getAverage();
    }
   
    /**
     * Returns the string containing the prompt of the being with
     * all of the variables replaced with their appropriate values.
     * @return
     */
    public String getPrompt() {
        return myPrompt.showPrompt(
                hitPoints, 
                maxHitPoints, 
                manaPoints,
                maxManaPoints, 
                movePoints, 
                maxMovePoints, 
                experience, 
                Util.experienceLevel(level) - experience,
                currentRoom,
                gold);
    }
    public void prompt() {
        prompt( getPrompt());
    }
    
    /**
     * Returns the string containing the prompt of the being, containing
     * unreplaced variables.
     * @return
     */
    public String getPromptString() {
        return myPrompt.getPrompt();
    }

    public void setPrompt(String s) {
        myPrompt.setPrompt(s);
    }

    public boolean isReady() {
        return (!this.isFighting());
    }

    public boolean isFighting() {
        if (beingFighting == null) {
            return false;
        } else {
            return true;
        }
    }

    public void setBeingFighting(Being b) {
        beingFighting = b;
    }

    public Being getFighting() {
        return beingFighting;
    }

    public int getExperience() {
        return experience;
    }

    public int getFavor() {
        return favor;
    }

    public void increaseFavor(int increase) {
        this.favor += increase;
    }

    public void decreaseFavor(int decrease) {
        this.favor -= decrease;
    }

    public int getManaPoints() {
        return manaPoints;
    }

    public void tell(String s, boolean wrapAtEnd) {
        if (switched != null) {
            Player sPlayer = null;
            try {
                sPlayer = (Player) switched;
            } catch (ClassCastException e) {
                TrollAttack
                        .error("This should never happen, a being has been switched with a non-player!");
            }
            sPlayer.communication.print(s + (wrapAtEnd ? Util.wrapChar : ""));
        }
    }
    public void tell() {
        tell("", true);
    }
    public void tell(String s) {
        tell(s, true);
    }
    
    /**
     * Sends the given message to the being as an interrupt, meaning
     * that after the message, we also send a fresh prompt.
     * @param message The message to be sent to the being.
     */
    public void interrupt(String message) {
        tell(Util.wrapChar + message +
                Util.wrapChar + getPrompt(), false);
        
    }
    
    /**
     * Sends the string s to the being after sending the wrapping character.
     * @param s the String to be sent to the being.
     */
    public void prompt(String s) {
        tell(Util.wrapChar + s, false);
    }

    public void healAll() {
        hitPoints = maxHitPoints;
        manaPoints = maxManaPoints;
        movePoints = maxMovePoints;
    }

    public void increaseManaPoints(int increase) {
        manaPoints += increase;
        if (manaPoints > maxManaPoints) {
            manaPoints = maxManaPoints;
        }
    }

    public void decreaseManaPoints(int decrease) {
        manaPoints -= decrease;
    }

    public void increaseHitPoints(int increase) {
        hitPoints += increase;
        //TrollAttack.debug("HP is now " + hitPoints + " for " + getShort() + " - " + increase);
        if (hitPoints > maxHitPoints) {
            hitPoints = maxHitPoints;
        }
    }

    public void increaseMovePoints(int increase) {
        movePoints += increase;
        if (movePoints > maxMovePoints) {
            movePoints = maxMovePoints;
        }
    }

    /**
     * Level Improvement Calculations
     */
    public void increaseExperience(int e) {
        experience += e;
        if ((experience > Util.experienceLevel(level)) && isPlayer) {
            interrupt(	Communication.WHITE + "Congratulations! You have attained level " + ++level + "." + Util.wrapChar + 
            		"Levelling up is good thing.  When you level, you will gain more health, mana, and movement points.  You will also become more likely to hit other beings that you are fighting, and when you do hit them, you will do more damage.  If you want to maximize your gains, make sure you have a high constitution, wisdom, strength, and dexterity.");
            
            int healthIncrease = (int) (Math.random() * 9) + constitution / 2
                    - 2;
            int manaIncrease = (int) (Math.random() * 9) + wisdom / 2 - 2;
            int moveIncrease = (int) (Math.random() * 9) + strength / 4
                    + constitution / 4 - 2;
            if (healthIncrease < 3)
                healthIncrease = (int) (Math.random() * 3) + 1;
            if (manaIncrease < 3)
                manaIncrease = (int) (Math.random() * 3) + 1;
            if (moveIncrease < 3)
                moveIncrease = (int) (Math.random() * 3) + 1;
            hitPoints += healthIncrease;
            maxHitPoints += healthIncrease;
            manaPoints += manaIncrease;
            maxManaPoints += manaIncrease;
            movePoints += moveIncrease;
            maxMovePoints += moveIncrease;
            hitDamage.sizeOfDice += constitution / 6;
            hitDamage.addition++;
            hitSkill.sizeOfDice += dexterity / 6;
            rehash();
        }
    }

    public int getLevel() {
        return level;
    }

    public void addItem(Item i) {
        if (i.getClass() == Gold.class) {
            gold += i.cost;
        } else {
            beingItems.add(i);
        }
    }

    public Gold createGoldItem(int amount) {
        gold -= amount;
        return new Gold(amount);
    }

    private String[] inventory() {
        String[] itemList = new String[255];
        int n = 0;
        for (Item currentItem : beingItems) {
            String itemName = ( (currentItem.getClass() == DrinkContainer.class) 
                    ? (Communication.BLUE + currentItem.getShort())
                    : (Communication.GREEN + currentItem.getShort()));
            itemList[n] = itemName;
            n++;
        }
        String[] inventoryList = new String[n];
        for (int i = 0; i < n; i++) {
            inventoryList[i] = itemList[i];
        }
        return inventoryList;

    }

    public String getInventory() {
        String[] inv = inventory();
        String result = "Inventory:" + Util.wrapChar;
        for (int i = 0; i < inv.length; i++) {
            result += inv[i] + Util.wrapChar;
        }
        if (inv.length < 1) {
            result += Communication.PURPLE + "none" + Util.wrapChar;
        }
        return result;
    }

    public String getEquipment() {
        String result = "Equipment:" + Util.wrapChar;
        int count = 0;
        for(Equipment currentItem : equipment) {
            result += Communication.GREEN
                    + Util.uppercaseFirst(currentItem.getWearLocation())
                    + Communication.WHITE + ": " + currentItem.getShort()
                    + Util.wrapChar;
            count++;
        }
        if (count < 1) {
            result += Communication.PURPLE + "none" + Util.wrapChar;
        }
        return result;
    }

    public Item removeItem(String name) {
        Item newItem = findItem(name);
        if(newItem != null) {
        	beingItems.remove(newItem);
        	return newItem;
        }
        return null;
    }

    /**
     * Finds an item of any type matching the given string.
     * @param name The name describing the object.
     * @return
     */
    public Item findItem(String itemName) {
       return findItem(itemName, null);
    }
    /**
     * Finds an item of the given class.
     * @param command The string describing the object.
     * @param objectClass The class of the object we are looking for (if known).
     * @return The item if one is found matching the given class and command, null otherwise.
     */
	public Item findItem(String command, java.lang.Class objectClass) {
    	int skipItems = 0;
    	if(name.matches("^\\d\\..*$")) {
    		// We want to skip some items until we get to the desired item.
    		skipItems = new Integer(name.substring(0,1)) - 1;
    		name = name.substring(2);
    	}
		Item currentItem;
		Iterator<Item> i = beingItems.iterator();
		while (i.hasNext()) {
			currentItem = i.next();
			if (Util.contains(currentItem.getName(), command) && (--skipItems < 0)) {
				
				if(objectClass != null) {
					//TrollAttack.debug("Looking for only objects that are  " + objectClass.toString());
					if(objectClass.isInstance(currentItem)) {
						//TrollAttack.debug("Found " + currentItem.getShort() + " because it is of type " + objectClass.toString());
						return currentItem;
					} else {
						//TrollAttack.debug("not considering " + currentItem.getShort() + " because it is not of type " + objectClass.toString());
					}
				} else {
					//TrollAttack.debug("not checking for correct class.");
					return currentItem;
				}
			} else {
				//TrollAttack.debug("This " + currentItem.getName() + " doesn't look like a " + command + ".");
			}
		}
		return null;
	}

    public void eatItem(Item newEat) {
        Food newEaty = null;
        try {
            newEaty = (Food) newEat;
        } catch (ClassCastException e) {
            tell("You can't eat that!");
            return;
        }
        if (hunger < 1) {
            tell( "You are too full to eat that.");
        } else {
            hunger -= newEaty.getQuality();
            if(hunger < 1) {
            	hunger = 1;
            }
            beingItems.remove(newEaty);
            tell( "You eat " + newEat.getShort() + " and are now "
                    + getHungerString() + ".");
            roomSay("%1 eats " + newEat.getShort() + ".");

        }
    }

    public String drinkItem(Item newDrink) {
        DrinkContainer newDrinky;
       
        try {
            newDrinky = (DrinkContainer) newDrink;
            if (thirst < 1) {
                tell("You are too full to take a drink.");
            } else if(newDrink instanceof Fountain) {
                thirst = 0;
                tell("You drink from " + newDrink.getShort()
                        + " and are now " + getThirstString() + ".");
            } else if (newDrinky.getVolume() > 0) {
            	thirst -= 2;
                newDrinky.use();
                tell("You drink from " + newDrink.getShort()
                        + " and are now " + getThirstString() + ".");
            } else {
                tell("There is nothing left in " + newDrink.getShort() + ".");
            }
        } catch (ClassCastException e) {
        	tell("You can't drink from that!");
        }
        return null;
    }

    public String getHungerString() {
        switch (hunger) {
        case 0:
        case 1:
            return "completely stuffed";
        case 2:
        case 3:
            return "full";
        case 4:
        case 5:
            return "a little hungry";
        case 6:
        case 7:
            return "hungry";
        case 8:
        case 9:
            return "STARVING";
        default:
            return "unknowningly hungry";
        }
    }

    public String getThirstString() {
        switch (thirst) {
        case 0:
        case 1:
            return "satisfied";
        case 2:
        case 3:
            return "not thirsty";
        case 4:
        case 5:
            return "a little thirsty";
        case 6:
        case 7:
            return "thirsty";
        case 8:
        case 9:
            return "DYING OF THIRST";
        default:
            return "unknowningly thirsty";
        }
    }
    
    /**
     * Wears the given item visibly to other beings.
     * @param newWear The item to be worn.
     */
    public void wearItem(Item newWear) {
    	wearItem(newWear, true);
    }
    
    /**
     * Adds the given item to the being's equipment in use.
     * @param newWear The item to wear.
     * @param alertWorld Whether or not this is visible to other beings.
     */
    public void wearItem(Item newWear, boolean alertWorld) {
        Equipment newWearEquipment = null;
        if(newWear instanceof Equipment) {
            newWearEquipment = (Equipment) newWear;
        } else {
        	if(alertWorld) {
        		tell( "This can't be worn!");
        	}
        	return;
        }

        for(Equipment tmpEq : equipment) {
            if (tmpEq.wearLocation == null) {
                if(alertWorld) {
                	tell( "The game can't figure out how to wear thing thing, wierd!");
                }
                return;
            }
            if (tmpEq.wearLocation
                    .compareToIgnoreCase(newWearEquipment.wearLocation) == 0) {
            	if(alertWorld) {
            		tell( "You are already wearing something where this would go!");
            	}
                return;
            }

        }
        equipment.add(newWearEquipment);
        beingItems.remove(newWear);
        if(alertWorld) {
	        tell( "You wear " + newWear.getShort());
	        roomSay("%1 wears " + newWear.getShort());
        }

    }

    public void unwearItem(String name) {
        Equipment inHand = null;
        for(Equipment test : equipment) {
            if (Util.contains(test.getName(), name)) {
                beingItems.add(test);
                inHand = test;
                break;
                
            }
        }
        if (inHand == null) {
            tell( "You aren't wearing that!");
        } else {
           tell( "You remove " + inHand.shortDesc + ".");
            equipment.remove(inHand);
            roomSay("%1 removes " + inHand.getShort() + ".");
        }


    }

    public void dropAll() {

        // TrollAttack.message("Attempting to drop all.. " + items.getLength() +
        // " left.");
        Iterator<Item> i = beingItems.iterator();
        int count = 0;
        while (i.hasNext()) {
            count++;
            getActualRoom().addItem(removeItem(""));
            if (count > 100) {
                TrollAttack
                        .error("Dropped more than 100 items, infinite loop somewhere.");
                return;
            }
            // tell("Something hits the ground, hard.");
            // TrollAttack.message("Attempting to drop all.. " +
            // items.getLength() + " left.");
        }
        if (gold > 0) {
            getActualRoom().addItem(new Gold(gold));
            gold = 0;
        }

    }
    
    public void dropAllEquipment() {


        Iterator<Equipment> i = equipment.iterator();
        Vector<Equipment> forFloor = new Vector<Equipment>();
        int count = 0;
        while (i.hasNext()) {
            count++;
            Equipment droppable = i.next();
            forFloor.add(droppable);
        }
        for(Equipment e : forFloor) {
        	equipment.remove(e);
        	getActualRoom().addItem(e);
        }

    }

    public boolean isBuilder() {
        return false;
    }

    public void setLastActive(int i) {
    }

    public void kill() {
    	busyDoing = "dead";
    	dropAllEquipment();
        dropAll();
        save();
        getActualRoom().removeBeing(this);
    }

    public void kill(Being b) {
        kill();
    }

    public void roomSay(String string) {
        if(TrollAttack.gameRooms != null) {
        	getActualRoom().say(Util.uppercaseFirst(string), this);
        } else {
        	TrollAttack.debug("Not saying words because mud isn't ready.");
        }
    	
    }

    public void score() {
        tell("Mobiles can't score, yet!");
    }

    public void setTitle(String s) {
        tell("Mobiles don't have titles!");
    }

    public void name(String s) {
        tell("Mobiles don't have names!");
    }

    public void setPassword(String s) {
        tell("Mobiles don't have passwords!");
    }

    public boolean checkPassword(String s) {
        setPassword(s);
        return false;
    }

    public void save() {
        tell("Mobiles don't need to save, use savearea.");
    }

	public void setStats(int str, int con, int cha, int dex, int inte, int wis) {
		strength = str;
		constitution = con;
		charisma = cha;
		dexterity = dex;
		intelligence = inte;
		wisdom = wis;
		
	}
    
    public Communication getCommunication() {
        return null;
    }

    public boolean canEdit(int i) {
        return false;
    }

    public void setArea(Area a) {
        tell("Mobiles can't edit areas!");
    }

    public void setBuilder(boolean b) {
        tell("Mobiles can't be buidlers!");
    }

    public void rehash() {
        ch = new CommandHandler(this);
    }

    public Area getArea() {
        return null;
    }

    public void quit() {
        tell("Mobiles can't quit!");
    }

    public String showBeing() {
        String results = Communication.WHITE + Util.uppercaseFirst(getShort())
                + "'s " + getInventory() + Util.wrapChar + Communication.WHITE
                + Util.uppercaseFirst(getShort()) + "'s " + getEquipment();
        return results;
    }

    public Item getItem(String name, boolean remove) {

        Item newItem = null;
        for (Item currentItem : beingItems) {

            if (Util.contains(currentItem.getName(), name)) {
                newItem = currentItem;
                break;
            } else {
            }
            
        }
        if (remove == true) {
            beingItems.remove(newItem);
        }
        return newItem;
    }

    public boolean isMobile() {
        return false;
    }

    public void switchWith(Being being) {
        if (being == this && switched != null && backupCH != null) {
              // unswitch
              
              switched.setSwitched(null);
              setSwitched( null );
              ch = backupCH;
          } else {
              // switch
              backupCH = ch;
              ch = being.ch;
              being.setSwitched(this);
              switched = being;
          }
      }

    public void setSwitched(Being b) {
        switched = b;
    }

    public double getTimePlayed() {
        return 0;
    }
    
    public static void addAbilityNodes(Document doc, Player player, Node beingNode) {
       for(Ability ability : player.getAbilitiesData().keySet()) {
           Node abilityNode = doc.createElement("ability");
           abilityNode.appendChild(Util.nCreate(doc, "name", ability.toString()));
           abilityNode.appendChild(Util.nCreate(doc, "proficiency", player.getProficiency(ability) + ""));
           beingNode.appendChild(abilityNode);
       }
        
    }

    public static void addEquipmentNodes(Document doc, Being being,
            Node beingNode) {
        for (Item item : being.beingItems) {
            Node itemNode = doc.createElement("item");
            Node[] attributes = item.getAttributeNodes(doc);
            for (int i = 0; i < attributes.length; i++) {
                try {
                    itemNode.appendChild(attributes[i]);
                } catch (DOMException e) {
                    TrollAttack.error("Could not print the attributes of item "
                            + item.toString());
                }
                // itemNode.getAttributes(attributes[i]);
                // Need to figure out how to create attributes using DOM
                // @TODO!!!
                // @TODO!!! @@TODO@@@!!TODO!!!
            }
            if (attributes.length < 1) {
                itemNode.appendChild(doc.createTextNode(item.vnum + ""));
            }
            beingNode.appendChild(itemNode);
        }
        for(Equipment eq : being.equipment) {
            beingNode.appendChild(Util.nCreate(doc, "equipment", eq.vnum + ""));
        }

    }
    public int getVnum() {
        return 0;
    }
    public int countExactItem(Item item) {
        int count = 0;
        Iterator<Item> i = beingItems.iterator();
        while (i.hasNext()) {
            if (item == i.next()) {
                count++;
            }
        }
        return count;
    }

    public int countExactEquipment(Equipment item) {
        int count = 0;
        for(Equipment eq : equipment) {
            if (item == eq) {
                count++;
            }
        }
        return count;
    }

    public Object clone() {
        try {
            Object me = super.clone();
            ((Being) me).ch = new CommandHandler((Being) me);
            return me;

        } catch (CloneNotSupportedException e) {
            // This should never happen
            throw new InternalError(e.toString());
        }
    }

    public double getProficiency(Ability ability) {
        AbilityData data = abilitiesData.get(ability);
        if(data == null) {
            return 0;
        } else {
            return data.proficiency;
        }
    }
    
    public boolean close(String direction) {
        Exit exit = getActualRoom().getExit(Exit.getDirection(direction));
        if(exit == null || !exit.isDoor() ) {
            tell("There is no door in that direction.");
            return false;
        }
        if(!exit.isOpen()) {
            tell("The door is already closed.");
            return false;
        } else {
            tell("You close the door.");
            roomSay("%1 closes the door.");
            exit.getDestinationRoom().say("The door to the " + exit.getOtherExit().getDirectionName() + " closes.");
            exit.close();
            return true;
        }
    }
    public boolean open(String direction) {
        Exit exit = getActualRoom().getExit(Exit.getDirection(direction));
        if(exit == null || !exit.isDoor() ) {
            tell("There is no door in that direction.");
            return false;
        }
        if(exit.isOpen()) {
            tell("The door is already open.");
            return false;
        } else {
            tell("You open the door.");
            roomSay("%1 opens the door.");
            exit.getDestinationRoom().say("The door to the " + exit.getOtherExit().getDirectionName() + " opens.");
            exit.open();
            return true;
        }
    }

    public Set<Ability> getAbilityList() {
        return abilitiesData.keySet();
    }
    public void practice(Ability ability) {
        practice(ability, 6, true);
    }
    public void practice(Ability ability, float proficiency) {
        practice(ability, proficiency, true);
    }
    public void practice(Ability ability, float proficiency, boolean safety) {
        if(ability == null) {
            TrollAttack.error("You can't practice a null ability!");
        }
        if(abilitiesData.containsKey(ability)) {
            // already know this skill
            if(!safety) {
                abilitiesData.put(ability, new AbilityData(proficiency));
            }
            return;
        }
        AbilityData data = new AbilityData(proficiency);
        abilitiesData.put(ability, data);
    }

    public void learnFromPractice(boolean success, Ability ability) {
       AbilityData data = abilitiesData.get(ability);
       AbilityData classData = getBeingClass().getAbilityData().get(ability);
       
       // If this is an ability of the class, then check the maxproficiency for learning.
       if(classData != null && data.proficiency < classData.maxProficiency) {
            // Can learn more still.
            float increase = (float)(success ? .9 : 0.3);
            data.proficiency += increase;
            data.proficiency *= 10;
            data.proficiency = Math.round(data.proficiency);
            data.proficiency /= 10;
            tell(Communication.GREEN + 
                    "You learn from your " + (success ? "sucess" : "failure") + ", and " + 
                    ability.toString() + " rises by " + 
                    increase + " to " + 
                    data.proficiency);
        }
    }

    public boolean isSuccess(Ability ability) {
        float probability = 100 * new Random().nextFloat();
        AbilityData data = abilitiesData.get(ability);
        return (data.proficiency > probability);
    }

    public void stopFollowing() {
        if(getFollowing() == null) {
            tell(Communication.GREEN + "You weren't following anything!");
            return;
        }
        if(getFollowing().followers.contains(this)) {
            getFollowing().followers.remove(this);            
        } else {
            try {
                tell(Communication.GREEN + "Hrm, they didn't know you were following them.");
                TrollAttack.error("Being didn't know that they were being followed.");
            } catch(Exception e) {
                
            }
        }
        tell(Communication.GREEN + "You stop following " + getFollowing().getShort() + ".");
        getFollowing().interrupt(Communication.GREEN + getShort() + " stops following you.");
        setFollowing(null);
    }
    public void startFollowing(Being victim) {
        if(victim == null) {
            tell(Communication.GREEN + "You can't follow nothing!");
            return;
        }
        if(getFollowing() != null) {
            stopFollowing();
        }
        victim.followers.add(this);
        tell(Communication.GREEN + "You start following " + victim.getShort() + ".");
        victim.interrupt(Communication.GREEN + getShort() + " starts following you.");
        setFollowing(victim);
    }

    private void setFollowing(Being victim) {
        following = victim;
    }
    public Being getFollowing() {
        return following;
    }

    public void move(int direction) {
        ch.handleCommand(Exit.directionName(direction));
    }
    
    /**
     * Moves this being to the given vnum. (Room is found by asking game via vnum).
     * If the given VNUM is valid but doesn't exist yet, we attempt to create the room.
     * @param vnum The VNUM of the room that we are trying to go to.
     * @return True if we successfully go to destination room, false if permission is denied.
     */
    public boolean transport(int vnum) {
        int oldRoom = getCurrentRoom();
        getActualRoom().removeBeing(this);
        getActualRoom().say(getShort() + " disappears in a whirl of smoke.");
        Room newRoom = TrollAttack.getRoom(vnum);
        if(newRoom == null) {
            if(canEdit(vnum)) {
                tell("Waving your hand, you form order from swirling chaos, and step into a new reality...");
            
	               newRoom = new Room(vnum);
	    	        TrollAttack.gameRooms.add(newRoom);
	    	        Area.test(vnum, TrollAttack.gameAreas).areaRooms.add(newRoom);
	    	        setCurrentRoom(newRoom);
            } else {
                setCurrentRoom(oldRoom);
                tell("You don't have permission to make that room (" + vnum + ")!");
                return false;
            }
        } else {
            setCurrentRoom(newRoom);
        }
        getActualRoom().addBeing(this);
        look();
        return true;
    }

    public void follow(Being player, int direction) {
        Room previousRoom = getActualRoom();
        previousRoom.removeBeing(this);
        roomSay(Communication.GREEN + "%1 follows " + player.getShort() + ".");
        setCurrentRoom(player.getActualRoom());
        getActualRoom().addBeing(this);
        Being[] ignores = {this, player};
        
        getActualRoom().say(Communication.GREEN + "%1 arrives following %2.",ignores);
        
        interrupt(Communication.GREEN + "You follow " + player.getShort() + "." + Util.wrapChar + getActualRoom().look(this));
        for(Being follower : followers) {
            if(follower.getActualRoom() == previousRoom) {
                follower.follow(player, direction);
            }
            
        }
        
    }

    public void wander() {
        Vector<Exit> exitList = getActualRoom().getWanderableExits();
        Roll chance = new Roll("1d" + exitList.size());
        Exit randomExit = exitList.get(chance.roll() - 1);

        ch.handleCommand(randomExit.getDirectionName());
    }

	public void sacAll() {
		// TODO Auto-generated method stub
		
	}

	public int getArmorClass() {
		int result = 0;
		for(Equipment e : equipment) {
			if(e instanceof Armor) {
				result += ((Armor)e).armorClass;
			}
		}
		return 0;
	}

	/**
	 * Figures out the max weight a being can carry.  If the being is over level 50 
	 * or a builder, they should be able to pick anything up (100000 for now, !TODO), 
	 * otherwise they can carry 15*strength.
	 * @return The max weight that the being can carry.
	 */
	public int getCarryingMax() {
		if(level > 50 || isBuilder()) {
			return 100000;
		}
		return strength * 15;
	}

	/**
	 * Gets the total weight of the items that they are currently carrying.
	 * @return The weight that they are currently carrying.
	 */
	public int getCarryingWeight() {
		int weight = 0;
		for(Item i : beingItems) {
			weight += i.weight;
		}
		return weight;
	}


	




}
