/*
 * Created on Nov 2, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
package TrollAttack.Classes;

import TrollAttack.Commands.abilities.Scan;

public class Warrior extends Class {
    public Warrior() {
        super(Classes.Warrior);
        regAb(new Scan(), new AbilityData(5, 90));
    }
}