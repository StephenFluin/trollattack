package TrollAttack;
import java.io.Serializable;
import TrollAttack.Commands.*;

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
	private Communication communication = null;
	private CommandHandler ch = null;

	public void look() {
	    TrollAttack.gameRooms[this.getCurrentRoom()].pLook();
	}
	public Player(Communication com) {
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
		ch = new CommandHandler(this);
		communication = com;
	}
	public void tell(String s) {
	    communication.print(s);
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
	public void setState( int newState ) {
	    state = newState;
	}
	public void handleCommand(String command) {
	    ch.handleCommand(command);
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
			for(int i = 0; i < playerItems.length; i++) {
				if(playerItems[i] != null) {
					
					if(playerItems[i].name.compareToIgnoreCase(name) == 0) {
						newItem = playerItems[i];
						playerItems[i] = null;
						return newItem;
						
					} else {
						//TrollAttack.print("looking at object i in room " + .getCurrentRoom());
					}
				}
			}
			return null; 
		
	}
	
	
	
	public String wearItem(String name) {
	    boolean success = false;
		for(int i = 0; i < playerItems.length; i++) {
			if(playerItems[i] != null) {
			    if(playerItems[i].getName().compareToIgnoreCase(name) == 0) {
			        Item newWear = playerItems[i];
			       // TrollAttack.print("newWear would go on your " + newWear.type);
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
			            playerItems[i] = null;
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
	        int i = 0;
            while(playerItems[i] != null) {
                i++;
            }
            playerItems[i] = inHand;
            return "You remove " + inHand.shortDesc;
	    } else {
	        return "You aren't wearing that!";
	    }
	    
	}
}
