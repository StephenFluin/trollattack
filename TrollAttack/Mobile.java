package TrollAttack;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import TrollAttack.Commands.CommandHandler;
import java.util.Vector;

/*
 * Created on May 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author PeEll
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Mobile extends Being implements Cloneable {
    public int vnum, clicks;
    private boolean isWanderer;

    public Mobile(int v, int leve, String n, int h, int mh, int m, int mm, int mo, int mmo, int hLevel,
            String hSkill, String hDamage, int mClicks, String s, String l, boolean wander) {
        vnum = v;
        level = leve;
        isPlayer = false;
        name = n;
        hitPoints = h;
        maxHitPoints = mh;
        manaPoints = m;
        maxManaPoints = mm;
        movePoints = mo;
        maxMovePoints = mmo;
        hitLevel = hLevel;
        hitSkill = new Roll(hSkill);
        hitDamage = new Roll(hDamage);
        shortDescription = s;
        longDesc = l;
        clicks = mClicks;
        this.isWanderer = wander;
        ch = new CommandHandler(this);
        //TrollAttack.error("Creating mobile #" + v);
        this.setPrompt("<%h>");
    }

    public String toString() {
        return vnum + ":" + name + "," + hitPoints + "/" + maxHitPoints + ","
                + super.getShort() + "," + longDesc;

    }

    public boolean equals(Mobile mob) {
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

    public int getVnum() {
        return vnum;
    }
    
    public boolean isWanderer() {
        return isWanderer;
    }
    public Node toNode(Document doc) {

        Node m = doc.createElement("mobile");
        Vector<Node> attribs = new Vector<Node>();
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
        attribs.add(Util.nCreate(doc, "maxmove", maxMovePoints + ""));
        attribs.add(Util.nCreate(doc, "hitlevel", hitLevel + ""));
        attribs.add(Util.nCreate(doc, "hitskill", hitSkill + ""));
        attribs.add(Util.nCreate(doc, "hitdamage", hitDamage.toString() + ""));
        attribs.add(Util.nCreate(doc, "canTeach", canTeach ? "true"
                : "false"));
        attribs.add(Util.nCreate(doc, "wander", isWanderer() ? "true" : "false"));
        attribs.add(Util.nCreate(doc, "class", getClassName() + ""));
        attribs.add(Util.nCreate(doc, "gold", gold + ""));
        attribs.add(Util.nCreate(doc, "strength", strength + ""));
        attribs.add(Util.nCreate(doc, "constitution", constitution + ""));
        attribs.add(Util.nCreate(doc, "charisma", charisma + ""));
        attribs.add(Util.nCreate(doc, "dexterity", dexterity + ""));
        attribs.add(Util.nCreate(doc, "intelligence", intelligence + ""));
        attribs.add(Util.nCreate(doc, "wisdom", wisdom + ""));
        for(Node newAttrib : attribs) {
            m.appendChild(newAttrib);
        }

        return m;
    }

    public boolean isMobile() {
        return true;
    }

    public void setWanderer(boolean isWanderer) {
        this.isWanderer = isWanderer;
        
    }
}
