

package TrollAttack.Items;

import java.util.Vector;
import java.util.Hashtable;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import TrollAttack.Roll;
import TrollAttack.Util;

public class Item implements Cloneable {
	public int vnum;
	public boolean takeable = true;
	public ItemData data;
	/**
	 * Effect Types
	 * These are constants representing each of the possible affect types.  This
	 * should eventually be used for both items and spells.
	 */
	final public static String[] effectTypes = 
	{
	        "none", "strength", "dexterity", "intelligence", "wisdom", "constitution",
	        "sex", "class", "level", "age", "height", "weight", "mana", "hit", "move",
	        "gold", "experience", "armor", "hitroll", "damroll", "save_poison", "save_rod",
	        "save_para", "save_breath", "save_spell", "charisma", "affected", "resistant",
	        "immune", "susceptible", "weaponspell", "luck", "backstab", "pick", "track",
	        "steal", "sneak", "hide", "palm", "detrap", "dodge", "peek", "scan", "gouge",
	        "search", "mount", "disarm", "kick", "parry", "bash", "stun", "punch", "climb",
	        "grip", "scribe", "brew", "wearspell", "removespell", "mentalstate", "emotion",
	        "stripsn", "remove", "dig", "full", "thirst", "drunk", "blood", "cook",
	        "recurringspell", "contagious", "xaffected", "odor", "roomflag", "sectortype",
	        "roomlight", "televnum", "teledelay"
	        };
	/**
	 * Item Types
	 * A list of all of the types of items.
	 */
	public enum ItemType	{
	        none, armor, weapon, light, scroll, wand, staff,  _missile,
	        treasure,  potion, furniture, trash, 
	        container, drinkcon, key, food, money, pen, boat,
	        corpse, fountain, pill, blood, bloodstain,
	        scraps, pipe, herbcon, herb, incense, fire, book, Switch,
	        lever, pullchain, button, dial, rune, runepouch, match, trap,
	        map, portal, paper, tinder, lockpick, spike, disease, oil,
	        fuel, missileweapon, projectile, quiver, shovel,
	        salve, cook, keyring, odor, chance
	        };
	
	final public static ItemType getItemType(String type) {
	    for(ItemType itemType : ItemType.values()) {
	        if(type.compareToIgnoreCase(itemType.toString()) == 0) {
	            return itemType;
	        }
	    }
	    return null;
	}
	public Item() {}
	public Item(int vnum, int itemWeight, int itemCost, String nom, String shortdes, String longdesc) {
	 this.vnum = vnum;
	 data = new ItemData();
	 setName(nom);
	 setWeight(itemWeight);
	 setCost(itemCost);
	 setShort(shortdes);
	 setLong(longdesc);
	 setTypeMessage("an item");
	 
	}
	public void setTypeMessage(String string) {
		data.itemTypeMessage = string;
		
	}
	public void setCost(int itemCost) {
		data.cost = itemCost;
		
	}
	public void setWeight(int itemWeight) {
		data.weight = itemWeight;
		
	}
	public Item(Item i) {
	    this(i.vnum, i.getWeight(), i.getCost(), i.getName(), i.getShort(), i.getLong());
	}
	
	public int getCost() {
		return data.cost;
	}
	public int getWeight() {
		return data.weight;
	}
	public String toString() {
		return vnum + ":" +
		getName() + "," +
		getWeight() + "," +
		getShort() + "," +
		getLong();
					
	}


	public String[] look() {
		String[] items = new String[255];
		return items;
	}
	public String getLong() {
		return data.longDesc;
	}
	public String getType() {
	    return getItemType();
	}
	public boolean equals(Item item) {
	    return (vnum == item.vnum);
	          
	}
	public String getTypeData() {
	    return "--------Item----------";
	}
	public void setLong(String longdesc) {
	    data.longDesc = longdesc;
	}
	public void setShort(String shortdesc) {
	    data.shortDesc = shortdesc;
	}
	public String getShort() {
		return data.shortDesc;
	}
	public String getName() {
		return data.name;
	}
	public void setName( String nom) {
	    data.name = nom;
	}
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            // This should never happen
            throw new InternalError(e.toString());
        }
    }

    public static String getItemType() {
        return "item";
    }
    
    public boolean setAttribute(String attr, String val) {
    	return false;
    }
    public String getAttributeList() {
    	return "name short long weight cost type";
    }
    
    /**
     * Sets up the item by passing in all item-specific data.
     */
    public void setup() {
    	
    }
    
    /**
     * This method is used to specify the type of an object, suitable
     * for use in a sentence such as "A gold watch is <armour|gold|a weapon>". 
     * @param i
     * @return
     */
    public String getItemTypeMessage(Item i) {
    	return data.itemTypeMessage;
    }
    
    /**
     * Creates the node (for doc) that contains all of the ALWAYS-true data about this item.
     * @param doc
     * @return
     */
    public Node getTypeNode(Document doc) {
        Node data = doc.createElement("typeData");
        return data;
    }
    
    /**
     * Creates an array of nodes that contain INSTANCE-specific data for this item.
     * @param doc
     * @return
     */
	public Node[] getAttributeNodes(Document doc) {
	    Node[] attributes = new Node[0];
	    return attributes;
	}
	
	/**
	 * Sets the instance-specific data from a hash.  For containers and deep-objects, 
	 * this should create clone items for child items as well.
	 * @param hash
	 */
	public void setAttributesFromHash(Hashtable hash) {
	    return;
	}
	
	/**
	 * Turns the item into a item-data node suitable for saving.
	 * @param doc
	 * @return
	 */
	public Node toItemNode(Document doc) {
	   
	    Node m = doc.createElement("item");
	    Vector<Node> attribs = new Vector<Node>();
	    attribs.add(Util.nCreate(doc, "vnum", vnum + ""));
	    attribs.add(Util.nCreate(doc, "name", getName() + ""));
	    attribs.add(Util.nCreate(doc, "short", getShort()));
	    attribs.add(Util.nCreate(doc, "long", getLong() + ""));
	    attribs.add(Util.nCreate(doc, "weight", getWeight() + ""));
	    attribs.add(Util.nCreate(doc, "type", getType() + ""));
	    attribs.add(Util.nCreate(doc, "cost", getCost() + ""));
	    attribs.add(getTypeNode(doc));
	    for(int i = 0; i < attribs.size(); i++) {
	        
	        Node newAttrib = (Node)attribs.get(i);
	        m.appendChild(newAttrib);
	    }
	    
	    return m;
	}
	
	/**
	 * Creates a item-instance node suitable for saving as an item 
	 * in a room, or as something someone is carrying.
	 * @param doc
	 * @return
	 */
	public Node toInstanceNode(Document doc) {
		Node m = doc.createElement("item");
        Node[] attribs = getAttributeNodes(doc);
        for (Node n : attribs) {
            m.appendChild(n);
        }
        if (attribs.length < 1) {
            m.appendChild(doc.createTextNode(vnum + ""));
        } else {
        	m.appendChild(Util.nCreate(doc,"vnum", vnum + ""));
        }
        return m;
	}
	
}
