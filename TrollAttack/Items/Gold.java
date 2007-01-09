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
import org.w3c.dom.ranges.RangeException;

import TrollAttack.TrollAttack;


/**
 * @author PeEll
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Gold extends Item {
    public Gold(int vnum, int itemWeight, int itemCost, String nom, String shortdes, String longdes) {
        super(vnum, itemWeight, itemCost, nom, shortdes, longdes);
        setTypeMessage("something valuable");
    }
    public Gold(int amount) {
        this(0, 0, amount, "gold coins", amount + "gold coins", amount + " gold coins are piled here.");
        if(amount > 1) {
            data.name = "gold coins";
            data.shortDesc = amount + " gold coins";
            data.longDesc = amount + " gold coins are piled here.";
        } else if (amount == 1) {
            data.name = "gold coin";
            data.shortDesc = "a gold coin";
            data.longDesc = "A single gold coin lies here.";
        } else if (amount == 0) {
        	data.name = "gold shavings";
        	data.shortDesc = "some gold shavings";
        	data.longDesc = "Some gold shavings lie here.";
        } else {
        	TrollAttack.error("Attempt to create negative gold detected.");
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
