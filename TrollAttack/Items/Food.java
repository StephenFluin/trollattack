/*
 * Created on Jul 17, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a ja`va game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
package TrollAttack.Items;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import TrollAttack.Effect;
import TrollAttack.Roll;
import TrollAttack.Util;

/**
 * @author PeEll
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Food extends Item {
    int quality = 0;
    public Food(int vnum, int itemWeight, int itemCost, String nom, String shortdes, String longdes) {
        super(vnum, itemWeight, itemCost, nom, shortdes, longdes);
        setTypeMessage("food");
    }
    public Food(Item i) {
	    this(i.vnum, i.getWeight(), i.getCost(), i.getName(), i.getShort(), i.getLong());
	}
        
    public void setQuality(int dmg) {
        quality = dmg;
    }
    public int getQuality() {
        return quality;
    }

    public String getType() {
        return getItemType();
    }
    public static String getItemType() {
        return "food";
    }
    public String getTypeData() {
        return "Food-----------\nQuality:\t" + quality + "";
    }
    public Node getTypeNode(Document doc) {
        Node data = doc.createElement("typeData");
        data.appendChild(Util.nCreate(doc, "quality", quality + ""));
        return data;
    }
}
