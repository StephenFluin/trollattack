/*
 * Created on Jul 14, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
package TrollAttack.Items;

import java.util.Vector;


/**
 * @author PeEll
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WearLocation {
    private static int internalPointer = 0;
    private Vector wearLocations;
    private String name;
    private int value;
    private WearLocation(String name) {
        this.name = name;
        value = ++internalPointer;
    }
    public String toString() {
        return name;
    }
    public static WearLocation HOLD = new WearLocation("hold");
    public static WearLocation WIELD = new WearLocation("wield");
    public static WearLocation BODY = new WearLocation("body");
    public static WearLocation HEAD = new WearLocation("head");
    /*public static WearLocation HOLD = new WearLocation("legs");
    public static WearLocation HOLD = new WearLocation("light");
    public static WearLocation HOLD = new WearLocation("hands");
    public static WearLocation HOLD = new WearLocation("arms");
    public static WearLocation HOLD = new WearLocation("finger1");
    public static WearLocation HOLD = new WearLocation("finger2");
    public static WearLocation HOLD = new WearLocation("neck1");
    public static WearLocation HOLD = new WearLocation("neck2"); 
    public static WearLocation HOLD = new WearLocation("feet");
    public static WearLocation HOLD = new WearLocation("shield");
    public static WearLocation HOLD = new WearLocation("about");
    public static WearLocation HOLD = new WearLocation("waist");
    public static WearLocation HOLD = new WearLocation("wrist1");
    public static WearLocation HOLD = new WearLocation("wrist2");
    public static WearLocation HOLD = new WearLocation("dual_wield");
    public static WearLocation HOLD = new WearLocation("ears");
    public static WearLocation HOLD = new WearLocation("eyes");
    public static WearLocation HOLD = new WearLocation("missile_wield");
    public static WearLocation HOLD = new WearLocation("back");
    public static WearLocation HOLD = new WearLocation("face");
    public static WearLocation HOLD = new WearLocation("ankle1"); 
    public static WearLocation HOLD = new WearLocation("ankle2");*/

	//public static String wearLocToString(int location) {
	//    return wearLocations[location];
	//}
}

