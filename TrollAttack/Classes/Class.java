/*
 * Created on Nov 1, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
package TrollAttack.Classes;

import java.util.Hashtable;
import java.util.LinkedList;

import TrollAttack.TrollAttack;
import TrollAttack.Util;
import TrollAttack.Commands.Ability;


public class Class {
    Classes name;
    
    Class(Classes className) {
        name = className;
    }
    
    LinkedList<Ability> abilities = new LinkedList<Ability>();
    
    Hashtable<Ability, AbilityData> abilitiesData 
        = new Hashtable<Ability, AbilityData>();
    public static enum Classes {Unclassed, Wizard, Warrior, Thief};
    
    public void regAb(Ability ability, AbilityData data) {
        abilities.add(ability);
        abilitiesData.put(ability, data);
    }
    public Classes getName() {
        return name;
    }
    public String listAbilities() {
        String result = "";
        for(Ability ability : abilities) {
            result += ability.toString() + Util.wrapChar;
        }
        return result;
    }
    public Ability findAbility(String name) {
        return findAbility(name, 65);
    }
    public Ability findAbility(String name, int level) {
        Hashtable<Ability, AbilityData> data = getAbilityData();
        for(Ability ability : data.keySet()) {
            if(data.get(ability) == null) {
                TrollAttack.error("Unknown ability in playerfile.");
            }
            if(ability.name == null) {
                TrollAttack.error("Ability has no name,, what the?!");
            } else {
                //TrollAttack.debug("Name is " + name + " and abilityname is " + ability.name + ".");
            }
            if(data.get(ability).level <= level 
                        && 
                        ability
                        .name
                        .startsWith(
                                name
                                )) {
                return ability;
            }
        }
        return null;
    }
    public LinkedList<Ability> getAbilityList() {
        return abilities;
    }
    public Hashtable<Ability, AbilityData> getAbilityData() {
        return abilitiesData;
    }
}



