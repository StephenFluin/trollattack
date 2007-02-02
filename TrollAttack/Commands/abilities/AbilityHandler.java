/*
 * Created on Nov 8, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
package TrollAttack.Commands.abilities;

import TrollAttack.Roll;
import TrollAttack.TrollAttack;
import TrollAttack.Commands.Ability;

import java.util.Vector;

public class AbilityHandler {
    private Vector<Ability>  abilities = new Vector<Ability>();
    public AbilityHandler() {
        register(new Scan());
        register(new Track());
        register(new Heal());
        register(new OffensiveSpell("magic missile", 7, 5, "You manifest a ball of violent light, rocketing towards %1.", "%1 manifests a ball of violent light, rocketing towards %2.") { });
        register(new OffensiveSpell("burning hand", 8, 15, "You lay a burning hand upon %1.", "%1 lays a burning hand upon %2.") {});
        register(new OffensiveSpell("fire burst",10, 20, "You launch a burst of flames against %1.", "A burst of flames flies from %1 at %2."));
        register(new OffensiveAttack("smack", "You smack %1.", "%1 smacks %2.", new Roll("1d4"), new Roll("3d2")));
        register(new OffensiveAttack("pound", "You slam into %1.", "%1 pounds on %2.",new Roll("2d6"), new Roll("2d3")));
        register(new OffensiveAttack("punch", "You punch at %1.", "%1 punches %2.",new Roll("4d2"), new Roll("3d3")));
        
        
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

    public Vector<Ability> getList() {
        TrollAttack.debug("Master list of abilities is being used, there are currently " + abilities.size());
        return abilities;
    }
}
