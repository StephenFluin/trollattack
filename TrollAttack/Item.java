

package TrollAttack;
public class Item {
	int vnum, weight, type;
	String  shortDesc = "", longDesc = "", name = "";
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
