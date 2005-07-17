/*
 * Created on Jul 17, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
package TrollAttack.Items;

import TrollAttack.Effect;
import TrollAttack.LinkedList;
import TrollAttack.Roll;

/**
 * @author PeEll
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Weapon extends Equipment {
    public Weapon(int vnum, int itemWeight, int itemCost, String nom, String shortdes, String longdes) {
        super(vnum, itemWeight, itemCost, nom, shortdes, longdes);
    }
    
    public Roll damage = new Roll("0d0");
    public LinkedList effects = new LinkedList();
    
    public void setDamage(String dmg) {
        damage = new Roll(dmg);
    }
    public int getHitDamage() {
        return damage.roll();
    }
    public void setEffects(LinkedList newEffects) {
        effects = newEffects;
    }
    public void addEffect(Effect effect) {
        effects.add(effect);
    }
    public String getType() {
        return getItemType();
    }
    public static String getItemType() {
        return "weapon";
    }
    public String getTypeData() {
        return "Weapon-----------\nDamage:\t" + damage.toString();
    }
}
