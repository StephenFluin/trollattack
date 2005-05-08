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
public class Player {
	String prompt = "&G<&R%h&G/&R%H&G-&z%x&G> %T";
	int hitPoints = 60, maxHitPoints = 60, experience = 1, currentRoom = 0;
	
	// HARDCODED MAX ITEMS - FIX THIS! @TODO!
	private Item[] playerItems = new Item[50];
	private Item weapon = null;
	private Item helm = null;
	private Item gloves =  null;
	public int getCurrentRoom() {
		return currentRoom;
	}
	public void setCurrentRoom(int r) {
		currentRoom = r;
	}
	public Player() {
		currentRoom = 1;
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
		if(gloves != null) {
			TrollAttack.print("Gloves: " + gloves.getShort());
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
	
	public String wieldItem(String name) {
		if(TrollAttack.player.weapon == null) {
			for(int i = 0; i < TrollAttack.player.playerItems.length; i++) {
				if(TrollAttack.player.playerItems[i] != null) {
					if(TrollAttack.player.playerItems[i].getName().compareToIgnoreCase(name) == 0) {
						Item newWeapon = TrollAttack.player.playerItems[i];
						
						TrollAttack.player.weapon = newWeapon;
						TrollAttack.player.playerItems[i] = null;
						return "You wield " + newWeapon.getShort();
					}
				}
			}
			return "You don't have that.";
	
		} else {
			return "You are already wielding something!";
		}
	}
}
