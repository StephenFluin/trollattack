/*
 * Nov 11, 2006
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2006
 * to Stephen Fluin.
 */
package TrollAttack.Items;

import java.util.Hashtable;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import TrollAttack.Being;
import TrollAttack.DataReader;
import TrollAttack.TrollAttack;
import TrollAttack.Util;
import TrollAttack.Commands.CommandHandler;

/**
 * Containers have a constant capacity, and a variable 
 * amount of other items.
 * @author PeEll
 */
public class Container extends Item {
    public String wearLocation;
    public Vector<Item> contents;
    public int capacity;
    public Container(int vnum, int itemWeight, int itemCost, String nom, String shortdes, String longdes) {
        super(vnum, itemWeight, itemCost, nom, shortdes, longdes);
        data.itemTypeMessage = "a container";
        contents = new Vector<Item>();
    }
	public Container(Item i) {
	    this(i.vnum, i.getWeight(), i.getCost(), i.getName(), i.getShort(), i.getLong());
	}
    public static String getItemType() {
    	return "container";
    }
    public void setCapacity(int i) {
    	capacity = i;
    }
    public int getRemainingCapacity() {
    	return capacity - contents.size();
    }
    public void add(Item i) {
    	contents.add(i);
    	//TrollAttack.debug("Contents of the portal after adding " + i.getShort() + " is " + contents.toString());
    	//Thread.dumpStack();
    }
    public Item get(String name) {
    	Item i = Util.findMember(contents, name);
    	return i;
    }
    public String getTypeData() {
        return "Container-----------" + Util.wrapChar +
        		"Capacity:\t" + capacity + "\tContents:\t" + contents.size();
    }
    public String getType() {
    	return getItemType();
    }
    public Node getTypeNode(Document doc) {
        Node data = doc.createElement("typeData");
        data.appendChild(Util.nCreate(doc, "capacity", capacity + ""));
        for(Item i: contents) {
        	//data.appendChild(Util.nCreate);
        }
        return data;
    }
	public Node[] getAttributeNodes(Document doc) {
	    // We should be using attributes, but I can't figure
	    // out how to get dom to make them.  TODO
		
		//TrollAttack.debug("Getting the attribs of a container.");
	    Node[] attributes = new Node[1];
	    attributes[0] = doc.createElement("contents");
	    for(Item item : contents) {
	    	attributes[0].appendChild(item.toInstanceNode(doc));
	    	//TrollAttack.debug("This container has " + item.getShort() + ".");
	    }
	    //TrollAttack.debug("Container is: " + attributes[0].toString());
	    return attributes;
	}
	public void setAttributesFromHash(Hashtable hash) {
		try{
			Hashtable hasht = (Hashtable)hash.get("contents");
			if(hasht == null) {
				// Container is empty.
				return;
			}
			//TrollAttack.debug("Container attributes: " + hasht.toString());
			
			
			Vector items = DataReader.linkedListify(hasht.get("item"));
			for(Object i : items) {
				add(DataReader.getItemFromObject(i));
			}
			
			
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		}
	}
	public void setup(int capacity) {
		this.capacity = capacity;
		
	}
	
	public Object clone() {
        Object something = super.clone();
        Container me = (Container)something;
        me.contents = new Vector<Item>();
        for(Item i : contents) {
        	me.contents.add((Item)i.clone());
        }
        return me;
	}
	public int countExactItem(Item item) {
		int count = 0;
		for(Item i : contents) {
			if(i == item) {
				count++;
			}
		}
		return count;
	}
}

