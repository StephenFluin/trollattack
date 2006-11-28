/*
 * Nov 28, 2006
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
package TrollAttack.Items;

import TrollAttack.Being;

public class Corpse extends Container implements Disposable {
	public int disposalTime = 0;
	public Corpse(int vnum, int itemWeight, int itemCost, String nom,
			String shortdes, String longdes) {
		super(vnum, itemWeight, itemCost, nom, shortdes, longdes);
		disposalTime = 10;
	}

	public Corpse(Item i) {
		this(i.vnum, i.getWeight(), i.getCost(), i.getName(), i.getShort(), i.getLong());
	}
	public Corpse(Being b) {
		this(0, 300, 0, "corpse", "the corpse of " + b.getShort(), "The corpse of " + b.getShort() + " lies here.");
	}

	public boolean isDone() {
		return (disposalTime < 0);
	}

	public void decay() {
		disposalTime--;
		
	}

}

