/*
 * Created on Jul 17, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
package TrollAttack.Items;

/**
 * @author PeEll
 *
 * Equipment represents all items that can be worn.
 */
public class Equipment extends Item {
    public String wearLocation;
    public Equipment(int vnum, int itemWeight, int itemCost, String nom, String shortdes, String longdes) {
        super(vnum, itemWeight, itemCost, nom, shortdes, longdes);
        setTypeMessage("equipment");
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

    public boolean setAttribute(String attr, String value) {
    	 if(attr.equalsIgnoreCase("wear")) {
    		 setWearLocation(value);
    	 } else {
    		 return false;
    	 }
    	 return true;
    }
    public String getAttributeList() {
    	return super.getAttributeList() + " wear";
    }
}
