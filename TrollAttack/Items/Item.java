

package TrollAttack.Items;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import TrollAttack.LinkedList;
import TrollAttack.Roll;
import TrollAttack.Util;

public class Item {
	public int vnum, weight, cost;
	public boolean takeable = true;
	public String  shortDesc = "", longDesc = "", name = "";

	
	/*public String wearLocToString() {
	    return WearLocation.wearLocToString( wearLocation );
	}
	
	
	public static int getWearLocation(String location) {
	    for(int i = 0; i < wearLocations.length;i++) {
	        if(location.compareToIgnoreCase(wearLocations[i]) == 0) {
	            return i;
	        }
	    }
	    return 0;
	}*/
	
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
	final public static String[] itemTypes =
	{
	        "none", "armor", "weapon", "light", "scroll", "wand", "staff",  "_missile",
	        "treasure",  "potion", "furniture", "trash", 
	        "container", "drinkcon", "key", "food", "money", "pen", "boat",
	        "corpse", "fountain", "pill", "blood", "bloodstain",
	        "scraps", "pipe", "herbcon", "herb", "incense", "fire", "book", "switch",
	        "lever", "pullchain", "button", "dial", "rune", "runepouch", "match", "trap",
	        "map", "portal", "paper", "tinder", "lockpick", "spike", "disease", "oil",
	        "fuel", "missileweapon", "projectile", "quiver", "shovel",
	        "salve", "cook", "keyring", "odor", "chance"
	        };
	final public static int ITEM_ARMOR = getItemType("armor");
	final public static int ITEM_WEAPON = getItemType("weapon");
	
	final public static int getItemType(String type) {
	    for(int i = 0; i < itemTypes.length;i++) {
	        if(type.compareToIgnoreCase(itemTypes[i]) == 0) {
	            return i;
	        }
	    }
	    return 0;
	}
	public Item() {}
	public Item(int vnum, int itemWeight, int itemCost, String nom, String shortdes, String longdesc) {
	 this.vnum = vnum;
	 name = nom;
	 weight = itemWeight;
	 cost = itemCost;
	 shortDesc = shortdes;
	 longDesc = longdesc;
	}
	public Item(Item i) {
	    this(i.vnum, i.weight, i.cost, i.name, i.shortDesc, i.longDesc);
	}
	
	public String toString() {
		return vnum + ":" +
		name + "," +
		weight + "," +
		shortDesc + "," +
		longDesc;
					
	}
	public Node toNode(Document doc) {
		   
		    Node m = doc.createElement("item");
		    LinkedList attribs = new LinkedList();
		    attribs.add(Util.nCreate(doc, "vnum", vnum + ""));
		    attribs.add(Util.nCreate(doc, "name", name + ""));
		    attribs.add(Util.nCreate(doc, "short", getShort()));
		    attribs.add(Util.nCreate(doc, "long", longDesc + ""));
		    attribs.add(Util.nCreate(doc, "weight", weight + ""));
		    attribs.add(Util.nCreate(doc, "type", getType() + ""));
		    
		    for(int i = 0; i < attribs.length(); i++) {
		        
		        Node newAttrib = (Node)attribs.getNext();
		        m.appendChild(newAttrib);
		    }
		    
		    return m;
		}

		public String[] look() {
		String[] items = new String[255];
		return items;
	}
	public String getLong() {
		return longDesc;
	}
	public String getType() {
	    return "";
	}
	public boolean equals(Item item) {
	    return (vnum == item.vnum);
	          
	}
	public String getTypeData() {
	    return "Unspecified Type";
	}
	public void setLong(String longdesc) {
	    longDesc = longdesc;
	}
	public void setShort(String shortdesc) {
	    shortDesc = shortdesc;
	}
	public String getShort() {
		return shortDesc;
	}
	public String getName() {
		return name;
	}
	public void setName( String nom) {
	    name = nom;
	}
}
