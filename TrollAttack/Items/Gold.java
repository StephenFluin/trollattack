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
import TrollAttack.LinkedList;
import TrollAttack.Roll;
import TrollAttack.Util;

/**
 * @author PeEll
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Gold extends Item {
    public Gold(int vnum, int itemWeight, int itemCost, String nom, String shortdes, String longdes) {
        super(vnum, itemWeight, itemCost, nom, shortdes, longdes);
    }
    public Gold(int amount) {
        this(0, 0, amount, "gold coins", amount + "gold coins", amount + " gold coins are piled here.");
        if(amount > 1) {
            name = "gold coins";
            shortDesc = amount + " gold coins";
            longDesc = amount + " gold coins are piled here.";
        } else {
            name = "gold coin";
            shortDesc = "a gold coin";
            longDesc = "A single gold coin lies here.";
        }
    }
    public String getType() {
        return getItemType();
    }
    public static String getItemType() {
        return "gold";
    }
    public String getTypeData() {
        return "------------GOLD-----------";
    }
    public Node getTypeNode(Document doc) {
        Node data = doc.createElement("typeData");
        return data;
    }
}
