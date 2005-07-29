package TrollAttack;

import TrollAttack.Items.Equipment;
import TrollAttack.Items.Gold;
import TrollAttack.Items.Item;

/*
 * Created on May 9, 2005
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
public class Being {
	public int hitPoints, maxHitPoints, manaPoints, maxManaPoints, movePoints, maxMovePoints, hitLevel, experience, level, favor, gold, weight;
	int currentRoom, state = 0;
	int strength, intelligence, wisdom, dexterity, constitution, charisma, luck;
	public LinkedList items = new LinkedList();
	Room actualRoom = null;
	public LinkedList equipment = new LinkedList();
	
	public Roll hitDamage;
	public Roll hitSkill;
	/**
	 * States:
	 * 0 Awake/Alert
	 * 1 Sitting
	 * 2 Lying down
	 * 3 Sleeping
	 */
	
	
	public String shortDescription, name, longDesc;
	boolean isPlayer = false;
	Being beingFighting = null;
	private Prompt myPrompt = new Prompt();
	public int getCurrentRoom() {
		return currentRoom;
	}
	public Room getActualRoom() {
	    return actualRoom;
	}
	public Area getActualArea() {
	    return Area.test(currentRoom);
	}
	public void setCurrentRoom(int r) {
		currentRoom = r;
		actualRoom = TrollAttack.getRoom(currentRoom);
	}
	
	public int getState() {
	    return state;
	}
	public void setState( int newState ) {
	    state = newState;
	}
	public String getDoing() {
	    if(isFighting()) {
	        return "fighting";
	    } else if(state == 0) {
	        return "standing";
	    } else if(state == 1) {
	        return "sitting";
	    } else if(state == 2) {
	        return "lying down";
	    } else if(state == 3) {
	        return "sleeping";
	    } else {
	        return "something secret";
	    }
	    
	}
	
	public int[] getStatistics() {
	    int[] stats = {strength, intelligence, wisdom, dexterity, constitution, charisma, luck};
	    return stats;
	}
	public void setStatistics(int str, int intel, int wis, int dex, int con, int cha, int lck) {
	    int[] stats = {str, intel, wis, dex, con, cha, lck};
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
	
	public boolean isPlayer() {
	    return isPlayer;
	}
	public String getShort() {
		return getShort(null);
	}
	public String getShort(Being whoAmI) {
	    if(whoAmI == this) {
	        return "you";
	    } else {
	        return shortDescription;
	    }
	}
	public String getLong() {
	    return Communication.PURPLE + Util.uppercaseFirst(getShort()) + " is " + getDoing() + " here";
	}
	public String getName() {
	    return name;
	}
	public int getHitDamage() {
	    return hitDamage.roll();
	}
	public int getAverageHitDamage() {
	    return hitDamage.getAverage();
	}
	public String prompt() {
		return myPrompt.showPrompt(hitPoints, maxHitPoints, manaPoints, maxManaPoints, movePoints, maxMovePoints, experience, Util.experienceLevel(level + 1) - experience);
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
	    if(beingFighting == null) {
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
	    
	}
	public void healAll() {
	    hitPoints = maxHitPoints;
	    manaPoints = maxManaPoints;
	    movePoints = maxMovePoints;
	}
	public void increaseManaPoints(int increase) {
	    manaPoints += increase;
	    if(manaPoints > maxManaPoints) {
	        manaPoints = maxManaPoints;
	    }
	}
	public void decreaseManaPoints(int decrease) {
	    manaPoints -= decrease;
	}
	public void increaseHitPoints(int increase) {
	    hitPoints += increase;
	    if(hitPoints > maxHitPoints) {
	        hitPoints = maxHitPoints;
	    }
	}
	public void increaseMovePoints(int increase) {
	    movePoints += increase;
	    if(movePoints > maxMovePoints) {
	        movePoints = maxMovePoints;
	    }
	}
	/**
	 * Level Improvement Calculations
	 */
	public void increaseExperience(int e) {
	    experience += e;
	    if(experience > Util.experienceLevel(level) ) {
	        tell("You have attained level " + ++level + ".");
	        int healthIncrease = (int)(Math.random() * 9) + constitution / 2 - 2;
	        int manaIncrease = (int)(Math.random() * 9) + wisdom / 2 - 2;
	        int moveIncrease = (int)(Math.random() * 9) + strength / 4 + constitution  / 4 - 2;
	        if(healthIncrease < 3) healthIncrease = (int)(Math.random() * 3) + 1;
	        if(manaIncrease < 3) manaIncrease = (int)(Math.random() * 3) + 1;
	        if(moveIncrease < 3) moveIncrease = (int)(Math.random() * 3) + 1;
	        hitPoints += healthIncrease;
	        maxHitPoints += healthIncrease;
	        manaPoints += manaIncrease;
	        maxManaPoints += manaIncrease;
	        movePoints += moveIncrease;
	        maxMovePoints += moveIncrease;
	        hitDamage.sizeOfDice += (int)(Math.random() * 3) + strength - ((strength / 2) < 2 ? 0 : strength / 2 - 2);
	        hitDamage.addition++;
	        hitSkill.sizeOfDice += (int)(Math.random() * 1) + dexterity - ((dexterity / 2) < 2 ? 0 : dexterity / 2 - 2);
	    }
	}
	
	public int getLevel() {
	    return level;
	}
	
	public void addItem(Item i) {
		if(i.getClass() == Gold.class) {
		    gold += i.cost;
		} else {
		    items.add(i);
		}
	}
	public Gold dropGold(int amount) {
	    gold -= amount;
	    return new Gold(amount);
	}
	private String[] inventory() {
		String[] itemList = new String[255];
		int n = 0;
		Item currentItem;
		for(int i = 0;i < items.length(); i++ ) {
			currentItem = (Item)items.getNext();
			itemList[n] = currentItem.getShort();
			n++;
		}
		items.reset();
		String[] inventoryList = new String[n];
		for(int i = 0;i < n;i++) {
			inventoryList[i] = itemList[i];
		}
		return inventoryList;
		
	}
	public void pInventory() {
			String[] inv = inventory();
			tell("Your Inventory:");
			for(int i = 0; i < inv.length; i++ ) {
					tell( inv[i] );
			}
	}
	public String equipmentToString() {
		String results = "Your Equipment:\n";
		Equipment currentItem;
		while(equipment.itemsRemain()) {
		    currentItem = (Equipment)equipment.getNext();
		    results += Util.uppercaseFirst(currentItem.getWearLocation()) + ": " + currentItem.getShort() + "\n";
		}
		equipment.reset();
		return results;
	}
	public Item dropItem(String name) {
		Item newItem = null;
		Item currentItem;
		for(int i = 0; i < items.length(); i++) {
			currentItem = (Item)items.getNext();
				
			if(Util.contains( currentItem.name, name)) {
				newItem = currentItem;
				items.delete(currentItem);
				return newItem;
				//TrollAttack.message("Someone drops " + newItem.getShort() + ".");
			} else {
				//TrollAttack.error("looking at object i in room " + .getCurrentRoom());
			}
		}
		items.reset();
		return newItem; 
	
	}
	public Item findItem(String name) {
	    Item currentItem;
	    while(items.itemsRemain()) {
	        currentItem = (Item)items.getNext();
	        if(Util.contains(currentItem.getName(), name)) {
	            items.reset();
	            return currentItem;
	        }
	    }
	    items.reset();
	    return null;
	}
	public String wearItem(Item newWear) {
	    Equipment newWearEquipment; 
        try {
           newWearEquipment = (Equipment)newWear;   
        } catch(ClassCastException e) {
            return "You don't know how to wear that (Can't make it a piece of eq)!";
        }
        
        
        Equipment tmpEq;
        while(equipment.itemsRemain()) {
            tmpEq = (Equipment)equipment.getNext();
            if(tmpEq.wearLocation == null) {
                return "The game can't figure out how to wear thing thing, wierd!";
            }
            if(tmpEq
                    .wearLocation
                    .compareToIgnoreCase( newWearEquipment
                            .wearLocation ) == 0 
                    ) {
                return "You are already wearing something where this would go!";
            }
                
        }
        equipment.reset();
        equipment.add(newWear);
        items.delete(newWear);
        return "You wear " + newWear.getShort();
        
	}
	public String removeItem(String name) {
	    Item inHand = null;
	    String result = "";
	    while(equipment.itemsRemain()) {
	        inHand = (Item)equipment.getNext();
	        if(Util.contains(inHand.getName(), name)) {
		        items.add(inHand);
		        equipment.delete(inHand);
	        }
	    }
	    equipment.reset();
	    if(inHand == null) {
	        return "You aren't wearing that!";
	    } else {
	        result = "You remove " + inHand.shortDesc + ".";
	    }
	    return result;
	    
	}
	public void dropAll() {
	    
	    //TrollAttack.message("Attempting to drop all.. " + items.getLength() + " left.");
	    while(items.getLength() > 0 ) {
	        getActualRoom().addItem(dropItem(""));
	        //tell("Something hits the ground, hard.");
	        //TrollAttack.message("Attempting to drop all.. " + items.getLength() + " left.");
	    }
	    if(gold > 0) {
	        getActualRoom().addItem(new Gold(gold));
	        gold = 0;
	    }
	    
	}
}
