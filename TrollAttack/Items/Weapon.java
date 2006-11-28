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
public class Weapon extends Equipment {
    public Weapon(int vnum, int itemWeight, int itemCost, String nom, String shortdes, String longdes) {
        super(vnum, itemWeight, itemCost, nom, shortdes, longdes);
        setWearLocation("wielded");
        setTypeMessage("a weapon");
    }
    public Weapon(Item i) {
	    this(i.vnum, i.getWeight(), i.getCost(), i.getName(), i.getShort(), i.getLong());
	}
    
    public Roll damage = new Roll("0d0");
    
    public void setDamage(String dmg) {
        damage = new Roll(dmg);
    }
    public int getHitDamage() {
        return damage.roll();
    }
    public String getType() {
        return getItemType();
    }
    public static String getItemType() {
        return "weapon";
    }
    public String getTypeData() {
        return "Weapon-----------\nDamage:\t" + damage.toString();
    }
    public Node getTypeNode(Document doc) {
        Node data = doc.createElement("typeData");
        data.appendChild(Util.nCreate(doc, "damage", damage.toString() + ""));
        return data;
    }
}
