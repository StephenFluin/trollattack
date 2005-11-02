/*
 * Created on Nov 1, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
package TrollAttack;

import java.util.Hashtable;

import TrollAttack.Commands.Ability;
import TrollAttack.Commands.abilities.Scry;


public class Class {
    String name;
    
    Class(String className) {
        name = className;
    }
    
    //A hashtable of 50 levels, each level giving a linkedList
    // of abilities for that level.
    Hashtable<Ability, AbilityData> abilities 
        = new Hashtable<Ability, AbilityData>();
    
    
    public void regAb(Ability ability, AbilityData data) {
        abilities.put(ability, data);
    }
    
    public class Wizard extends Class {

        Wizard() {
            super("Wizard");
            regAb(new Scry(), new AbilityData(10, 40));
        }
    }
    public class Warrior extends Class {
        Warrior() {
            super("Warrior");
            regAb(new Scry(), new AbilityData(5, 90));
        }
    }
    public class AbilityData {
        public int maxProficiency;
        public int level;
        AbilityData(int level, int maxProficiency ) {
            this.maxProficiency = maxProficiency;
            this.level = level;
        }
    }
}

