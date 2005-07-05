package TrollAttack;
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
	int hitPoints, maxHitPoints, manaPoints, maxManaPoints, movePoints, maxMovePoints, hitSkill, experience, level, favor;
	int currentRoom, state = 0;
	protected LinkedList items = new LinkedList();
	Room actualRoom = null;
	
	private Item weapon = null;
	private Item helm = null;
	private Item boots = null;
	private Item greaves =  null;
	
	Roll hitDamage;
	/**
	 * States:
	 * 0 Awake/Alert
	 * 1 Sitting
	 * 2 Lying down
	 * 3 Sleeping
	 */
	
	
	String shortDescription, name;
	boolean isPlayer = false;
	Being beingFighting = null;
	private Prompt myPrompt = new Prompt();
	public int getCurrentRoom() {
		return currentRoom;
	}
	public Room getActualRoom() {
	    return actualRoom;
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
	 * Level Calculation
	 * The leveling is cacluated as such:
	 * 2^(level-2) * 2000
	 */
	public void increaseExperience(int e) {
	    experience += e;
	    if(experience > Util.experienceLevel(level) ) {
	        tell("You have attained level " + ++level + ".");
	        int healthIncrease = (int)(Math.random() * 10) + 10;
	        int manaIncrease = (int)(Math.random() * 10) + 10;
	        int moveIncrease = (int)(Math.random() * 10) + 10;
	        hitPoints += healthIncrease;
	        maxHitPoints += healthIncrease;
	        manaPoints += manaIncrease;
	        maxManaPoints += manaIncrease;
	        movePoints += moveIncrease;
	        maxMovePoints += moveIncrease;
	        hitDamage.sizeOfDice += (int)(Math.random() * 4);
	        hitSkill -= (int)(Math.random() * 6);
	    }
	}
	
	public int getLevel() {
	    return level;
	}
	
	public void addItem(Item i) {
		items.add(i);
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
	public void pEquipment() {
		tell("Your Equipment:");
		if(weapon != null) {
			tell("Weapon: " + weapon.getShort());
		}
		if(helm != null) {
		    tell("Helm: " + helm.getShort());
		}
		if(boots != null) {
		    tell("Boots: " + boots.getShort());
		}
		if(greaves != null) {
		    tell("Greaves: " + greaves.getShort());
		}
	}
	public Item dropItem(String name) {
		Item newItem = null;
		Item currentItem;
		for(int i = 0; i < items.length(); i++) {
			currentItem = (Item)items.getNext();
				
			if(Util.contains( currentItem.name, name)) {
				newItem = currentItem;
				items.delete(currentItem);
				//TrollAttack.message("Someone drops " + newItem.getShort() + ".");
			} else {
				//TrollAttack.error("looking at object i in room " + .getCurrentRoom());
			}
		}
		items.reset();
		return newItem; 
	
	}
	public String wearItem(String name) {
	    boolean success = false;
	    Item currentItem = null;
		for(int i = 0; i < items.length(); i++) {
			currentItem = (Item)items.getNext();
		    if(currentItem.getName().compareToIgnoreCase(name) == 0) {
		        Item newWear = currentItem;
		       // TrollAttack.error("newWear would go on your " + newWear.type);
		        switch(newWear.type) {
		            case Item.SWORD:
		                if(weapon == null) {
		                    weapon = newWear;
					        success = true;
		                }
		                break;
		            case Item.HELM:
		                if(helm == null) {
		                    helm = newWear;
		                    success = true;
		                }
		                break;
		            case Item.BOOTS:
		                if(boots == null) {
		                    boots = newWear;
		                    success = true;
		                }
		                break;
		            case Item.GREAVES:
		                if(greaves == null) {
		                    greaves = newWear;
		                    success = true;
		                }
		                break;
		        }
		        if(success) {
		            items.delete(currentItem);
		            items.reset();
		            return "You wear " + newWear.getShort();
		        } else {
		            items.reset();
		            return "You are already wearing something where this would go!";
		        }
		        
		    }
		}
		items.reset();
		return "You don't have that.";

	}
	public String removeItem(String name) {
	    Item inHand = null;
	    if(weapon != null && Util.contains(weapon.getName(), name)) {
	        inHand = weapon;
	        weapon = null;
	    } else if(helm != null && Util.contains(helm.getName(), name)) {
	        inHand = helm;
	        helm = null;
	    } else if(boots != null && Util.contains(boots.getName(), name)) {
	        inHand = boots;
	        boots = null;
	    } else if(greaves != null && Util.contains(greaves.getName(), name)) {
	        inHand = greaves;
	        helm = null;
	    }
	    if(inHand != null) {
	        items.add(inHand);
            return "You remove " + inHand.shortDesc;
	    } else {
	        return "You aren't wearing that!";
	    }
	    
	}
	public void dropAll() {
	    while(items.getLength() > 0 ) {
	        getActualRoom().addItem(dropItem(""));
	    }
	}
}
