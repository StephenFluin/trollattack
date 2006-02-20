/*
 * Created on Nov 8, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
package TrollAttack.Commands;

import TrollAttack.TrollAttack;
import TrollAttack.Commands.abilities.BurningHand;
import TrollAttack.Commands.abilities.Heal;
import TrollAttack.Commands.abilities.MagicMissile;
import TrollAttack.Commands.abilities.Scan;
import TrollAttack.Commands.abilities.Track;

import java.util.LinkedList;

public class AbilityHandler {
    private LinkedList<Ability>  abilities = new LinkedList<Ability>();
    public AbilityHandler() {
        register(new Scan());
        register(new Track());
        register(new Heal());
        register(new MagicMissile());
        register(new BurningHand());
        
    }
    
    private void register(Ability ability) {
        //TrollAttack.debug("Adding ability '" + ability.name + "' to list.");
        abilities.add(ability);
    }
    public Ability find(String abilityName) {
        for(Ability ability : abilities) {
            if(ability.name.compareToIgnoreCase(abilityName) == 0 ) {
                return ability;
            }
        }
        TrollAttack.error("Looking for ability '" + abilityName + "', not found!");
        return null;
    }

    public LinkedList<Ability> getList() {
        TrollAttack.debug("Master list of abilities is being used, there are currently " + abilities.size());
        return abilities;
    }
}
