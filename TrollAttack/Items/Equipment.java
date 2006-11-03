/*
 * Created on Jul 17, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
package TrollAttack.Items;

import TrollAttack.TrollAttack;

/**
 * @author PeEll
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Equipment extends Item {
    public String wearLocation;
    public Equipment(int vnum, int itemWeight, int itemCost, String nom, String shortdes, String longdes) {
        super(vnum, itemWeight, itemCost, nom, shortdes, longdes);
    }
    public static String getItemType() {
    	return "equipment";
    }
    public String getType() {
    	return getItemType();
    }
    public String getWearLocation() {
        return wearLocation;
    }
    public void setWearLocation(String newLocation) {
        wearLocation = newLocation;
        //TrollAttack.message("Using new wearloc: " + newLocation);
    }
}
