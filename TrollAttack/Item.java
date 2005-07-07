

package TrollAttack;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class Item {
	public int vnum, weight, type;
	public String  shortDesc = "", longDesc = "", name = "";
	Roll hitDamage;

	/**
	 * Wearable item Types
	 * Use these constants for referring to and checking item types.
	 */
	final public static int OTHER = 0;
	final public static int SWORD = 1;
	final public static int HELM = 2;
	final public static int BOOTS = 3;
	final public static int GREAVES = 4;
	final public static int RING = 5;
	public String typeToString(int type) {
	    if(type == SWORD) {
	        return "sword";
	    } else if(type == HELM) {
	        return "helm";
	    } else if(type == BOOTS) {
	        return "boots";
	    } else if(type == GREAVES) {
	        return "greaves";
	    } else if(type == RING) {
	        return "ring";
	    } else {
	        return "other";
	    }
	}
	public Item(int vnum, String nom, int weigh, String shortdes, String longdes, Roll hd, int type) {
	 this.vnum = vnum;
	 name = nom;
	 weight = weigh;
	 shortDesc = shortdes;
	 longDesc = longdes;
	 hitDamage = hd;
	 this.type = type;
	 //TrollAttack.error("Creating item of type " + t);
	}
	public Item(Item i) {
	    this(i.vnum, i.name, i.weight, i.shortDesc, i.longDesc, i.hitDamage, i.type);
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
		    attribs.add(Util.nCreate(doc, "type", typeToString(type) + ""));
		    attribs.add(Util.nCreate(doc, "hitDamage", hitDamage.toString() + ""));
		    
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
	public void setLong(String longdesc) {
	    longDesc = longdesc;
	}
	public void setShort(String shortdesc) {
	    shortDesc = shortdesc;
	}
	public int getHitDamage() {
	    return hitDamage.roll();
	}
	public void setHitDamage(String hitdamage) {
	    hitDamage = new Roll(hitdamage);
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
