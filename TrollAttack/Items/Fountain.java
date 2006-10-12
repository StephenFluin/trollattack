/*
 * Created on Jul 17, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a ja`va game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
package TrollAttack.Items;

import java.util.Hashtable;

import org.w3c.dom.Attr;
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
public class Fountain extends Item {
    public Fountain(int vnum, int itemWeight, int itemCost, String nom, String shortdes, String longdes) {
        super(vnum, itemWeight, itemCost, nom, shortdes, longdes);
    }
    public Fountain(Item i) {
        this(i.vnum, i.weight, i.cost, i.name, i.shortDesc, i.longDesc);
    }
    public String getType() {
        return getItemType();
    }
    public static String getItemType() {
        return "fountain";
    }
    public String getTypeData() {
        return "-------Fountain-------";
    }
    public Node getTypeNode(Document doc) {
        Node data = doc.createElement("typeData");
        return data;
    }
	public Node[] getAttributeNodes(Document doc) {
	    // We should be using attributes, but I can't figure
	    // out how to get dom to make them.  TODO
	    // also, I am putting VNUM in here, but it doesn't really belong here..
	    Node[] attributes = new Node[1];
	    attributes[0] = Util.nCreate(doc, "vnum", vnum + "");
	    return attributes;
	}
	public void setAttributesFromHash(Hashtable hash) {
	}
}
