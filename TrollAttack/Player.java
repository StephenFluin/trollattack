package TrollAttack;
import java.io.Serializable;

/*
 * Created on Mar 7, 2005
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
public class Player extends Being implements Serializable {
	//String prompt = "&G<&R%h&G/&R%H&G-&z%x&G> %T";
	int experience = 1, currentRoom = 0, state = 0;
	/**
	 * States:
	 * 0 Awake/Alert
	 * 1 Sitting
	 * 2 Lying down
	 * 3 Sleeping
	 */
	// HARDCODED MAX ITEMS - FIX THIS! @TODO!
	private Item[] playerItems = new Item[50];
	private Item weapon = null;
	private Item helm = null;
	private Item boots = null;
	private Item greaves =  null;
	public int getCurrentRoom() {
		return currentRoom;
	}
	public void setCurrentRoom(int r) {
		currentRoom = r;
	}
	public Player() {
		// Set the default player values here.
	    hitPoints = 50;
		maxHitPoints = 50;
		manaPoints = 40;
		maxManaPoints = 40;
		movePoints = 30;
		maxMovePoints = 30;
		hitSkill = 50;
		hitDamage = 3;
		level = 1;
		currentRoom = 1;
		shortDescription = "you";
	}
	public String toString() {
	    String r = "";
	    r += "HP: " + hitPoints + "\n" +
	    "MHP: " + maxHitPoints + "\n" +
	    "MP: " + manaPoints + "\n" +
	    "MP: " + maxManaPoints + "\n";
	    return r;
	}
	public int getState() {
	    return state;
	}
	public int getHitDamage() {
	    //TrollAttack.print("Reading player hit damage.");
	    int hd = hitDamage;
	    for(int j = 0;j < playerItems.length; j++ ) {
	        if(playerItems[j] != null) {
	            hd += playerItems[j].hitDamage;
	            //TrollAttack.print("Adding to player damage by " + playerItems[j].hitDamage);
	        }
	    }
	    return hd;
	}
	public boolean isReady() {
	    return !(isFighting() || state > 0);
	}
	public String getDoing() {
	    if(isFighting()) {
	        return "fighting";
	    } else if(state == 0) {
	        return "doing nothing";
	    } else if(state == 1) {
	        return "sitting";
	    } else if(state == 2) {
	        return "lying down";
	    } else {
	        return "something secret";
	    }
	    
	}
	public void addItem(Item i) {
		for(int j = 0;j < playerItems.length; j++ ) {
			if(playerItems[j] == null) {
				playerItems[j] = i;
				return;
			}
		}
		throw( new Error("TOO MANY PLAYER ITEMS!"));
	}
	private String[] inventory() {
		String[] items = new String[255];
		int n = 0;
		for(int i = 0;i < playerItems.length; i++ ) {
			if(playerItems[i] != null) {
				items[n] = playerItems[i].getShort();
				n++;
			}
		}
		String[] inventoryList = new String[n];
		for(int i = 0;i < n;i++) {
			inventoryList[i] = items[i];
		}
		return inventoryList;
		
	}
	public void pInventory() {
			String[] inv = inventory();
			TrollAttack.print("Your Inventory:");
			for(int i = 0; i < inv.length; i++ ) {
					TrollAttack.print( inv[i] );
			}
	}
	public void pEquipment() {
		TrollAttack.print("Your Equiptment:");
		if(weapon != null) {
			TrollAttack.print("Weapon: " + weapon.getShort());
		}
		if(helm != null) {
			TrollAttack.print("Helm: " + helm.getShort());
		}
		if(boots != null) {
		    TrollAttack.print("Boots: " + boots.getShort());
		}
		if(greaves != null) {
			TrollAttack.print("Greaves: " + greaves.getShort());
		}
	}
	
	public Item dropItem(String name) {
			Item newItem;
			for(int i = 0; i < TrollAttack.player.playerItems.length; i++) {
				if(TrollAttack.player.playerItems[i] != null) {
					
					if(TrollAttack.player.playerItems[i].name.compareToIgnoreCase(name) == 0) {
						newItem = TrollAttack.player.playerItems[i];
						TrollAttack.player.playerItems[i] = null;
						return newItem;
						
					} else {
						//TrollAttack.print("looking at object i in room " + TrollAttack.player.getCurrentRoom());
					}
				}
			}
			return null; 
		
	}
	
	
	
	public String wearItem(String name) {
	    boolean success = false;
		for(int i = 0; i < TrollAttack.player.playerItems.length; i++) {
			if(TrollAttack.player.playerItems[i] != null) {
			    if(TrollAttack.player.playerItems[i].getName().compareToIgnoreCase(name) == 0) {
			        Item newWear = TrollAttack.player.playerItems[i];
			       // TrollAttack.print("newWear would go on your " + newWear.type);
			        switch(newWear.type) {
			            case Item.SWORD:
			                if(TrollAttack.player.weapon == null) {
			                    TrollAttack.player.weapon = newWear;
						        success = true;
			                }
			                break;
			            case Item.HELM:
			                if(TrollAttack.player.helm == null) {
			                    TrollAttack.player.helm = newWear;
			                    success = true;
			                }
			                break;
			            case Item.BOOTS:
			                if(TrollAttack.player.boots == null) {
			                    TrollAttack.player.boots = newWear;
			                    success = true;
			                }
			                break;
			            case Item.GREAVES:
			                if(TrollAttack.player.greaves == null) {
			                    TrollAttack.player.greaves = newWear;
			                    success = true;
			                }
			                break;
			        }
			        if(success) {
			            TrollAttack.player.playerItems[i] = null;
			            return "You wear " + newWear.getShort();
			        } else {
			            return "You are already wearing something where this would go!";
			        }
			        
			    }
			}
		}
		
		return "You don't have that.";

	}
	public String removeItem(String name) {
	    Item inHand = null;
	    if(TrollAttack.player.weapon != null && Util.contains(TrollAttack.player.weapon.getName(), name)) {
	        inHand = TrollAttack.player.weapon;
	        TrollAttack.player.weapon = null;
	    } else if(TrollAttack.player.helm != null && Util.contains(TrollAttack.player.helm.getName(), name)) {
	        inHand = TrollAttack.player.helm;
	        TrollAttack.player.helm = null;
	    } else if(TrollAttack.player.boots != null && Util.contains(TrollAttack.player.boots.getName(), name)) {
	        inHand = TrollAttack.player.boots;
	        TrollAttack.player.boots = null;
	    } else if(TrollAttack.player.greaves != null && Util.contains(TrollAttack.player.greaves.getName(), name)) {
	        inHand = TrollAttack.player.greaves;
	        TrollAttack.player.helm = null;
	    }
	    if(inHand != null) {
	        int i = 0;
            while(TrollAttack.player.playerItems[i] != null) {
                i++;
            }
            TrollAttack.player.playerItems[i] = inHand;
            return "You remove " + inHand.shortDesc;
	    } else {
	        return "You aren't wearing that!";
	    }
	    
	}
}
