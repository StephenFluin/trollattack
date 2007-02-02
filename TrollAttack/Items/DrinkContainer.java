/*
 * Created on Jul 17, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a ja`va game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
package TrollAttack.Items;

import java.util.Vector;
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
public class DrinkContainer extends Item {
    int volume = 0, capacity = 0;
    public DrinkContainer(int vnum, int itemWeight, int itemCost, String nom, String shortdes, String longdes) {
        super(vnum, itemWeight, itemCost, nom, shortdes, longdes);
        setTypeMessage("a drink container");
    }
    public DrinkContainer(Item i) {
	    this(i.vnum, i.getWeight(), i.getCost(), i.getName(), i.getShort(), i.getLong());
	}
    
    public Vector effects = new Vector();
    
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
    public void setEffects(Vector newEffects) {
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
        return "Drink Container-----------" + Util.wrapChar +
        		"Volume:\t" + volume + "\tCapacity:\t" + capacity;
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
	    Node[] attributes = new Node[1];
	    attributes[0] = doc.createElement("volume");
	    attributes[0].setTextContent(volume + "");
	    //attributes[1] = Util.nCreate(doc, "vnum", vnum + "");
	    return attributes;
	}
	public void setAttributesFromHash(Hashtable hash) {
	    setVolume(new Integer((String)hash.get("volume")).intValue());
	}
	
    public boolean setAttribute(String attr, String value) {
        if(attr.compareToIgnoreCase("volume") == 0) {
           setVolume(Util.intize( value));
        } else if(attr.compareToIgnoreCase("capacity") == 0) {
            setCapacity(Util.intize( value));
        } else {
        	return super.setAttribute(attr, value);
        }
        return true;
    }
    public String getAttributeList() {
    	return super.getAttributeList() + " volume capacity";
    }
	
}
