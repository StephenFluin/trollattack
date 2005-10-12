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
import TrollAttack.LinkedList;
import TrollAttack.Roll;
import TrollAttack.Util;

/**
 * @author PeEll
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DrinkContainer extends Equipment {
    int volume = 0, capacity = 0;
    public DrinkContainer(int vnum, int itemWeight, int itemCost, String nom, String shortdes, String longdes) {
        super(vnum, itemWeight, itemCost, nom, shortdes, longdes);
    }
    public DrinkContainer(Item i) {
        this(i.vnum, i.weight, i.cost, i.name, i.shortDesc, i.longDesc);
    }
    
    public LinkedList effects = new LinkedList();
    
    public void setup(int volume, int capac) {
        setVolume(volume);
        setCapacity(capac);
    }
    public void setVolume(int vol) {
        volume = vol;
    }
    public void use() {
        setVolume(volume - 1);
    }
    public void setCapacity(int cap) {
        capacity = cap;
    }
    public int getCapacity() {
        return capacity;
    }
    public int getVolume() {
        return volume;
    }
    public void setEffects(LinkedList newEffects) {
        effects = newEffects;
    }
    public void addEffect(Effect effect) {
        effects.add(effect);
    }
    public String getType() {
        return getItemType();
    }
    public static String getItemType() {
        return "drinkcontainer";
    }
    public String getTypeData() {
        return "Drink Container-----------\nVolume:\t" + volume + "\tCapacity:\t" + capacity;
    }
    public Node getTypeNode(Document doc) {
        Node data = doc.createElement("typeData");
        data.appendChild(Util.nCreate(doc, "volume", volume + ""));
        data.appendChild(Util.nCreate(doc, "capacity", capacity + ""));
        return data;
    }
	public Node[] getAttributeNodes(Document doc) {
	    // We should be using attributes, but I can't figure
	    // out how to get dom to make them.  TODO
	    // also, I am putting VNUM in here, but it doesn't really belong here..
	    Node[] attributes = new Node[2];
	    attributes[0] = doc.createElement("volume");
	    attributes[0].setTextContent(volume + "");
	    attributes[1] = Util.nCreate(doc, "vnum", vnum + "");
	    return attributes;
	}
	public void setAttributesFromHash(Hashtable hash) {
	    setVolume(new Integer((String)hash.get("volume")).intValue());
	}
}
