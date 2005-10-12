package TrollAttack;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import TrollAttack.Commands.CommandHandler;

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
public class Mobile extends Being implements Cloneable {
	public int vnum, clicks;
	
	public Mobile(int v, int leve, String n, int h, int mh, int hLevel, String hSkill, String hDamage, int mClicks, String s, String l) {
	    vnum = v;
		level = leve;
		isPlayer = false;
		name = n;
		hitPoints = h;
		maxHitPoints = mh;
		hitLevel = hLevel;
		hitSkill = new Roll(hSkill);
		hitDamage = new Roll(hDamage);
		shortDescription = s;
		longDesc = l;
		clicks = mClicks;
		ch = new CommandHandler(this);
		//TrollAttack.error("Creating mobile #" + v);
		this.setPrompt("<%h>");
	}
	
	// Is there a better way to duplicate a mobile?
	public Mobile( Mobile m ) {
	    vnum = m.vnum;
	    name = m.name;
	    gold = m.gold;
	    hitPoints = m.hitPoints;
	    maxHitPoints = m.maxHitPoints;
	    hitLevel = m.hitLevel;
	    hitSkill = m.hitSkill;
	    hitDamage = m.hitDamage;
	    shortDescription = m.shortDescription;
	    longDesc = m.longDesc;
	    clicks = m.clicks;
	    setPrompt("<%h>");
	    isPlayer = m.isPlayer;
	    level = m.level;
	    ch = new CommandHandler(this);
	}
	public String toString() {
		return vnum + ":" +
		name + "," +
		hitPoints + "/" + maxHitPoints + "," +
		super.getShort() + "," +
		longDesc;
					
	}
	public  boolean equals(Mobile mob) {
	    return (vnum == mob.vnum);
	}
	public String getLong() {
		return longDesc;
	}
	public String getname() {
		return name;
	}
	public int getClicks() {
		return clicks;
	}

	public Node toNode(Document doc) {
		   
		    Node m = doc.createElement("mobile");
		    LinkedList attribs = new LinkedList();
		    attribs.add(Util.nCreate(doc, "vnum", vnum + ""));
		    attribs.add(Util.nCreate(doc, "name", name + ""));
		    attribs.add(Util.nCreate(doc, "level", level + ""));
		    attribs.add(Util.nCreate(doc, "short", getShort()));
		    attribs.add(Util.nCreate(doc, "long", longDesc + ""));
		    attribs.add(Util.nCreate(doc, "clicks", clicks + ""));
		    attribs.add(Util.nCreate(doc, "hp", hitPoints + ""));
		    attribs.add(Util.nCreate(doc, "maxhp", maxHitPoints + ""));
		    attribs.add(Util.nCreate(doc, "mana", manaPoints + ""));
		    attribs.add(Util.nCreate(doc, "maxmana", maxManaPoints + ""));
		    attribs.add(Util.nCreate(doc, "move", movePoints + ""));
		    attribs.add(Util.nCreate(doc, "maxmove",  maxMovePoints + ""));
		    attribs.add(Util.nCreate(doc, "hitlevel", hitLevel + ""));
		    attribs.add(Util.nCreate(doc, "hitskill", hitSkill + ""));
		    attribs.add(Util.nCreate(doc, "hitdamage", hitDamage.toString() + ""));
		    
	    
		    for(int i = 0; i < attribs.length(); i++) {
		        
		        Node newAttrib = (Node)attribs.getNext();
		        m.appendChild(newAttrib);
		    }
		    
		    return m;
		}
	public boolean isMobile() {
	    return true;
	}
}
