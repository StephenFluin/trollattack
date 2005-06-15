

package TrollAttack;
public class Item {
	int vnum, weight, hitDamage, type;
	String  shortDesc = "", longDesc = "", name = "";
	

	/**
	 * Wearable item Types
	 * Use these constants for referring to and checking item types.
	 */
	final public static int SWORD = 1;
	final public static int HELM = 2;
	final public static int BOOTS = 3;
	final public static int GREAVES = 4;
	final public static int RING = 5;
	
	public Item(int v, String n, int w, String s, String l, int t, int hd) {
	 vnum = v;
	 name = n;
	 weight = w;
	 shortDesc = s;
	 longDesc = l;
	 hitDamage = hd;
	 type = t;
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
	

		public String[] look() {
		String[] items = new String[255];
		return items;
	}
	public String getLong() {
		return longDesc;
	}
	public String getShort() {
		return shortDesc;
	}
	public String getName() {
		return name;
	}

}
