package TrollAttack;

import java.util.*;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


import TrollAttack.Classes.*;
import TrollAttack.Classes.Class;
import TrollAttack.Classes.Class.*;
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
    public int hitPoints, maxHitPoints, manaPoints, maxManaPoints, movePoints,
            maxMovePoints, hitLevel, experience, level, favor, gold, weight;

    public int currentRoom = 1, state = 0, hunger = 0, thirst = 0,
            practiceSessions;

    int strength, intelligence, wisdom, dexterity, constitution, charisma,
            luck;

    public java.util.LinkedList<Item> beingItems = new java.util.LinkedList<Item>();

    public Being switched = null;

    public boolean canTeach = false;
    
    private Class beingClass = null;

    Room actualRoom = null;

    private Hashtable<Ability, AbilityData> abilitiesData = new Hashtable<Ability, AbilityData>();
    
    public Hashtable<Ability, AbilityData> getAbilitiesData() {
        return abilitiesData;
    }
    public LinkedList equipment = new LinkedList();

    // Allows mobiles to do anything a player can do, is this dangerous or
    // memory hoggery?
    public CommandHandler ch = null;

    public Roll hitDamage;

    public Roll hitSkill;

    /**
     * States: 0 Awake/Alert 1 Sitting 2 Lying down 3 Sleeping Should inverse
     * this list, and add fighting as a state so we can have "minpos"
     */

    public String shortDescription, name, longDesc;

    boolean isPlayer = false;

    Being beingFighting = null;

    private Prompt myPrompt = new Prompt();

    public int getCurrentRoom() {
        return currentRoom;
    }

    public Room getActualRoom() {
        Room result = TrollAttack.getRoom(currentRoom);
        // can't optimize with actualRoom because reloadWorld can't update it..
        if (result == null) {
            TrollAttack.error("Being doesn't have a correct currentroom ("
                    + currentRoom + ").");
        }
        return result;
    }

    public Area getActualArea() {
        return Area.test(currentRoom, TrollAttack.gameAreas);
    }

    public void setCurrentRoom(int r) {
        currentRoom = r;
        if (TrollAttack.gameRooms != null) {
            actualRoom = TrollAttack.getRoom(currentRoom);
        }
        if (r == 0) {
            throw (new NullPointerException());
        } else {
        }
    }

    public void setCurrentRoom(Room r) {
        actualRoom = r;
        currentRoom = r.vnum;
    }

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

    public void look() {
        look(getActualRoom());
    }
    public void look(Room room) {
        String[] looks = {};
        try {
            looks = room.look(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < looks.length; i++) {
            tell(looks[i] + "");
        }
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public String getShort() {
        return getShort(null);
    }

    public String getShort(Being whoAmI) {
        if (whoAmI == this) {
            return "you";
        } else {
            return shortDescription;
        }
    }

    public String getLong() {
        return Communication.PURPLE + Util.uppercaseFirst(getShort()) + " is "
                + getDoing() + " here";
    }

    public String getName() {
        return name;
    }

    public Class getBeingClass() {
        return beingClass;
    }
    public Class.Classes getClassName() {
        if(beingClass == null) {
            return Class.Classes.Unclassed;
        } else {
            return beingClass.getName();
        }
    }
    
    public void setBeingClass(Class newClass) {
        beingClass = newClass;
    }
    public void setBeingClass(Class.Classes className) {
        switch(className) {
        case Wizard:
            setBeingClass(new Wizard());
            //TrollAttack.debug("Setting to be a wiz.");
            break;
        case Warrior:
            setBeingClass(new Warrior());
            //TrollAttack.debug("Setting to be a war.");
            break;
        case Thief:
            setBeingClass(new Thief());
            //TrollAttack.debug("Setting to be a thief.");
            break;
        default:
            //TrollAttack.error("Couldn't figure out what class this being is to be!!!");
        }
    }
    public String setBeingClass(String value) {
        for(Classes classType : Class.Classes.values()) {
            if((classType.toString()).toLowerCase().startsWith(value.toLowerCase())) {
                setBeingClass(classType);
                //TrollAttack.debug("MATCH - Setting " + getShort() + " to be a " + getClassName() + " because of string " + value + ".");
                return "Setting " + getShort() + " to be a " + getClassName() + ".";
            } else {
                //TrollAttack.debug((classType.toString()).toLowerCase() + " did not start with '" + value + "'.");
            }
        }
        //TrollAttack.debug("Setting " + getShort() + " to be a " + getClassName() + " because of string " + value + ".");
        return "Setting " + getShort() + " to be a " + getClassName() + ".";
    }
    
    public int getHitDamage() {
        return hitDamage.roll();
    }

    public int getAverageHitDamage() {
        return hitDamage.getAverage();
    }

    public String prompt() {
        return myPrompt.showPrompt(
                hitPoints, 
                maxHitPoints, 
                manaPoints,
                maxManaPoints, 
                movePoints, 
                maxMovePoints, 
                experience, 
                Util.experienceLevel(level + 1) - experience,
                currentRoom,
                gold);
    }

    public String getPrompt() {
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

    public void tell(String s) {
        if (switched != null) {
            Player sPlayer = null;
            try {
                sPlayer = (Player) switched;
            } catch (ClassCastException e) {
                TrollAttack
                        .error("This should never happen, a being has been switched with a non-player!");
            }
            sPlayer.communication.print(s);
        }
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
        if (experience > Util.experienceLevel(level)) {
            tell("You have attained level " + ++level + ".");
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
            hitDamage.sizeOfDice += (int) (Math.random() * 3) + strength
                    - ((strength / 2) < 2 ? 0 : strength / 2 - 2);
            hitDamage.addition++;
            hitSkill.sizeOfDice += (int) (Math.random() * 1) + dexterity
                    - ((dexterity / 2) < 2 ? 0 : dexterity / 2 - 2);
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

    public Gold dropGold(int amount) {
        gold -= amount;
        return new Gold(amount);
    }

    private String[] inventory() {
        String[] itemList = new String[255];
        int n = 0;
        for (Item currentItem : beingItems) {
            itemList[n] = currentItem.getShort();
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
        Equipment currentItem;
        int count = 0;
        while (equipment.itemsRemain()) {
            currentItem = (Equipment) equipment.getNext();
            result += Communication.GREEN
                    + Util.uppercaseFirst(currentItem.getWearLocation())
                    + Communication.WHITE + ": " + currentItem.getShort()
                    + Util.wrapChar;
            count++;
        }
        equipment.reset();
        if (count < 1) {
            result += Communication.PURPLE + "none" + Util.wrapChar;
        }
        return result;
    }

    public Item dropItem(String name) {
        Item newItem = null;
        for (Item currentItem : beingItems) {

            if (Util.contains(currentItem.name, name)) {
                newItem = currentItem;
                beingItems.remove(currentItem);
                return newItem;
                // TrollAttack.message("Someone drops " + newItem.getShort() +
                // ".");
            } else {
                // TrollAttack.error("looking at object i in room " +
                // .getCurrentRoom());
            }
        }
        return newItem;

    }

    public Item findItem(String name) {
        Item currentItem;
        Iterator<Item> i = beingItems.iterator();
        while (i.hasNext()) {
            currentItem = i.next();
            if (Util.contains(currentItem.getName(), name)) {
                return currentItem;
            }
        }
        return null;
    }

    public String eatItem(Item newEat) {
        Food newEaty;
        try {
            newEaty = (Food) newEat;
        } catch (ClassCastException e) {
            return "You can't eat that!";
        }
        if (hunger < 1) {
            return "You are too full to eat that.";
        } else {
            hunger -= newEaty.getQuality();
            beingItems.remove(newEaty);
            return "You eat " + newEat.getShort() + " and are now "
                    + getHungerString() + ".";

        }
    }

    public String drinkItem(Item newDrink) {
        DrinkContainer newDrinky;
        try {
            newDrinky = (DrinkContainer) newDrink;
        } catch (ClassCastException e) {
            try {
                Fountain fountain = (Fountain) newDrink;
                thirst -= 2;
                return "You drink from " + newDrink.getShort()
                        + " and are now " + getThirstString() + ".";
            } catch (ClassCastException er) {
                return "You can't drink from that!";
            }
        }
        if (thirst < 1) {
            return "You are too full to take a drink.";
        } else {
            if (newDrinky.getVolume() > 0) {
                thirst -= 2;
                newDrinky.use();
                return "You drink from " + newDrink.getShort()
                        + " and are now " + getThirstString() + ".";
            } else {
                return "There is nothing left in " + newDrink.getShort() + ".";
            }

        }
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
            return "bloated";
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

    public String wearItem(Item newWear) {
        Equipment newWearEquipment;
        try {
            newWearEquipment = (Equipment) newWear;
        } catch (ClassCastException e) {
            return "You don't know how to wear that (Can't make it a piece of eq)!";
        }

        Equipment tmpEq;
        while (equipment.itemsRemain()) {
            tmpEq = (Equipment) equipment.getNext();
            if (tmpEq.wearLocation == null) {
                return "The game can't figure out how to wear thing thing, wierd!";
            }
            if (tmpEq.wearLocation
                    .compareToIgnoreCase(newWearEquipment.wearLocation) == 0) {
                return "You are already wearing something where this would go!";
            }

        }
        equipment.reset();
        equipment.add(newWear);
        beingItems.remove(newWear);
        return "You wear " + newWear.getShort();

    }

    public String removeItem(String name) {
        Item inHand = null;
        String result = "";
        while (equipment.itemsRemain()) {
            inHand = (Item) equipment.getNext();
            if (Util.contains(inHand.getName(), name)) {
                beingItems.add(inHand);
                equipment.delete(inHand);
            }
        }
        equipment.reset();
        if (inHand == null) {
            return "You aren't wearing that!";
        } else {
            result = "You remove " + inHand.shortDesc + ".";
        }
        return result;

    }

    public void dropAll() {

        // TrollAttack.message("Attempting to drop all.. " + items.getLength() +
        // " left.");
        Iterator<Item> i = beingItems.iterator();
        int count = 0;
        while (i.hasNext()) {
            count++;
            getActualRoom().addItem(dropItem(""));
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

    public boolean isBuilder() {
        return false;
    }

    public void setLastActive(int i) {
    }

    public void kill() {
        dropAll();
        getActualRoom().removeBeing(this);
    }

    public void kill(Being b) {
        kill();
    }

    public void roomSay(String string) {
        if (getActualRoom() == null) {

        } else {
            getActualRoom().say(Util.uppercaseFirst(string), this);
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
                if (remove == true) {
                    beingItems.remove(newItem);
                }
            } else {
            }
        }
        return newItem;
    }

    public boolean isMobile() {
        return false;
    }

    public void switchWith(Being b) {
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
        while (being.equipment.itemsRemain()) {
            beingNode.appendChild(Util.nCreate(doc, "equipment",
                    ((Item) (being.equipment.getNext())).vnum + ""));
        }
        being.equipment.reset();

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
        while (equipment.itemsRemain()) {
            if (item == equipment.getNext()) {
                count++;
            }
        }
        equipment.reset();
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
        if(data.proficiency < classData.maxProficiency) {
            // Can learn more still.
            tell("Prof is " + data.proficiency);
            float increase = (float)(success ? .9 : 0.3);
            data.proficiency += increase;
            //tell("Prof is " + data.proficiency);
            data.proficiency *= 10;
            //tell("Prof is " + data.proficiency);
            data.proficiency = Math.round(data.proficiency);
            //tell("Prof is " + data.proficiency);
            data.proficiency /= 10;
            //tell("Prof is " + data.proficiency);
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




}
