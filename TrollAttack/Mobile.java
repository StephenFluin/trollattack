package TrollAttack;

/*
 * Created on May 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author PeEll
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Mobile extends Being {
	int vnum, reSpawn;
	String  longDesc = "";
	static String dataFile = "mobiles.xml";
	
	public Mobile(int v, int leve, String n, int h, int mh, int hSkill, String hDamage, int rSpawn, String s, String l) {
	 vnum = v;
	 level = leve;
	 isPlayer = false;
	 name = n;
	 hitPoints = h;
	 maxHitPoints = mh;
	 hitSkill = hSkill;
	 hitDamage = new Roll(hDamage);
	 shortDescription = s;
	 longDesc = l;
	 reSpawn = rSpawn;
	 //TrollAttack.error("Creating mobile #" + v);
	 this.setPrompt("<%h>");
	}
	
	// Is there a better way to duplicate a mobile?
	public Mobile( Mobile m ) {
	    vnum = m.vnum;
	    name = m.name;
	    hitPoints = m.hitPoints;
	    maxHitPoints = m.maxHitPoints;
	    hitSkill = m.hitSkill;
	    hitDamage = m.hitDamage;
	    shortDescription = m.shortDescription;
	    longDesc = m.longDesc;
	    reSpawn = m.reSpawn;
	    setPrompt("<%h>");
	    isPlayer = m.isPlayer;
	    level = m.level;
	}
	public String toString() {
		return vnum + ":" +
		name + "," +
		hitPoints + "/" + maxHitPoints + "," +
		super.getShort() + "," +
		longDesc;
					
	}
	public String getLong() {
		return longDesc;
	}
	public String getname() {
		return name;
	}
	public int getRespawnTime() {
		return reSpawn;
	}
}
