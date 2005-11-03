/*
 * Created on Nov 2, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
package TrollAttack.Classes;

import TrollAttack.Commands.abilities.Heal;
import TrollAttack.Commands.abilities.MagicMissile;
import TrollAttack.Commands.abilities.Scan;

public class Wizard extends Class {

    public Wizard() {
        super(Classes.Wizard);
        regAb(new MagicMissile(),   new AbilityData(1, 89));
        regAb(new Heal(),   new AbilityData(5, 89));
        regAb(new Scan(),           new AbilityData(10, 40));
    }
}
