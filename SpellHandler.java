/*
 * Created on May 29, 2005
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
public class SpellHandler {
	LinkedList spellList;
	public SpellHandler() {
		spellList = new LinkedList(false, 0);
		
		//TrollAttack.print("Starting spell registration...");
		registerSpell(new Heal("heal"));

	}

	public void registerSpell( Spell c) {
	    spellList.add(c);
	}
	public void handleSpell( String spellString) {
		String[] spellParts = spellString.split(" ");
		String spellParameter = "";
		if(spellParts.length > 0 ) {
		    spellString = spellParts[0];
		    if(spellParts.length > 1 ) {
		        for(int i = 1; i < spellParts.length; i ++ ) {
		            if(i > 1) {
		                spellParameter += " ";
		            }
		            spellParameter += spellParts[i];
		        }
		    }
		} else {
		    spellString = "";
		}
		
		
		Spell spell = (Spell)spellList.getClosest(spellString);
		if(spellString.length() > 0 && spell != null) {
		    if(spell.isPeaceful() && TrollAttack.player.isFighting()) {
		        TrollAttack.print("You can't cast that while fighting!");
		    } else {
			    if(spellParts.length > 1) {
	
				        spell.execute(spellParameter);
					
				} else {
					spell.execute();
				}
		    }
		} else {
			TrollAttack.print("Cast what spell?");
		}
		
	}
	// Spells
	class Heal extends Spell {
		public Heal(String s) { super(s, 6); }
		public void execute() {
			if(TrollAttack.player.manaPoints > 6) {
			    
			}
		}
		public void execute( String s ) {
		   	if( s.compareToIgnoreCase("self") == 0 ) {
		   	    this.execute();
		   	}
		    for(int i = 0;i < 3;i++) {
		   	    TrollAttack.gameMobiles[i].hitPoints++;
		   	    }
		   	}
	}
}
