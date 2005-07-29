/*
 * Created on Jul 17, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
package TrollAttack.Items;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import TrollAttack.Util;

/**
 * @author PeEll
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Armor extends Equipment {
    
    public int armorClass = 0;
    public Armor(int vnum, int itemWeight, int itemCost, String nom, String shortdes, String longdes) {
        super(vnum, itemWeight, itemCost, nom, shortdes, longdes);
    }
    public Armor(Item i) {
        this(i.vnum, i.weight, i.cost, i.name, i.shortDesc, i.longDesc);
    }
    
    public void setArmorClass(int newAC) {
        armorClass = newAC;
    }
    public void setupArmor(int newAC, String newLocation) {
        setArmorClass(newAC);
        setWearLocation(newLocation);
    }

    public String getType() {
        return getItemType();
    }
    public static String getItemType() {
        return "armor";
    }
    public String getTypeData() {
        return "Armor-----------\nAC:\t" + armorClass + "\tWear Location:\t" + wearLocation;
    }
    public Node getTypeNode(Document doc) {
        Node data = doc.createElement("typeData");
        data.appendChild(Util.nCreate(doc, "armorClass", armorClass + ""));
        data.appendChild(Util.nCreate(doc, "wearLocation", wearLocation + ""));
        return data;
    }
}
